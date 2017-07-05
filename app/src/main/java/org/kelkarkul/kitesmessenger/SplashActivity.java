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
                super.onPageStarted(view, url, favicon);
                prDialog = ProgressDialog.show(SplashActivity.this, "Just a minute", "Setting up things .. this may take time .");
//                if (url.equals(my_app_url)) {
//                    Intent intent = new Intent(SplashActivity.this, NumberActivity.class);
//                    intent.setData(Uri.parse(url));
//                    startActivity(intent);
//                    finish();
//                }
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                try {
                    if (prDialog.isShowing()) {
                        prDialog.dismiss();
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
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
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        for (String PERMISSION : PERMISSIONS) {
            if (!hasPermissions(this, PERMISSION)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            }
        }
    }


    public boolean hasPermissions(Context ctx,String per) {
        return ContextCompat.checkSelfPermission(ctx, per) == PackageManager.PERMISSION_GRANTED;
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
            if(prDialog.isShowing())
            {
                prDialog.dismiss();
            }
            overridePendingTransition(0,0);
            Intent j = new Intent(SplashActivity.this,IntActivity.class);
            startActivity(j);
            finish();
            overridePendingTransition(0,0);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if ((prDialog != null) && prDialog.isShowing()) {
            prDialog.dismiss();
            prDialog = null;
        }
    }
}
