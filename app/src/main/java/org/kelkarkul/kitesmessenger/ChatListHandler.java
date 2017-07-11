package org.kelkarkul.kitesmessenger;

/**
 * Created by Asgard on 26-06-2017.
 */

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by Asgard on 24-06-2017.
 */
public class ChatListHandler extends SimpleAdapter {

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
    StorageController sc;
    Context ctx;
    public ChatListHandler(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        inflater = LayoutInflater.from(context);
        this.data_list = data;
        this.resource =resource;
        this.ctx = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        sc = new StorageController(ctx);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(resource, null);
            // Locate the TextViews in listview_item.xml
            //holder.user_name = (TextView) convertView.findViewById(R.id.user_name);
            //holder.user_id = (TextView) convertView.findViewById(R.id.user_id);
            holder.user_msg_left = (TextView) convertView.findViewById(R.id.msg_left);
            holder.user_msg_right = (TextView) convertView.findViewById(R.id.msg_right_chat);
            //holder.flg_str = (TextView) convertView.findViewById(R.id.flg_str);
            //holder.drawable = (ImageView) convertView.findViewById(R.id.user_dp);
            if (data_list.get(position).get("OWNER").equals("Y")) {
                holder.user_msg_right.setGravity(Gravity.END);
                holder.user_msg_right.setText(String.valueOf(data_list.get(position).get("MSG")));
                holder.user_msg_left.setVisibility(View.GONE);
                if(String.valueOf(data_list.get(position).get("MSG_STAT")).equals("N")) {
                    holder.user_msg_right.setCompoundDrawablesWithIntrinsicBounds(null, null, ctx.getResources().getDrawable(R.drawable.ic_single_check), null);
                }
                else if(String.valueOf(data_list.get(position).get("MSG_STAT")).equals("S"))
                {
                    holder.user_msg_right.setCompoundDrawablesWithIntrinsicBounds(null, null, ctx.getResources().getDrawable(R.drawable.ic_double_pending_check), null);
                }
                else
                {
                    holder.user_msg_right.setCompoundDrawablesWithIntrinsicBounds(null, null, ctx.getResources().getDrawable(R.drawable.ic_double_check), null);
                }
            } else {
                holder.user_msg_left.setGravity(Gravity.START);
                holder.user_msg_left.setText(String.valueOf(data_list.get(position).get("MSG")));
                holder.user_msg_right.setVisibility(View.GONE);
                if(String.valueOf(data_list.get(position).get("MSG_STAT")).equals("N")) {
                    holder.user_msg_left.setCompoundDrawablesWithIntrinsicBounds(null, null, ctx.getResources().getDrawable(R.drawable.ic_single_check), null);
                }
                else if(String.valueOf(data_list.get(position).get("MSG_STAT")).equals("S"))
                {
                    holder.user_msg_left.setCompoundDrawablesWithIntrinsicBounds(null, null, ctx.getResources().getDrawable(R.drawable.ic_double_pending_check), null);
                }
                else
                {
                    holder.user_msg_left.setCompoundDrawablesWithIntrinsicBounds(null, null, ctx.getResources().getDrawable(R.drawable.ic_double_check), null);
                }
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //holder.user_name.setText(String.valueOf(data_list.get(position).get("FULLNAME")));
        //holder.user_id.setText(String.valueOf(data_list.get(position).get("USER_ID")));


        //((TextView)convertView).setText(String.valueOf(data_list.get(position).get("USER_MSG")));
        //holder.user_msg_left.setText(String.valueOf(data_list.get(position-1).get("USER_MSG")));
        return convertView;
    }

    public void setListData(List<? extends Map<String, ?>> data){
        //notifyDataSetInvalidated();
        data_list = data;
        //notifyDataSetChanged();
    }


    private class ViewHolder {
        TextView user_msg_right,user_msg_left;
    }

    @Override
    public int getCount() {
        return data_list.size();
    }

    @Override
    public Object getItem(int position) {
        return data_list.get(position).get("MSG");
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
