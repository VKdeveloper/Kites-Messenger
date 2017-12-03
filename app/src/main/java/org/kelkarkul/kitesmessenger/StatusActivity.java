package org.kelkarkul.kitesmessenger;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
        ImageView iv = (ImageView) findViewById(R.id.stat_edit);
        SharedPreferences sp = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        final SharedPreferences.Editor sp_edit = getSharedPreferences("user_info", Context.MODE_PRIVATE).edit();
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog prDialog;
                prDialog = new Dialog(StatusActivity.this);
                prDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                prDialog.setCancelable(false);
                prDialog.setContentView(R.layout.dialog_stat_edit);
                //prDialog.setTitle("Privacy Policy");
                EditText stat_edit = (EditText) prDialog.findViewById(R.id.stat_edit_txt);
                //TextView cnt = (TextView) prDialog.findViewById(R.id.txt_lmt);
                LinearLayout ll = (LinearLayout) prDialog.findViewById(R.id.ll_stat);
                Button sv = (Button) prDialog.findViewById(R.id.btn_sv);
                Button cal = (Button) prDialog.findViewById(R.id.btn_cnl);
                sv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText edit_txt = (EditText) prDialog.findViewById(R.id.stat_edit_txt);
                        sp_edit.putString("user_stat", edit_txt.getText().toString());
                        sp_edit.apply();
                        sp_edit.commit();
                        //Toast.makeText(StatusActivity.this, "Vehicle saved !", Toast.LENGTH_LONG).show();
                        prDialog.dismiss();
                    }
                });
                cal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(ParkActivity.this, "Vehicle was not saved", Toast.LENGTH_SHORT).show();
                        prDialog.dismiss();
                    }
                });
                //ll.setMinimumHeight(getWindowManager().getDefaultDisplay().getHeight()/2);
                ll.setMinimumWidth(getWindowManager().getDefaultDisplay().getWidth() / 2);
                prDialog.show();
            }
        });
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
                Intent i = new Intent(StatusActivity.this, StatusActivity.class);
                startActivity(i);
                Toast.makeText(StatusActivity.this, "Recent status history cleared.", Toast.LENGTH_SHORT).show();
                break;
            default:
                return true;
        }
        return true;
    }
}
