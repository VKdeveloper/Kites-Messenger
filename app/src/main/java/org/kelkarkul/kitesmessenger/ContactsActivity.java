package org.kelkarkul.kitesmessenger;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class ContactsActivity extends AppCompatActivity {
    StorageController sc;
    ListView lv;
    TextView tv;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        sc=new StorageController(ContactsActivity.this);
        lv = (ListView) findViewById(R.id.contacts_list);
        tv = (TextView) findViewById(R.id.no_con);
        pb = (ProgressBar) findViewById(R.id.pb);
        new loadCon().execute();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final TextView user_name = (TextView) view.findViewById(R.id.user_name);
                final TextView user_msg = (TextView) view.findViewById(R.id.user_msg);
                AlertDialog alertDialog = new AlertDialog.Builder(ContactsActivity.this).create();
                alertDialog.setTitle(user_name.getText().toString());
                alertDialog.setMessage("Are you sure to start conversation with "+user_msg.getText().toString()+" ?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor sp = getSharedPreferences("user_conv",MODE_PRIVATE).edit();
                                sp.putString("USER_NUM",user_msg.getText().toString());
                                sp.putString("USER_NAME",user_name.getText().toString());
                                sp.apply();
                                sp.commit();
                                Intent chat_n = new Intent(ContactsActivity.this,ChatActivity.class);
                                startActivity(chat_n);
                                finish();
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
               // Toast.makeText(ContactsActivity.this, user_msg.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class loadCon extends AsyncTask<String,String,String>
    {
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        ConversationListHandler conversationListHandler;
        @Override
        protected String doInBackground(String... params) {
            String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '"
                    + ("1") + "'";
            String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
                    + " COLLATE LOCALIZED ASC";
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, selection
                    + " AND " + ContactsContract.Contacts.HAS_PHONE_NUMBER
                    + "=1",null, sortOrder);
//            Cursor phones = getContentResolver().query(
//                    ContactsContract.Contacts.CONTENT_URI, null, selection
//                            + " AND " + ContactsContract.Contacts.HAS_PHONE_NUMBER
//                            + "=1", null, sortOrder);
            if (phones.moveToFirst()) {
                do
                {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("FULLNAME", phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                    map.put("USER_MSG", phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                    map.put("USER_ID", phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)));
                    //String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    // String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    // Toast.makeText(getApplicationContext(),name, Toast.LENGTH_LONG).show()
                    list.add(map);
                }
                while (phones.moveToNext());
            }
            phones.close();
            //list = sc.getConversations();
            if(list.size() > 0)
            {
                //tv.setVisibility(View.GONE);
                //lv.setVisibility(View.VISIBLE);
                conversationListHandler = new ConversationListHandler(ContactsActivity.this,list,R.layout.listview_contacts,new String[]{"FULLNAME","USER_MSG","USER_ID"},new int[]{R.id.user_name,R.id.user_msg,R.id.user_id});
                //lv.setAdapter(conversationListHandler);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pb.setVisibility(View.GONE);
            if(list.size() > 0)
            {
                tv.setVisibility(View.GONE);
                lv.setVisibility(View.VISIBLE);
                lv.setAdapter(conversationListHandler);
            }
            else
            {
                lv.setVisibility(View.GONE);
                tv.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ic_menu_options_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                //Toast.makeText(this, "Google selected", Toast.LENGTH_SHORT).show();
                Toast.makeText(ContactsActivity.this, "Refreshing contact list..", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}

