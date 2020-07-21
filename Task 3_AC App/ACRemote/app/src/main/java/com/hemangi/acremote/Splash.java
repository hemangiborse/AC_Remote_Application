package com.hemangi.acremote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.hemangi.acremote.helper.Constants;
import com.hemangi.acremote.database.SharedPreferenceManager;

public class Splash extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;

    private SharedPreferenceManager sp;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        sp = new SharedPreferenceManager(this);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                 sp.connectDB();
                 if (sp.getBoolean(Constants.IS_LOGGED_IN)){
                     Splash.this.startActivity(new Intent(Splash.this,MainActivity.class));
                     Splash.this.finish();
                 }else {
                     Splash.this.startActivity(new Intent(Splash.this,ProfileActivity.class));
                     Splash.this.finish();
                 }
                 sp.closeDB();

            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
