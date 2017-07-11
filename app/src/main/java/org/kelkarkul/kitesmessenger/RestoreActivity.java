package org.kelkarkul.kitesmessenger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

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

public class RestoreActivity extends AppCompatActivity {
    StorageController sc;
    ProgressBar pr_bg;
    Button btn_next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore);
        pr_bg = (ProgressBar) findViewById(R.id.pr_bg);
        FontsOverride.setDefaultFont(this, "MONOSPACE", "font.ttf");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                FontsOverride.setDefaultFont(RestoreActivity.this, "MONOSPACE", "font.ttf");

            }
        }, 500);
        sc = new StorageController(RestoreActivity.this);
        SharedPreferences sp = getSharedPreferences("messenger_user_stat",MODE_PRIVATE);
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent start = new Intent(RestoreActivity.this,IntActivity.class);
                startActivity(start);
                finish();
            }
        });
        new addUser().execute(sp.getString("userNum",""));

    }

    private class addUser extends AsyncTask<String, Void, String> {
//        ProgressDialog progressDialog;


        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            Api controller = new Api(RestoreActivity.this);
            HttpPost httppost = new HttpPost(controller.getApiUrl("user_det"));
            Log.i("URL",controller.getApiUrl("user_det"));
            try {
                // Add your data
                WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
                WifiInfo wInfo = wifiManager.getConnectionInfo();
                //String macAddress = wInfo.getMacAddress();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("number", params[0]));
                nameValuePairs.add(new BasicNameValuePair("user_mac", wInfo.getMacAddress()));
                //nameValuePairs.add(new BasicNameValuePair("stringdata", "Hi"));
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
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            SharedPreferences sp = getSharedPreferences("messenger_user_stat",MODE_PRIVATE);
            new restore().execute(sp.getString("userNum",""));

        }

    }


    public class restore extends AsyncTask<String,String,String>
    {

        @Override
        public synchronized String doInBackground(String... params)
        {
            HttpClient httpclient = new DefaultHttpClient();
            Api controller = new Api(RestoreActivity.this);
            HttpPost httppost = new HttpPost(controller.getApiUrl("user_conv"));
            Log.i("URL",controller.getApiUrl("user_conv"));
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
            if(!result.equals("null"))
            {

                 try {
                    JSONObject obj = new JSONObject(result);
                    JSONArray user_conv = obj.getJSONArray("response");
                    if(user_conv.length() > 0)
                    {
                        for (int i = 0; i < user_conv.length(); i++) {
                            JSONObject c = user_conv.getJSONObject(i);
                            HashMap<String, String> h = new HashMap<String, String>();
                                h.put("USER_ID","");
                                h.put("FULLNAME", c.getString("FULLNAME"));
                                h.put("USER_NUM", c.getString("USER_NUM").trim());
                                sc.insertUser(h);
                        }
                    }
    //                Intent conv = new Intent(RestoreActivity.this,IntActivity.class);
    //                startActivity(conv);
    //                finish();
                     SharedPreferences sp = getSharedPreferences("messenger_user_stat",MODE_PRIVATE);
                     new restoreMsgs().execute(sp.getString("userNum",""));
                }
                catch (Exception e)
                {
                    pr_bg.setVisibility(View.GONE);
                    btn_next.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }

            }
            else
            {
                Intent conv = new Intent(RestoreActivity.this,IntActivity.class);
                startActivity(conv);
                finish();
            }
        }
    }

    public class restoreMsgs extends AsyncTask<String,String,String>
    {

        @Override
        public synchronized String doInBackground(String... params)
        {
            HttpClient httpclient = new DefaultHttpClient();
            Api controller = new Api(RestoreActivity.this);
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
            pr_bg.setVisibility(View.GONE);
            if(!result.equals("null"))
            {

                 try {
                    JSONObject obj = new JSONObject(result);
                    JSONArray user_conv = obj.getJSONArray("response");

                    if(user_conv.length() > 0)
                    {
                        for (int i = 0; i < user_conv.length(); i++) {
                            JSONObject c = user_conv.getJSONObject(i);
                            HashMap<String, String> h = new HashMap<String, String>();
                                h.put("USER_ID",sc.getUser(c.getString("USER_NUM")));
                                h.put("SRVID",c.getString("ID"));
                                h.put("MSG", c.getString("MSG"));
                                h.put("MSG_STAT", c.getString("MSG_STAT"));
                                h.put("OWNER", c.getString("OWNER"));
                                sc.insertMsg(h);
                        }
                    }
                     Intent conv = new Intent(RestoreActivity.this,IntActivity.class);
                     startActivity(conv);
                     finish();
                }
                catch (Exception e)
                {
                    btn_next.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }

            }
            else
            {
                Intent conv = new Intent(RestoreActivity.this,IntActivity.class);
                startActivity(conv);
                finish();
            }
        }
    }
}
