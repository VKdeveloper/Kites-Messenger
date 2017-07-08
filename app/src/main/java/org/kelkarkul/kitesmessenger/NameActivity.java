package org.kelkarkul.kitesmessenger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.novoda.merlin.MerlinsBeard;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class NameActivity extends AppCompatActivity {
    MerlinsBeard merlinsBeard;
    Button done;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        final EditText nm = (EditText) findViewById(R.id.edit_nm);
        done = (Button) findViewById(R.id.btn_done);
        pb= (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(View.GONE);
        done.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(done.getText().equals("Turn on connection")){
                    try {
                        Object sbservice = getSystemService("statusbar");
                        Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
                        Method showsb;
                        if (Build.VERSION.SDK_INT >= 17) {
                            showsb = statusbarManager.getMethod("expandNotificationsPanel");
                        } else {
                            showsb = statusbarManager.getMethod("expand");
                        }
                        showsb.invoke(sbservice);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        startActivity(intent);
                    }
                }
                else {
                    pb.setVisibility(View.VISIBLE);
                    SharedPreferences.Editor sp = getSharedPreferences("user_info", MODE_PRIVATE).edit();
                    SharedPreferences sp_getter = getSharedPreferences("messenger_user_stat", MODE_PRIVATE);
                    sp.putString("user_name", nm.getText().toString());
                    sp.apply();
                    sp.commit();
                    new updateInfo().execute(sp_getter.getString("userNum",""),nm.getText().toString());
                }
            }
        });

        merlinsBeard = MerlinsBeard.from(NameActivity.this);
        final Handler handler_beared = new Handler();
        handler_beared.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (merlinsBeard.isConnected() || merlinsBeard.isConnectedToWifi()) {
                    // Connected, do something!
                    done.setVisibility(View.VISIBLE);
                    done.setText("Done");
                } else {
                    // Disconnected, do something!
                    done.setVisibility(View.GONE);
                    done.setText("Turn on connection");
                    //Toast.makeText(SignInActivity.this, "Internet not available", Toast.LENGTH_SHORT).show();
                }
                handler_beared.postDelayed(this,500);
            }
        },500);
    }


    public class updateInfo extends AsyncTask<String,String,String> {

        @Override
        public synchronized String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            Api controller = new Api(NameActivity.this);
            HttpPost httppost = new HttpPost(controller.getApiUrl("user_info"));
            Log.i("URL", controller.getApiUrl("user_info"));
            try {
                //String macAddress = wInfo.getMacAddress();
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("number", params[0]));
                nameValuePairs.add(new BasicNameValuePair("user_name", params[1]));
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

                while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
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
            if(!result.equals("null")) {
                pb.setVisibility(View.GONE);
                Intent conv = new Intent(NameActivity.this, ConversationActivity.class);
                startActivity(conv);
                finish();
            }
        }
    }
}
