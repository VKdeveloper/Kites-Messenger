package org.kelkarkul.kitesmessenger;

/**
 * Created by Asgard on 08-07-2017.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MessageService extends Service {
    private static Timer timer = new Timer();
    public static final String LOGIN_SESSION = "LOGIN_SESSION";
    protected int k =0;
    // textView is the TextView view that should display it
    //textView.setText(currentDateTimeString);
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }
    public void onCreate()
    {
        super.onCreate();
        //ctx = this;
        startService();
    }

    private void startService()
    {
        timer.scheduleAtFixedRate(new mainTask(), 0,900);

    }
    private class mainTask extends TimerTask
    {

        public void run()
        {

            toastHandler.sendEmptyMessage(0);
        }
    }

    public void onDestroy()
    {
        super.onDestroy();
        Toast.makeText(this, "Online service was stopped", Toast.LENGTH_SHORT).show();
    }

    private final Handler toastHandler = new Handler()
    {


        @Override
        public void handleMessage(Message msg)
        {
            SharedPreferences sp = getSharedPreferences("messenger_user_stat", MODE_PRIVATE);
           // Gson gson = new Gson();
            //StorageController sc = new StorageController(MessageService.this);
            //if(sc.getMessages().size() > 0) {

                new restore().execute(sp.getString("userNum", ""));
           // }
        }
    };



    public class restore extends AsyncTask<String,String,String>
    {

        @Override
        public synchronized String doInBackground(String... params)
        {
            HttpClient httpclient = new DefaultHttpClient();
            Api controller = new Api(MessageService.this);
            HttpPost httppost = new HttpPost(controller.getApiUrl("get_conv"));
            Log.i("URL",controller.getApiUrl("get_conv"));
            try {
                //String macAddress = wInfo.getMacAddress();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("number", params[0]));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                // According to the JAVA API, InputStream constructor do nothing.
                //So we can't initialize InputStream although it is not an interface
                InputStream inputStream = response.getEntity().getContent();

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder stringBuilder = new StringBuilder();

                String bufferedStrChunk = null;

                while((bufferedStrChunk = bufferedReader.readLine()) != null){
                    stringBuilder.append(bufferedStrChunk);
                }

                return stringBuilder.toString();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return "null";
        }

        @Override
        protected synchronized void onPostExecute(String result) {
            if(!result.equals("null"))
            {

                try {
                    JSONObject obj = new JSONObject(result);
                    StorageController sc = new StorageController(MessageService.this);
                    JSONArray user_conv = obj.getJSONArray("response");
                    if(k != user_conv.length()) {
                        if (user_conv.length() > 0) {
                            k++;
                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MessageService.this);
                            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            mBuilder.setSmallIcon(R.drawable.ic_stat_notification);
                            mBuilder.setContentTitle(user_conv.length() + " new conversation(s).");
                            mBuilder.setContentText("You may have new messages.");
                            mBuilder.setSound(soundUri);
                            Intent resultIntent = new Intent(MessageService.this, MessageService.class);
                            resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            TaskStackBuilder stackBuilder = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                stackBuilder = TaskStackBuilder.create(MessageService.this);
                                stackBuilder.addParentStack(ConversationActivity.class);
                                stackBuilder.addNextIntent(resultIntent);
                                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(k, PendingIntent.FLAG_UPDATE_CURRENT);
                                mBuilder.setContentIntent(resultPendingIntent);
                            }

                            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            mNotificationManager.notify(k, mBuilder.build());


                            for (int i = 0; i < user_conv.length(); i++) {
                                JSONObject c = user_conv.getJSONObject(i);
                                HashMap<String, String> h = new HashMap<String, String>();
                                h.put("FULLNAME", c.getString("FULLNAME"));
                                h.put("USER_NUM", c.getString("SENDER_NUM").trim());
                                h.put("SYNC_STATUS", "Y");
                                sc.insertUser(h);
                            }
                        }

                        SharedPreferences sp = getSharedPreferences("messenger_user_stat", MODE_PRIVATE);
                        new restoreMsgs().execute(sp.getString("userNum", ""));
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
            Log.i("RESULT",String.valueOf(result));
        }
    }

    public class restoreMsgs extends AsyncTask<String,String,String>
    {

        @Override
        public synchronized String doInBackground(String... params)
        {
            HttpClient httpclient = new DefaultHttpClient();
            Api controller = new Api(MessageService.this);
            HttpPost httppost = new HttpPost(controller.getApiUrl("user_msgs"));
            Log.i("URL",controller.getApiUrl("user_msgs"));
            try {
                //String macAddress = wInfo.getMacAddress();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("number", params[0]));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                // According to the JAVA API, InputStream constructor do nothing.
                //So we can't initialize InputStream although it is not an interface
                InputStream inputStream = response.getEntity().getContent();

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder stringBuilder = new StringBuilder();

                String bufferedStrChunk = null;

                while((bufferedStrChunk = bufferedReader.readLine()) != null){
                    stringBuilder.append(bufferedStrChunk);
                }

                return stringBuilder.toString();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return "null";
        }

        @Override
        protected void onPostExecute(String result) {
            //pr_bg.setVisibility(View.GONE);
            if(!result.equals("null"))
            {

                try {
                    JSONObject obj = new JSONObject(result);
                    StorageController sc = new StorageController(MessageService.this);
                    JSONArray user_conv = obj.getJSONArray("response");
                    List<String> list = sc.getSrvid();
                    if(user_conv.length() > 0)
                    {
                        for (int i = 0; i < user_conv.length(); i++) {
                            JSONObject c = user_conv.getJSONObject(i);
                            HashMap<String, String> h = new HashMap<String, String>();
                            h.put("USER_ID",sc.getUser(c.getString("USER_NUM")));
                            h.put("MSG", c.getString("MSG"));
                            h.put("MSG_STAT", c.getString("MSG_STAT"));
                            h.put("SRVID", c.getString("ID"));
                            if(!list.contains(c.getString("ID"))) {
                                sc.insertMsg(h);
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }
    }
}

