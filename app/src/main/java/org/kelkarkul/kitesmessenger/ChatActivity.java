package org.kelkarkul.kitesmessenger;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatActivity extends Activity {
    ListView lv;
    ArrayList<HashMap<String,String>> list ;
    ChatListHandler clh;
    LinearLayout ll;
    StorageController sc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        TextView header_title = (TextView) findViewById(R.id.header_title);
        TextView sub_title = (TextView) findViewById(R.id.sub_title);
        final EditText msg_txt = (EditText) findViewById(R.id.edittext_msg);
        ImageButton btn_send = (ImageButton) findViewById(R.id.btn_send);
        ImageView iv = (ImageView) findViewById(R.id.back_key);
        lv = (ListView) findViewById(R.id.list_chat);
        ll = (LinearLayout) findViewById(R.id.user_profile);
        sc = new StorageController(ChatActivity.this);
        SharedPreferences sp = getSharedPreferences("user_conv",MODE_PRIVATE);
        header_title.setText(sp.getString("USER_NAME",""));
        list= sc.getConv(sp.getString("USER_ID",""));
        sub_title.setText("last live on 7:00 pm");
//        HashMap<String,String> map = new HashMap<String, String>();
//        map.put("USER_MSG","Awesome @");
        clh = new ChatListHandler(ChatActivity.this,list,R.layout.listview_chat,new String[]{"MSG"},new int[]{R.id.msg_right_chat});
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(msg_txt.getText().toString().trim().isEmpty())
                {
                    msg_txt.animate();
                    msg_txt.requestFocus();
                    msg_txt.setHint("Well , message cannot be empty !");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            msg_txt.setHint("Enter Message");
                        }
                    }, 1000);
                }
                else {
                    HashMap<String, String> map = new HashMap<String, String>();
                    SharedPreferences sp = getSharedPreferences("user_conv",MODE_PRIVATE);
                    map.put("MSG", msg_txt.getText().toString());
                    map.put("USER_ID", sp.getString("USER_ID",""));
                    map.put("MSG_STAT", "N");
                    list.add(map);
                    msg_txt.setText("");
                    StorageController sc = new StorageController(ChatActivity.this);
                    sc.insertMsg(map);
                    //if(list.size() >0)
                    //{
                    // clh = new ChatListHandler(ChatActivity.this,list,R.layout.listview_chat,new String[]{"USER_MSG"},new int[]{R.id.msg_right});
                    clh.setListData(list);
                    clh.notifyDataSetChanged();
                    scrollMyListViewToBottom();
                    //lv.invalidate();
                    // Toast.makeText(ChatActivity.this,String.valueOf(list.size()) , Toast.LENGTH_SHORT).show();
                    //}

                }
            }
        });
        lv.setAdapter(clh);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clh.notifyDataSetChanged();
                TextView msg_right=(TextView) view.findViewById(R.id.msg_right_chat);
                Toast.makeText(ChatActivity.this, msg_right.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent conv = new Intent(ChatActivity.this,ConversationActivity.class);
                startActivity(conv);
                finish();
            }
        });
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile_activity = new Intent(ChatActivity.this,ProfileActivity.class);
                startActivity(profile_activity);
            }
        });
    }
    private void scrollMyListViewToBottom() {
        lv.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                lv.setSelection(clh.getCount() - 1);
            }
        });
    }
}
