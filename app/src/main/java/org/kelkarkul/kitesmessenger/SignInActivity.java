package org.kelkarkul.kitesmessenger;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.novoda.merlin.Merlin;
import com.novoda.merlin.MerlinBuilder;
import com.novoda.merlin.MerlinsBeard;
import com.novoda.merlin.registerable.bind.Bindable;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.disconnection.Disconnectable;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class SignInActivity extends AppCompatActivity {
    AutoCompleteTextView actv_email_txt;
    EditText otp_text;
    Button btn_next;
   // Merlin merlin;
    MerlinsBeard merlinsBeard;
    //protected Merlin merlin;
    //MerlinBuilder merlinBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().setElevation(0);
        if(getIntent().getStringExtra("R") != null)
        {
            Intent reboot = new Intent(SignInActivity.this,SignInActivity.class);
            startActivity(reboot);
            finish();
        }

//        merlinBuilder.withConnectableCallbacks().withDisconnectableCallbacks().build(SignInActivity.this);
        final Button btn_exit = (Button) findViewById(R.id.btn_exit);
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setVisibility(View.GONE);
        actv_email_txt = (AutoCompleteTextView) findViewById(R.id.number_txt);
        otp_text = (EditText) findViewById(R.id.otp_txt);
        otp_text.setVisibility(View.GONE);
        btn_exit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(btn_exit.getText().equals("Turn on connection")){
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
                    finish();
                }
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(otp_text.getText().toString().equals("")) {
                    validateField(actv_email_txt.getText().toString());
                }
                else {
                    Handler handler = new Handler();
//                    Handler head = new Handler();
                    final SharedPreferences sp_get = getSharedPreferences("messenger_user_stat", Context.MODE_PRIVATE);
//                    head.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            //Do something after 100ms
//                            if (otp_text.getText().toString().equals(sp_get.getString("otp_num", ""))) {
//                                new ProgressLoader(SignInActivity.this).execute();
//                            }
//                        }
//                    }, 550);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms
                            if (otp_text.getText().toString().equals(sp_get.getString("otp_num", ""))) {
                                SharedPreferences.Editor sp_put = getSharedPreferences("messenger_user_stat", Context.MODE_PRIVATE).edit();
                                sp_put.putString("isUserLoggedIn", "true");
                                sp_put.putString("userNum", actv_email_txt.getText().toString());
                                sp_put.apply();
                                sp_put.commit();
                                //Activity act = new LoginActivity();
                                overridePendingTransition(0,0);
                                Intent n = new Intent(SignInActivity.this, RestoreActivity.class);
                                startActivity(n);
                                finish();
                                overridePendingTransition(0,0);
                            } else {
                                Toast.makeText(SignInActivity.this, "OTP mismatch", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 1350);
                }
            }
        });
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //Do something after 100ms
//                FontsOverride.setDefaultFont(SignInActivity.this, "MONOSPACE", "font.ttf");
//
//            }
//        }, 500);

        FontsOverride.setDefaultFont(this, "MONOSPACE", "font.ttf");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                FontsOverride.setDefaultFont(SignInActivity.this, "MONOSPACE", "font.ttf");
            }
        }, 500);
        merlinsBeard = MerlinsBeard.from(SignInActivity.this);
        final Handler handler_beared = new Handler();
        handler_beared.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (merlinsBeard.isConnected() || merlinsBeard.isConnectedToWifi()) {
                    // Connected, do something!
                    btn_next.setVisibility(View.VISIBLE);
                    btn_exit.setText("Later");
                } else {
                    // Disconnected, do something!
                    btn_next.setVisibility(View.GONE);
                    btn_exit.setText("Turn on connection");
                    //Toast.makeText(SignInActivity.this, "Internet not available", Toast.LENGTH_SHORT).show();
                }
                handler_beared.postDelayed(this,500);
            }
        },500);

//        if (ContextCompat.checkSelfPermission(SignInActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) SignInActivity.this, Manifest.permission.SEND_SMS)) {
//                android.support.v7.app.AlertDialog.Builder alertBuilder = new android.support.v7.app.AlertDialog.Builder(SignInActivity.this);
//                alertBuilder.setCancelable(true);
//                alertBuilder.setTitle("Permission necessary");
//                alertBuilder.setMessage("SMS sending permission is necessary");
//                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//
//                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//                    public void onClick(DialogInterface dialog, int which) {
//                        ActivityCompat.requestPermissions((Activity) SignInActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);
//                    }
//                });
//
//                android.support.v7.app.AlertDialog alert = alertBuilder.create();
//                alert.show();
//            } else {
//                ActivityCompat.requestPermissions((Activity) SignInActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);
            //}
            //return false;}
            //else {
            // return true;
            // }
//        }
    }


    @Override
    protected void onStart() {
        super.onStart();
//        merlin = new Merlin.Builder().withDisconnectableCallbacks().withConnectableCallbacks().build(SignInActivity.this);
//        merlin.bind();
//        merlin.registerConnectable(new Connectable() {
//            @Override
//            public void onConnect() {
//                // Do something you haz internet!
//                btn_next.setVisibility(View.VISIBLE);
//            }
//        });
//        merlin.registerDisconnectable(new Disconnectable() {
//            @Override
//            public void onDisconnect() {
//                // Do something you dont have internet!
//                btn_next.setVisibility(View.GONE);
//            }
//        });
    }



    public void validateField(final String num)
    {
        if(num.length() < 10 || num.length() > 10)
        {
            Toast.makeText(SignInActivity.this, "Number should be 10 digit", Toast.LENGTH_SHORT).show();
        }
        else
        {
            AlertDialog alertDialog = new AlertDialog.Builder(SignInActivity.this).create();
            alertDialog.setTitle("Confirmation");
            alertDialog.setMessage("Are you sure to use " + num + " ?\nOTP will be send to this number for confirmation.");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            int min = 1000;
                            int max = 7000;
                            SharedPreferences.Editor sp_edit = getSharedPreferences("messenger_user_stat", Context.MODE_PRIVATE).edit();
                            Random r = new Random();
                            int i1 = r.nextInt(max - min + 1) + min;
                            sp_edit.putString("otp_num",String.valueOf(i1));
                            sp_edit.apply();
                            sp_edit.commit();
                            //new getOTP(num).execute(String.valueOf(i1));
                            SimpleDateFormat _sdfWatchTime = new SimpleDateFormat("HH");
                            String dt = _sdfWatchTime.format(new Date());
                           // if (Integer.parseInt(dt) < 21 && Integer.parseInt(dt) > 9) {
                                new getOTP(String.valueOf(num)).execute(String.valueOf(i1));
//                            } else {
//                                Toast.makeText(SignInActivity.this, "Sorry , Server(s) are in Maintenance", Toast.LENGTH_SHORT).show();
//                            }
                            dialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

        }
    }

    private class getOTP extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String num ="";

        public getOTP(String number)
        {
            this.num =number;
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            Api controller = new Api(SignInActivity.this);
            HttpPost httppost = new HttpPost(controller.otpApi("otpapi",num,params[0]));
            Log.i("URL",controller.otpApi("otpapi",num,params[0]));
            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("number", params[0]));
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
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            try {
                if (result.equals("Your message is successfully sent to:91" + num)) {
                    otp_text.setVisibility(View.VISIBLE);
                    btn_next.setText("Confirm");
                    Toast.makeText(SignInActivity.this, "You will receive otp soon !", Toast.LENGTH_SHORT).show();
                } else if (result.equals("DND")) {
                    Toast.makeText(SignInActivity.this, "You have activated Do Not Disturb(DND) ! , Please turn it off .", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignInActivity.this, "OTP Error !", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                Toast.makeText(SignInActivity.this, "Something went wrong..", Toast.LENGTH_SHORT).show();
                Toast.makeText(SignInActivity.this, result, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            // txt.setText(result);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }

        @Override
        protected void onPreExecute() {
            progressDialog= new ProgressDialog(SignInActivity.this);
            progressDialog.setMessage("Working..");
            progressDialog.show();}

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onPreExecute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ic_menu_options_splash, menu);
        return true;
    }

    private class ProgressLoader extends AsyncTask<String, Void, Void> {
        Context context;
        ProgressDialog progressDialog;
        public ProgressLoader(Context ctx)
        {
            this.context = ctx;
        }

        @Override
        protected void onPreExecute() {
            progressDialog= new ProgressDialog(context);
            progressDialog.setMessage("Validating OTP..");
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            // Parsse response data
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    try {
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }, 1200);
            //move activity
            super.onPostExecute(result);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //merlin.bind();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SmsReceiver");
        registerReceiver(ReceivefromService, filter);
        //the first parameter is the name of the inner class we created.
    }

    @Override
    protected void onPause() {
        //merlin.unbind();
        super.onPause();
        try {
            unregisterReceiver(ReceivefromService);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Receiver not registered")) {
                Log.i("TAG","Tried to unregister the reciver when it's not registered");
            }
            else
            {
                throw e;
            }
        }
    }

    private BroadcastReceiver ReceivefromService = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent)
        {
            //get the data using the keys you entered at the service
            String IncomingSms=intent.getStringExtra("incomingSms");//
            String phoneNumber=intent.getStringExtra("incomingPhoneNumber");
            final SharedPreferences sp_get = context.getSharedPreferences("messenger_user_stat", Context.MODE_PRIVATE);
            final SharedPreferences.Editor sp_put = context.getSharedPreferences("messenger_user_stat", Context.MODE_PRIVATE).edit();
            if(phoneNumber.contains("KKITES")) {
                otp_text.setText(String.valueOf(IncomingSms.substring(IncomingSms.length() - 4)));
                Handler handler = new Handler();
                Handler head = new Handler();
                head.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        if (otp_text.getText().toString().equals(sp_get.getString("otp_num", ""))) {
                            new ProgressLoader(SignInActivity.this).execute();
                        }
                    }
                }, 550);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        if (otp_text.getText().toString().equals(sp_get.getString("otp_num", ""))) {
                            sp_put.putString("isUserLoggedIn", "true");
                            sp_put.putString("userNum", actv_email_txt.getText().toString());
                            sp_put.apply();
                            sp_put.commit();
                            //Activity act = new LoginActivity();
                            overridePendingTransition(0,0);
                            Intent n = new Intent(SignInActivity.this, RestoreActivity.class);
                            startActivity(n);
                            finish();
                            overridePendingTransition(0,0);
                        } else {
                            Toast.makeText(SignInActivity.this, "OTP mismatch", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 1350);
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_google:
                //Toast.makeText(this, "Google selected", Toast.LENGTH_SHORT).show();
                String url = "https://plus.google.com/118441158261436684772";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            case R.id.action_facebook:
                //Toast.makeText(this, "Facebook selected", Toast.LENGTH_SHORT).show();
                String urlf = "https://www.facebook.com/kelkarkul";
                Intent igf = new Intent(Intent.ACTION_VIEW);
                igf.setData(Uri.parse(urlf));
                startActivity(igf);
                break;
            case R.id.action_twitter:
                // Toast.makeText(this, "Twitter selected", Toast.LENGTH_SHORT).show();
                String urldf = "https://twitter.com/kelkar_kul";
                Intent igdf = new Intent(Intent.ACTION_VIEW);
                igdf.setData(Uri.parse(urldf));
                startActivity(igdf);
                break;
        }
        return true;
    }
}
