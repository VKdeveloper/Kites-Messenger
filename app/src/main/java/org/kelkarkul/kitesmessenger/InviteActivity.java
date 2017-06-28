package org.kelkarkul.kitesmessenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class InviteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        ListView lv = (ListView) findViewById(R.id.invite_item);
        String[] items = new String[]{"Invite Friend"};
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        for(int k = 0 ; k < items.length;k++)
        {
            HashMap<String,String> item = new HashMap<>();
            item.put("NAME",items[k]);
            list.add(item);
        }
        SimpleAdapter sa = new SimpleAdapter(InviteActivity.this,list,android.R.layout.simple_list_item_1,new String[]{"NAME"},new int[]{android.R.id.text1});
        lv.setAdapter(sa);
    }
}
