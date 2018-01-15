package com.example.mygreenfee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MyBankingCardActivity extends AppCompatActivity {

    BankingCardRepository bcRepository ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_banking_card);

        //Mise en place de la custom app bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar2);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        bcRepository = new BankingCardRepository(this);
        SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
        String user_mail = sharedPref.getString("user_email", "false");
        String user_currency = "EUR";
        bcRepository.checkCard(user_mail, user_currency);

        final ProgressBar simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
        simpleProgressBar.setVisibility(View.VISIBLE);

        //Bind des buttons avec les méthodes correspondantes
        final Button buttonValidation = findViewById(R.id.buttonvalidation);
        buttonValidation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleValidation();
            }
        });
    }

    public void handleSuccess(CardData carddata){
        final ProgressBar simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
        simpleProgressBar.setVisibility(View.GONE);

        final TextView alias = (TextView) findViewById(R.id.alias);
        alias.setVisibility(View.VISIBLE);
        alias.setText(carddata.alias);

        final TextView datetw = (TextView) findViewById(R.id.date);
        datetw.setVisibility(View.VISIBLE);
        datetw.setText(carddata.expiration_date);

        final Button button = (Button) findViewById(R.id.buttonvalidation);
        button.setVisibility(View.VISIBLE);

        final ImageView imagev = (ImageView) findViewById(R.id.CB);
        imagev.setVisibility(View.VISIBLE);
    }

    public void handleError(String s){
        Toast toast = Toast.makeText(this, s, Toast.LENGTH_LONG);
        toast.show();

        Intent intent = new Intent(this, AddBankingCardActivity.class);
        startActivity(intent);
    }

    public void handleValidation(){
        Intent intent = new Intent(this, AddBankingCardActivity.class);
        startActivity(intent);
    }
}
