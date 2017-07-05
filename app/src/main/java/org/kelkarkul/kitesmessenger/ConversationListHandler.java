package org.kelkarkul.kitesmessenger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.util.List;
import java.util.Map;

/**
 * Created by Asgard on 24-06-2017.
 */
public class ConversationListHandler extends SimpleAdapter {

    /**
     * Constructor
     *
     * @param context  The context where the View associated with this SimpleAdapter is running
     * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
     *                 Maps contain the data for each row, and should include all the entries specified in
     *                 "from"
     * @param resource Resource identifier of a view layout that defines the views for this list
     *                 item. The layout file should include at least those named views defined in "to"
     * @param from     A list of column names that will be added to the Map associated with each
     *                 item.
     * @param to       The views that should display column in the "from" parameter. These should all be
     *                 TextViews. The first N views in this list are given the values of the first N columns
     */
    LayoutInflater inflater;
    ViewHolder holder;
    List<? extends Map<String, ?>> data_list;
    int resource;
    Context ctx;

    public ConversationListHandler(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        inflater = LayoutInflater.from(context);
        this.data_list = data;
        this.resource =resource;
        this.ctx = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StorageController sc = new StorageController(ctx);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(resource, null);
            // Locate the TextViews in listview_item.xml
            holder.user_name = (TextView) convertView.findViewById(R.id.user_name);
            holder.user_id = (TextView) convertView.findViewById(R.id.user_id);
            holder.user_msg = (TextView) convertView.findViewById(R.id.user_msg);
            //holder.flg_str = (TextView) convertView.findViewById(R.id.flg_str);
            holder.drawable = (ImageView) convertView.findViewById(R.id.user_dp);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.user_name.setText(String.valueOf(data_list.get(position).get("FULLNAME")));
        holder.user_id.setText(String.valueOf(data_list.get(position).get("ID")));
        //Toast.makeText(ctx,String.valueOf(position) , Toast.LENGTH_SHORT).show();
        //holder.user_msg.setText(sc.getMsg(String.valueOf(data_list.get(position).get("ID"))));
        holder.user_msg.setText(sc.getMsg(sc.getUser(String.valueOf(data_list.get(position).get("USER_NUM")))));
        return convertView;
    }

    private class ViewHolder {
        TextView user_name,user_id,user_msg;
        ImageView drawable;
    }
}