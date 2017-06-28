package org.kelkarkul.kitesmessenger;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.provider.Contacts;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {
    ListView lv ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        FontsOverride.setDefaultFont(this, "MONOSPACE", "font.ttf");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                FontsOverride.setDefaultFont(SettingsActivity.this, "MONOSPACE", "font.ttf");

            }
        }, 500);
        lv = (ListView) findViewById(R.id.settings_options);
        String[] b = new String[]{"Account","Chats","Notifications","Data Usage","Contacts","Help"};
        int[] d = new int[]{R.drawable.ic_key,R.drawable.ic_chat,R.drawable.ic_notifications,R.drawable.ic_data_usage,R.drawable.ic_account,R.drawable.ic_help};
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        for (String aB : b) {
            HashMap<String, String> m = new HashMap<>();
            //HashMap<String,Drawable> m_d = new HashMap<>();
            m.put("NAME", aB);
            //m_d.put("IMAGE",getResources().getDrawable(d[h]));
            // m.put(getResources().getDrawable(d[h]));
            list.add(m);
        }
        SettingsListHandler slh = new SettingsListHandler(SettingsActivity.this,list,R.layout.listview_settings,new String[]{"NAME"},d);
        lv.setAdapter(slh);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 5)
                {
                    Intent help = new Intent(SettingsActivity.this,HelpActivity.class);
                    startActivity(help);
                }
                else if(position == 4)
                {
                    Intent invite = new Intent(SettingsActivity.this,InviteActivity.class);
                    startActivity(invite);
                }
            }
        });
    }
}
