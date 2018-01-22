package com.example.mygreenfee;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SpashscreenActivity extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
                String is_connected = sharedPref.getString("has_ended_tutorial", "false");

                if(is_connected.equals("false")) {
                    Intent intent = new Intent(SpashscreenActivity.this, OnboardingActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(SpashscreenActivity.this, HomeMapsActivity.class);
                    startActivity(intent);
                }

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("is_booking", "false");
                editor.commit();

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
