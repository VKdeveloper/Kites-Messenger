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
 * Created by Asgard on 28-06-2017.
 */
public class SettingsListHandler extends SimpleAdapter  {
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
    int[] to_drawable;
    Context ctx;
    public SettingsListHandler(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        inflater = LayoutInflater.from(context);
        this.data_list = data;
        this.resource =resource;
        this.to_drawable = to;
        this.ctx =context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(resource, null);
            // Locate the TextViews in listview_item.xml
            holder.action_name = (TextView) convertView.findViewById(R.id.icon_name);
            //holder.flg_str = (TextView) convertView.findViewById(R.id.flg_str);
            holder.drawable = (ImageView) convertView.findViewById(R.id.icon_settings);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.action_name.setText(String.valueOf(data_list.get(position).get("NAME")));
        holder.drawable.setBackground(ctx.getResources().getDrawable(to_drawable[position]));
        //holder.user_id.setText(String.valueOf(data_list.get(position).get("USER_ID")));
       // holder.user_msg.setText(String.valueOf(data_list.get(position).get("USER_MSG")));
        return convertView;
    }

    private class ViewHolder {
        TextView action_name;
        ImageView drawable;
    }
}
