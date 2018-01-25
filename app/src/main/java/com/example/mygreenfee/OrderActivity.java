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

public class OrderActivity extends AppCompatActivity {

    OrderRepository orderRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //Mise en place de la custom app bar
        Toolbar myToolbar = findViewById(R.id.my_toolbar2);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        final ProgressBar simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
        simpleProgressBar.setVisibility(View.VISIBLE);

        //Bind des buttons avec les méthodes correspondantes
        final Button buttonValidation = findViewById(R.id.buttonvalidation);
        buttonValidation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleAccueil();
            }
        });

        orderRepository = new OrderRepository(this);
        SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
        String is_order = sharedPref.getString("order_id", "false");
        String email = sharedPref.getString("user_email", "false");
        orderRepository.confirm(is_order, email);
    }

    public void handleAccueil(){
        Intent intent = new Intent(this, HomeMapsActivity.class);
        startActivity(intent);
    }

    public void handleConfirm(){
        final ProgressBar simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
        simpleProgressBar.setVisibility(View.GONE);

        final TextView texte = (TextView) findViewById(R.id.textView);
        texte.setVisibility(View.VISIBLE);

        final Button button = (Button) findViewById(R.id.buttonvalidation);
        button.setVisibility(View.VISIBLE);
    }

    public void handleError(String s){
        Toast toast = Toast.makeText(this, s, Toast.LENGTH_LONG);
        toast.show();
    }
}
