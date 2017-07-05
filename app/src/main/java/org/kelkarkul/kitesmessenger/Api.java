package org.kelkarkul.kitesmessenger;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Asgard on 24-06-2017.
 */
public class Api {
    Context ctx;
    String parent_url = "http://www.kelkarkul.org/vrutanta/app/";

    public Api(Context context)
    {
        this.ctx = context;
    }

    public String getApiUrl(String url)
    {
        String result ;
        result ="";
        switch(url)
        {
            case "user_det":
                result = parent_url+"messenger_user_det.php?get_det=true";
                break;
            default:
                return result;
        }
        return result;
    }

    public String otpApi(String url,String number,String otp)
    {
        String result,message ;
        result ="";
        SharedPreferences.Editor j = ctx.getSharedPreferences("OTP_SESSION", Context.MODE_PRIVATE).edit();
        switch(url)
        {
            case "otpapi":
                j.putString("otp",number);
                j.apply();
                j.commit();
                message ="Thanks%20for%20registering%20on%20KITES%20,%20your%20otp%20is%20"+otp;
                //message ="Thanks%20for%20registering%20,%20your%20otp%20is%20"+otp;
                result = "http://bulksms.mysmsmantra.com:8080/WebSMS/SMSAPI.jsp?username=VKDeveloper&password=vikrant7&sendername=KKITES&mobileno="+number+"&message="+message;
                break;
            default:
                return result;
        }
        return result;
    }
}
