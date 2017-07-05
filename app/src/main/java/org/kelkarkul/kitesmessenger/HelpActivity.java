package org.kelkarkul.kitesmessenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ListView lv =(ListView) findViewById(R.id.items_help);
        String[] items = new String[]{"Contacts us","System status","Terms and Privacy Policy","App info"};
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        for(int h = 0 ; h < items.length;h++)
        {
            HashMap<String,String> map = new HashMap<>();
            map.put("NAME",items[h]);
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(HelpActivity.this,list,android.R.layout.simple_list_item_1,new String[]{"NAME"},new int[]{android.R.id.text1});
        lv.setAdapter(adapter);
    }
}
