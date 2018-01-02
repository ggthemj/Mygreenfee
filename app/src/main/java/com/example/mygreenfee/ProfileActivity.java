package com.example.mygreenfee;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView myAwesomeTextView = (TextView)findViewById(R.id.title1);
        SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
        String toDisplay = sharedPref.getString("civilite", "Pas trouvé ")+" "+sharedPref.getString("name", "Pas trouvé ");

        myAwesomeTextView.setText(toDisplay);
    }
}
