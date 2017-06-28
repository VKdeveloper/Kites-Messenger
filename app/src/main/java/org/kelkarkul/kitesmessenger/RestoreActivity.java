package org.kelkarkul.kitesmessenger;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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

public class RestoreActivity extends AppCompatActivity {
    StorageController sc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore);
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
        new getConv().execute(sp.getString("userNum",""));

    }

    private class getConv extends AsyncTask<String, Void, String> {
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
            try {
                JSONObject obj = new JSONObject(result);
                //HTTPControllers controllers = new HTTPControllers(MainActivity.this);
                JSONArray user_conv = obj.getJSONArray("user_det");
                if(user_conv.length() > 0)
                {
                    for (int i = 0; i < user_conv.length(); i++) {
                        JSONObject c = user_conv.getJSONObject(i);
                        HashMap<String, String> h = new HashMap<String, String>();
                        if(c.getString("STATUS").equals("Y")) {
                            h.put("USER_ID", c.getString("ID"));
                            h.put("FNAME", c.getString("FNAME"));
                            h.put("LNAME", c.getString("LNAME"));
                            h.put("FULLNAME", c.getString("FULLNAME"));
                            h.put("USER_STATUS", c.getString("USER_STATUS"));
                            h.put("DP_URL", c.getString("DP_URL"));
                            h.put("USER_NUM", c.getString("USER_NUM"));
                            sc.insertUser(h);
                        }
                        else {
                            Toast.makeText(RestoreActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                Intent conv = new Intent(RestoreActivity.this,ConversationActivity.class);
                startActivity(conv);
                finish();
            }
            catch (Exception e)
            {
                Toast.makeText(RestoreActivity.this, "Connection problem.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
//            progressDialog= new ProgressDialog(SignInActivity.this);
//            progressDialog.setMessage("Working..");
//            progressDialog.show();

        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onPreExecute();
        }
    }
}
