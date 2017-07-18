package org.kelkarkul.kitesmessenger;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Asgard on 18-07-2017.
 */
public class ChatViewHandler {

    Context context;
    Activity activity;

    public ChatViewHandler(Context ctx , Activity act)
    {
        this.context =ctx;
        this.activity = act;
    }

    public void refresh()
    {
        StorageController sc = new StorageController(context);
        SharedPreferences sp = context.getSharedPreferences("user_conv",Context.MODE_PRIVATE);
        ArrayList<HashMap<String,String>> list = sc.getConv(sp.getString("USER_ID",""));
        ChatListHandler clh = new ChatListHandler(context,list,R.layout.listview_chat,new String[]{"MSG"},new int[]{R.id.msg_right_chat});
        ListView lv = (ListView) activity.findViewById(R.id.list_chat);
        lv.setAdapter(clh);
        //clh.notifyDataSetChanged();
        lv.smoothScrollToPosition(clh.getCount() - 1);
        Toast.makeText(context, "chat list Refreshed", Toast.LENGTH_SHORT).show();
    }

    public void refreshList()
    {
        StorageController sc = new StorageController(context);
        SharedPreferences sp = context.getSharedPreferences("user_conv",Context.MODE_PRIVATE);
        ArrayList<HashMap<String,String>> list = sc.getConv(sp.getString("USER_ID",""));
        ChatListHandler clh = new ChatListHandler(context,list,R.layout.listview_chat,new String[]{"MSG"},new int[]{R.id.msg_right_chat});
        ListView lv = (ListView) activity.findViewById(R.id.list_chat);
        lv.setAdapter(clh);
        clh.notifyDataSetChanged();
        //lv.smoothScrollToPosition(clh.getCount() - 1);
        Toast.makeText(context, "List Refreshed", Toast.LENGTH_SHORT).show();
    }

}
