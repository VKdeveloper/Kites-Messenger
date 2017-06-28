package org.kelkarkul.kitesmessenger;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SplashActivity extends AppCompatActivity {
    private static final int CLICK_ON_WEBVIEW = 1;
    private static final int CLICK_ON_URL = 2;

    // private final Handler handler = new Handler(this);

    private WebView webView;
    private WebViewClient client;
    //String my_app_url = "http://www.kelkarkulvrutant.com/app/app_login.php";
    ProgressDialog prDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        webView = (WebView)findViewById(R.id.splash_activity_webview);
        //webView.setOnTouchListener(this);
        getSupportActionBar().setElevation(0);

        client = new WebViewClient(){
            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //handler.sendEmptyMessage(CLICK_ON_URL);
                return false;
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                prDialog = ProgressDialog.show(SplashActivity.this, "Just a minute", "Setting up things .. this may take time .");
//                if (url.equals(my_app_url)) {
//                    Intent intent = new Intent(SplashActivity.this, NumberActivity.class);
//                    intent.setData(Uri.parse(url));
//                    startActivity(intent);
//                    finish();
//                }
                super.onPageStarted(view, url, favicon);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                if(prDialog.isShowing()){
                    prDialog.dismiss();
                }
            }
        };

        webView.setWebViewClient(client);
        webView.setVerticalScrollBarEnabled(false);
        webView.loadUrl("file:///android_asset/index.html");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setSaveFormData(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            webView.getSettings().setAllowContentAccess(true);
        }
        //Enable Javascript
        webView.getSettings().setJavaScriptEnabled(true);
        //Inject WebAppInterface methods into Web page by having Interface 'Android'
        webView.addJavascriptInterface(new WebAppInterface(this), "messenger");
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setAllowFileAccess(true);
        if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) SplashActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(SplashActivity.this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("Permission necessary");
                alertBuilder.setMessage("External storage permission is necessary");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) SplashActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }
                });

                AlertDialog alert = alertBuilder.create();
                alert.show();
            } else {
                ActivityCompat.requestPermissions((Activity) SplashActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
            //return false;}
            //else {
            // return true;
            // }
        }
        if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) SplashActivity.this, Manifest.permission.READ_SMS)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(SplashActivity.this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("Permission necessary");
                alertBuilder.setMessage("SMS permission is necessary");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) SplashActivity.this, new String[]{Manifest.permission.READ_SMS}, 1);
                    }
                });

                AlertDialog alert = alertBuilder.create();
                alert.show();
            } else {
                ActivityCompat.requestPermissions((Activity) SplashActivity.this, new String[]{Manifest.permission.READ_SMS}, 1);
            }
            //return false;}
            //else {
            // return true;
            // }
        }
        if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) SplashActivity.this, Manifest.permission.SEND_SMS)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(SplashActivity.this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("Permission necessary");
                alertBuilder.setMessage("SMS sending permission is necessary");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) SplashActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);
                    }
                });

                AlertDialog alert = alertBuilder.create();
                alert.show();
            } else {
                ActivityCompat.requestPermissions((Activity) SplashActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);
            }
            //return false;}
            //else {
            // return true;
            // }
        }
    }

    //    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        if (v.getId() == R.id.splash_activity_webview && event.getAction() == MotionEvent.ACTION_DOWN){
//            handler.sendEmptyMessageDelayed(CLICK_ON_WEBVIEW, 500);
//        }
//        return false;
//    }
//
//    @Override
//    public boolean handleMessage(Message msg) {
//        if (msg.what == CLICK_ON_URL){
//            handler.removeMessages(CLICK_ON_WEBVIEW);
//            return true;
//        }
//        if (msg.what == CLICK_ON_WEBVIEW){
//            Toast.makeText(this, "WebView clicked", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//        return false;
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ic_menu_options_splash, menu);
        return true;
    }

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
    //Class to be injected in Web page
    public class WebAppInterface {
        Context mContext;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void moveToNextScreen(){
            try {
                SharedPreferences.Editor sp = getSharedPreferences("messenger_user_stat", Context.MODE_PRIVATE).edit();
                sp.putString("isFirstTimeRunner","true");
                sp.apply();
                sp.commit();
            }
            catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(SplashActivity.this, "Auth failed", Toast.LENGTH_SHORT).show();
            }
            overridePendingTransition(0,0);
            Intent j = new Intent(SplashActivity.this,IntActivity.class);
            startActivity(j);
            overridePendingTransition(0,0);
            finish();
        }
    }
}
