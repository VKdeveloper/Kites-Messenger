package org.kelkarkul.kitesmessenger;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class StatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        FontsOverride.setDefaultFont(this, "MONOSPACE", "font.ttf");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                FontsOverride.setDefaultFont(StatusActivity.this, "MONOSPACE", "font.ttf");

            }
        }, 500);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ic_menu_options_status, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_remove:
                //Toast.makeText(this, "Google selected", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(StatusActivity.this,StatusActivity.class);
                startActivity(i);
                Toast.makeText(StatusActivity.this, "Recent status history cleared.", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
