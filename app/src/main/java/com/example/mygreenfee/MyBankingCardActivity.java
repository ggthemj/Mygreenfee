package com.example.mygreenfee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

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

        String is_order = sharedPref.getString("order_id", "false");

        if(!is_order.equals("false")){
            buttonValidation.setText(getResources().getString(R.string.myBanking_Tunnel));
        }
    }

    public void handleSuccess2(){
        SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
        String is_order = sharedPref.getString("order_id", "false");
        String mail = sharedPref.getString("user_email", "false");
        bcRepository.confirmorder(is_order, mail);
    }

    public void handleSuccess3(){
        final ProgressBar simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
        simpleProgressBar.setVisibility(View.GONE);

        final TextView alias = (TextView) findViewById(R.id.alias);
        alias.setVisibility(View.VISIBLE);

        final TextView datetw = (TextView) findViewById(R.id.date);
        datetw.setVisibility(View.VISIBLE);

        final Button button = (Button) findViewById(R.id.buttonvalidation);
        button.setVisibility(View.VISIBLE);

        final ImageView imagev = (ImageView) findViewById(R.id.CB);
        imagev.setVisibility(View.VISIBLE);

        SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
        String is_order = sharedPref.getString("order_id", "false");

        if(!is_order.equals("false")){
            final EditText code = findViewById(R.id.code);
            code.setVisibility(View.VISIBLE);
        }
    }

    public void handleSuccess(CardData carddata){

        final TextView alias = (TextView) findViewById(R.id.alias);
        alias.setText(carddata.alias);
        alias.setVisibility(View.VISIBLE);

        final TextView datetw = (TextView) findViewById(R.id.date);
        datetw.setText(carddata.expiration_date.substring(0,2)+"/20"+carddata.expiration_date.substring(2,4));
        datetw.setVisibility(View.VISIBLE);

        final ImageView imagev = (ImageView) findViewById(R.id.CB);
        imagev.setVisibility(View.VISIBLE);

        final Button button = (Button) findViewById(R.id.buttonvalidation);
        button.setVisibility(View.VISIBLE);

        final ProgressBar simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
        simpleProgressBar.setVisibility(View.GONE);

        SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
        String is_order = sharedPref.getString("order_id", "false");
        String mail = sharedPref.getString("user_email", "false");

        /*
        if(!is_order.equals("false")){
            bcRepository.preparepay(is_order, mail);
        }
        else{
            final ProgressBar simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
            simpleProgressBar.setVisibility(View.GONE);

            final Button button = (Button) findViewById(R.id.buttonvalidation);
            button.setVisibility(View.VISIBLE);

            final ImageView imagev = (ImageView) findViewById(R.id.CB);
            imagev.setVisibility(View.VISIBLE);
        }
        */

        if(!is_order.equals("false")){
            final EditText code = findViewById(R.id.code);
            code.setVisibility(View.VISIBLE);
        }
    }

    public void handleSuccessPayment(CardData carddata){
        SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("order_id", "false");

        Intent intent = new Intent(this, ReservationsFragment.class);
        startActivity(intent);
    }

    public void handleError(String s){
        Toast toast = Toast.makeText(this, s, Toast.LENGTH_LONG);
        toast.show();

        Intent intent = new Intent(this, AddBankingCardActivity.class);
        startActivity(intent);
    }

    public void handleErrorPay(String s){
        Toast toast = Toast.makeText(this, s, Toast.LENGTH_LONG);
        toast.show();

        SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("order_id", "false");

        Intent intent = new Intent(this, HomeMapsActivity.class);
        startActivity(intent);
    }

    public void handleValidation(){

        SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
        String is_order = sharedPref.getString("order_id", "false");
        String email = sharedPref.getString("user_email", "false");

        EditText code = findViewById(R.id.code);
        if (is_order.equals("false")) {
            Intent intent = new Intent(this, AddBankingCardActivity.class);
            startActivity(intent);
        } else {
            if(code.getText().length()>2) {
                bcRepository.pay(is_order, email, code.getText().toString());
            }
            else{
                Toast toast = Toast.makeText(this, "Veuillez compléter votre code de sécurité", Toast.LENGTH_LONG);
                toast.show();
            }
        }

    }
}
