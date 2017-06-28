package org.kelkarkul.kitesmessenger;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Asgard on 24-06-2017.
 */
public class UserStat {
    Context context;
    public UserStat(Context ctx)
    {
        this.context = ctx;
    }

    public boolean isFirstTimeRunner()
    {
        SharedPreferences get_fr = context.getSharedPreferences("messenger_user_stat",Context.MODE_PRIVATE);
        if(get_fr.getString("isFirstTimeRunner","").equals("true"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean isUserLoggedIn()
    {
        SharedPreferences get_fr = context.getSharedPreferences("messenger_user_stat",Context.MODE_PRIVATE);
        if(get_fr.getString("isUserLoggedIn","").equals("true"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
