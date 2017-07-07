package org.kelkarkul.kitesmessenger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ConversationActivity extends AppCompatActivity {
    ListView lv;
    StorageController sc;
    TextView tv;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        lv =(ListView) findViewById(R.id.conversation_list);
        sc = new StorageController(ConversationActivity.this);
        tv =(TextView) findViewById(R.id.no_conver);
        pb=(ProgressBar) findViewById(R.id.pr_pb);
        new loadConv().execute();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final TextView user_name = (TextView) view.findViewById(R.id.user_name);
                final TextView user_num = (TextView) view.findViewById(R.id.user_num);
                final TextView user_id = (TextView) view.findViewById(R.id.user_id);
                                SharedPreferences.Editor sp = getSharedPreferences("user_conv",MODE_PRIVATE).edit();
                                sp.putString("USER_NUM",user_num.getText().toString());
                                sp.putString("USER_NAME",user_name.getText().toString());
                                sp.putString("USER_ID",user_id.getText().toString());
                                sp.apply();
                                sp.commit();
                                Intent chat_n = new Intent(ConversationActivity.this,ChatActivity.class);
                                startActivity(chat_n);
                                finish();

               // Toast.makeText(ConversationActivity.this, user_id.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        FontsOverride.setDefaultFont(this, "MONOSPACE", "font.ttf");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                FontsOverride.setDefaultFont(ConversationActivity.this, "MONOSPACE", "font.ttf");

            }
        }, 500);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ic_menu_options_conversations, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_conv:
                //Toast.makeText(this, "Google selected", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ConversationActivity.this,ContactsActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.action_settings:
                //Toast.makeText(this, "Google selected", Toast.LENGTH_SHORT).show();
                Intent i_s = new Intent(ConversationActivity.this,SettingsActivity.class);
                startActivity(i_s);
                break;
            case R.id.action_status:
                //Toast.makeText(this, "Google selected", Toast.LENGTH_SHORT).show();
                Intent a_s = new Intent(ConversationActivity.this,StatusActivity.class);
                startActivity(a_s);
                break;
        }
        return true;
    }

    public class loadConv extends AsyncTask<String,String,String>
    {
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        ConversationListHandler conversationListHandler;
        @Override
        protected String doInBackground(String... params) {
            //list = sc.getConversations();
            StorageController sc = new StorageController(ConversationActivity.this);
            list = sc.getUserDet();
//            String[] id = {"1","2","3"};
//            String[] name = {"Narendar Moody","Marky Zukarburg","Bell Gaets"};
//            String[] msg = {"Hello ...","Hi Marky !","Hello Dude ^_^"};
//            for(int i = 0 ; i < 3;i++)
//            {
//                HashMap<String,String> map = new HashMap<>();
//                map.put("ID",id[i]);
//                map.put("FULLNAME",name[i]);
//                map.put("USER_MSG",msg[i]);
//                map.put("USER_ID","");
//                map.put("USER_NUM","");
//                map.put("DP_URL","");
//                list.add(map);
//            }
            if(list.size() > 0)
            {
                //tv.setVisibility(View.GONE);
                //lv.setVisibility(View.VISIBLE);
                conversationListHandler = new ConversationListHandler(ConversationActivity.this,list,R.layout.listview_conversations,new String[]{"ID","FULLNAME","USER_NUM","USER_MSG"},new int[]{R.id.user_id,R.id.user_name,R.id.user_num,R.id.user_msg});
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
            //Toast.makeText(ConversationActivity.this, String.valueOf(list.size()), Toast.LENGTH_SHORT).show();
        }
    }
}
