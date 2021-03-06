package org.kelkarkul.kitesmessenger;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences sp = getSharedPreferences("user_conv",MODE_PRIVATE);
        SharedPreferences sp_getter = getSharedPreferences("user_info",MODE_PRIVATE);
        getSupportActionBar().setTitle(sp.getString("USER_NAME",""));
        getSupportActionBar().setElevation(0);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout layout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        //layout.setBackground(new ColorDrawable(Color.parseColor("#008080")));
        layout.setBackground(getResources().getDrawable(R.drawable.default_bg));
        FontsOverride.setDefaultFont(this, "MONOSPACE", "font.ttf");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                FontsOverride.setDefaultFont(ProfileActivity.this, "MONOSPACE", "font.ttf");
            }
        }, 500);
//        TextView user_num = (TextView) findViewById(R.id.user_num);
//        TextView user_name = (TextView) findViewById(R.id.user_name);
//        TextView user_stat = (TextView) findViewById(R.id.user_stat);
        //user_num.setText(sp.getString("USER_NUM",""));
        //user_name.setText(sp_getter.getString("user_name",""));
        //user_stat.setText(sp_getter.getString("user_stat",""));
    }
}
