package org.kelkarkul.kitesmessenger;

/**
 * Created by Asgard on 24-06-2017.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by Asgard on 23-05-2017.
 */
public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    Context ctx ;

    public void onReceive(final Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        this.ctx = context;
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    final String message = currentMessage.getDisplayMessageBody();
                    // Show Alert
                    //int duration = Toast.LENGTH_LONG;
                    //Toast toast_s = Toast.makeText(context,String.valueOf(message.substring(message.length() - 4)), duration);
                    //toast_s.show();
                    Intent in = new Intent("android.intent.action.SmsReceiver").putExtra("incomingSms", message);
                    in.putExtra("incomingPhoneNumber", phoneNumber);
                    context.sendBroadcast(in);
                } // end for loop
            } // bundle is null
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);
        }
    }
}
