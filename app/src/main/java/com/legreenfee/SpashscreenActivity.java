package com.legreenfee;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;

public class SpashscreenActivity extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_spashscreen);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                String isoL = Locale.getDefault().getISO3Language();
                String language ;
                if(isoL.equals("deu")){
                    language = "DE";
                }
                else if(isoL.equals("fra")){
                    language = "FR";
                }
                else {
                    language = "EN";
                }

                Log.d("DEBUG", "LANGUAGE "+language);

                SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
                String is_connected = sharedPref.getString("has_ended_tutorial", "false");
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("home_state", "1");
                editor.putString("language", language);

                editor.commit();

                Log.d("DEBUG", "home_state "+sharedPref.getString("home_state", "beuh"));
                // close this activity

                if(is_connected.equals("false")) {
                    Intent intent = new Intent(SpashscreenActivity.this, OnboardingActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(SpashscreenActivity.this, HomeMapsActivity.class);
                    startActivity(intent);
                }

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
