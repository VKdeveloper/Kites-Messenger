package org.kelkarkul.kitesmessenger;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
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
    Handler handler_beared ,handler_exit;
    boolean mHand =false;
    MediaPlayer m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        final TextView header_title = (TextView) findViewById(R.id.header_title);
        TextView sub_title = (TextView) findViewById(R.id.sub_title);
        final EditText msg_txt = (EditText) findViewById(R.id.edittext_msg);
        ImageButton btn_send = (ImageButton) findViewById(R.id.btn_send);
        ImageView iv = (ImageView) findViewById(R.id.back_key);
        lv = (ListView) findViewById(R.id.list_chat);
        ll = (LinearLayout) findViewById(R.id.user_profile);
        sc = new StorageController(ChatActivity.this);
        final SharedPreferences sp = getSharedPreferences("user_conv",MODE_PRIVATE);
        header_title.setText(sp.getString("USER_NAME",""));
        list= sc.getConv(sp.getString("USER_ID",""));
        sub_title.setText("last live on 7:00 pm");
        handler_beared = new Handler();
        handler_exit = new Handler();
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
                    playBeep();
                    mHand = true;
                    HashMap<String, String> map = new HashMap<String, String>();
                    SharedPreferences sp = getSharedPreferences("user_conv",MODE_PRIVATE);
                    map.put("MSG", msg_txt.getText().toString());
                    map.put("USER_ID", sp.getString("USER_ID",""));
                    map.put("MSG_STAT", "N");
                    list.add(map);
                    msg_txt.setFocusable(true);
                    msg_txt.setFocusableInTouchMode(true);
                    msg_txt.requestFocus();
                    msg_txt.setText("");
                    StorageController sc = new StorageController(ChatActivity.this);
                    sc.insertMsg(map);
                    //if(list.size() >0)
                    //{
                    //clh = new ChatListHandler(ChatActivity.this,list,R.layout.listview_chat,new String[]{"USER_MSG"},new int[]{R.id.msg_right});
                    clh.setListData(list);
                    clh.notifyDataSetChanged();
                    scrollMyListViewToBottom();
                    handler_exit.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            handler_beared.removeCallbacksAndMessages(null);
                            mHand = false;
                        }
                    },2500);

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
                profile_activity.putExtra("","");
                startActivity(profile_activity);
            }
        });
//        final Handler handler_beared = new Handler();


        //scrollMyListViewToBottom();
    }

    private synchronized void scrollMyListViewToBottom() {
        lv.post(new Runnable() {
            @Override
            public void run() {

                // Select the last row so it will scroll into view...
                SharedPreferences sp = getSharedPreferences("user_conv",MODE_PRIVATE);
                list= sc.getConv(sp.getString("USER_ID",""));
                clh = new ChatListHandler(ChatActivity.this,list,R.layout.listview_chat,new String[]{"MSG"},new int[]{R.id.msg_right_chat});
                lv.setAdapter(clh);
                //lv.setSelection(clh.getCount() - 1);
                lv.smoothScrollToPosition(clh.getCount() - 1);
                if(mHand) {
                    handler_beared.postDelayed(this, 1000);
                }
//                handler_exit.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        //clh.setListData(list);
//                        //runOnUiThread(new Runnable() {
//                        //  @Override
//                        // public void run() {
//                        handler_beared.removeCallbacksAndMessages(null);
//                        //handler_beared = null;
//                        mHand = false;
//                        // }
//                        //});
//                    }
//                },1020);
            }
        });
    }

    public void playBeep() {
        try {
            m= MediaPlayer.create(this, R.raw.send_message);
            if (m.isPlaying()) {
                m.stop();
                m.release();
            }
            m.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
