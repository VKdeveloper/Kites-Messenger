package org.kelkarkul.kitesmessenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class IntActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_int);
        UserStat us = new UserStat(IntActivity.this);
        if(us.isFirstTimeRunner())
        {
            if(us.isUserLoggedIn()) {
                Intent online_service = new Intent(IntActivity.this,SyncDataService.class);
                startService(online_service);
                Intent sync_service = new Intent(IntActivity.this,MessageService.class);
                startService(sync_service);
                Intent convers = new Intent(IntActivity.this,ConversationActivity.class);
                startActivity(convers);
                finish();
            }
            else
            {
                Intent sign_in = new Intent(IntActivity.this,SignInActivity.class);
                sign_in.putExtra("R","true");
                startActivity(sign_in);
                finish();
            }
        }
        else
        {
            Intent splash = new Intent(IntActivity.this,SplashActivity.class);
            startActivity(splash);
            finish();
        }
    }
}
