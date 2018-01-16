package com.example.mygreenfee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView myAwesomeTextView = (TextView)findViewById(R.id.title1);
        SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
        String toDisplay = sharedPref.getString("user_fname", "Pas trouvé ")+" "+sharedPref.getString("user_lname", "Pas trouvé ");

        //Mise en place de la custom app bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar2);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Association du bouton de validation à la méthode correspondante
        final ConstraintLayout constraintL1 = findViewById(R.id.element1);
        constraintL1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleMonProfil();
            }
        });

        //Association du bouton de validation à la méthode correspondante
        final ConstraintLayout constraintL2 = findViewById(R.id.element2);
        constraintL2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleMaCarte();
            }
        });

        //Association du bouton de validation à la méthode correspondante
        final ConstraintLayout constraintL3 = findViewById(R.id.element3);
        constraintL3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deconnexion();
            }
        });

        myAwesomeTextView.setText(toDisplay);
    }

    public void handleMonProfil(){
        //Intent intent = new Intent(this, MonCompteActivity.class);
        //startActivity(intent);
    }

    public void handleMaCarte(){
        Intent intent = new Intent(this, MyBankingCardActivity.class);
        startActivity(intent);
    }

    public void deconnexion(){
        SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("is_connected", "false");
        editor.remove("user_id");
        editor.remove("user_title");
        editor.remove("user_fname");
        editor.remove("user_lname");
        editor.remove("user_dob");
        editor.remove("user_email");
        editor.remove("user_country");
        editor.remove("user_region");
        editor.remove("user_phone");
        editor.commit();

        Intent intent = new Intent(this, ConnectMemberActivity.class);
        startActivity(intent);
    }
}
