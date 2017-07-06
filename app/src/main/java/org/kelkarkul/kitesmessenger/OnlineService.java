package org.kelkarkul.kitesmessenger;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class OnlineService extends Service {
    private static Timer timer = new Timer();
    public static final String LOGIN_SESSION = "LOGIN_SESSION";

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
            Gson gson = new Gson();
            StorageController sc = new StorageController(OnlineService.this);
            if(sc.getMessages().size() > 0) {
                new msgTask().execute(sp.getString("userNum", ""), gson.toJson(sc.getMessages()));
            }
            if(sc.getConv().size() > 0) {
                new convTask().execute(sp.getString("userNum", ""), gson.toJson(sc.getConv()));
            }
        }
    };


    public class msgTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {


        }
        @Override
        protected String doInBackground(String... params) {

            HttpClient httpClient = new DefaultHttpClient();
            Api api_controller = new Api(OnlineService.this);
            HttpPost httpPost = new HttpPost(api_controller.getApiUrl("sync_messages"));

            BasicNameValuePair numberBasicNameValuePair = new BasicNameValuePair("number", params[0]);
            BasicNameValuePair sysdateBasicNameValuePair = new BasicNameValuePair("json", params[1]);
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
            nameValuePairList.add(numberBasicNameValuePair);
            nameValuePairList.add(sysdateBasicNameValuePair);
            Log.i("MSG_JSON",String.valueOf(params[1]));
                try {

                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);

                        httpPost.setEntity(urlEncodedFormEntity);

                        HttpResponse httpResponse = httpClient.execute(httpPost);

                        InputStream inputStream = httpResponse.getEntity().getContent();

                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                        StringBuilder stringBuilder = new StringBuilder();

                        String bufferedStrChunk = null;

                        while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                            stringBuilder.append(bufferedStrChunk);
                        }

                        return stringBuilder.toString();

                } catch (Exception uee) {

                    uee.printStackTrace();
                }

            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {


        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonResponse = new JSONObject(result);
                JSONArray jsonMainNode = jsonResponse.getJSONArray("response");
                StorageController db = new StorageController(OnlineService.this);
                for(int i =0 ; i <jsonMainNode.length() ; i++)
                {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    if(!jsonChildNode.getString("STATUS").equals("E"))
                    {
                        String sql_id = jsonChildNode.getString("ID");
                        String srv_id = jsonChildNode.getString("SRVID");
                        String status = jsonChildNode.getString("STATUS");
                        db.updateMsg(sql_id,srv_id,status);
                    }
                }
            }
            catch(Exception w){
                w.printStackTrace();
            }
        }
    }

    public class convTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {


        }
        @Override
        protected String doInBackground(String... params) {

            HttpClient httpClient = new DefaultHttpClient();
            Api api_controller = new Api(OnlineService.this);
            HttpPost httpPost = new HttpPost(api_controller.getApiUrl("sync_conv"));

            BasicNameValuePair numberBasicNameValuePair = new BasicNameValuePair("number", params[0]);
            BasicNameValuePair sysdateBasicNameValuePair = new BasicNameValuePair("json", params[1]);
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
            nameValuePairList.add(numberBasicNameValuePair);
            nameValuePairList.add(sysdateBasicNameValuePair);
            Log.i("MSG_JSON",String.valueOf(params[1]));
                try {

                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairList);

                        httpPost.setEntity(urlEncodedFormEntity);

                        HttpResponse httpResponse = httpClient.execute(httpPost);

                        InputStream inputStream = httpResponse.getEntity().getContent();

                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                        StringBuilder stringBuilder = new StringBuilder();

                        String bufferedStrChunk = null;

                        while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                            stringBuilder.append(bufferedStrChunk);
                        }

                        return stringBuilder.toString();

                } catch (Exception uee) {

                    uee.printStackTrace();
                }

            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {


        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonResponse = new JSONObject(result);
                JSONArray jsonMainNode = jsonResponse.getJSONArray("response");
                StorageController db = new StorageController(OnlineService.this);
                for(int i =0 ; i <jsonMainNode.length() ; i++)
                {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    if(!jsonChildNode.getString("STATUS").equals("E"))
                    {
                        String sql_id = jsonChildNode.getString("ID");
                        String srv_id = jsonChildNode.getString("SRVID");
                        String status = jsonChildNode.getString("STATUS");
                        db.updateConv(sql_id,srv_id,status);
                    }
                }
            }
            catch(Exception w){
                w.printStackTrace();
            }
        }
    }
}
