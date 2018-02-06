package com.example.mygreenfee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class OrderActivity extends AppCompatActivity {

    OrderRepository orderRepository;
    private Fragment newFragment;
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

        final LinearLayout lay = (LinearLayout) findViewById(R.id.formulaire2);
        lay.setVisibility(View.VISIBLE);

        //Bind des buttons avec les méthodes correspondantes
        final Button buttonValidation = findViewById(R.id.buttonvalidation);
        buttonValidation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleAccueil();
            }
        });

        //Bind des buttons avec les méthodes correspondantes
        final Button buttonpaiement = findViewById(R.id.buttonpaiement);
        buttonValidation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handlePaiement();
            }
        });

        orderRepository = new OrderRepository(this);
        SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);

        final TextView club = (TextView)findViewById(R.id.club);
        club.setText(sharedPref.getString("order_club", "Non défini"));

        final TextView date = (TextView)findViewById(R.id.date);
        date.setText(sharedPref.getString("order_date", "Non défini"));

        final TextView parcours = (TextView)findViewById(R.id.parcours);
        parcours.setText(sharedPref.getString("order_parcours", "Non défini"));

        final TextView nbim = (TextView)findViewById(R.id.nb);
        nbim.setText(sharedPref.getString("order_nombre", "Non défini"));

        final TextView prix = (TextView)findViewById(R.id.prix);
        prix.setText(sharedPref.getString("order_price", "Non défini"));

        String is_order = sharedPref.getString("order_id", "false");
        String email = sharedPref.getString("user_email", "false");
        String password = sharedPref.getString("user_password", "false");

        orderRepository.login(email,password);
    }

    protected void displayMyBanking(){
        newFragment = new MyBankingCardFragment();
        Bundle args = new Bundle();

        // Create fragment and give it an argument specifying the article it should show
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.constraintLayout2, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

        final ProgressBar simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
        simpleProgressBar.setVisibility(View.GONE);
    }

    protected void displayMonCompte(){
        Log.d("DEBUG", "DISPLAY MON COMPTE");

        newFragment = new ConnectMemberFragment();
        Bundle args = new Bundle();

        // Create fragment and give it an argument specifying the article it should show
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.constraintLayout2, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

        final ProgressBar simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
        simpleProgressBar.setVisibility(View.GONE);
    }

    public void handleAccueil(){
        Intent intent = new Intent(this, HomeMapsActivity.class);
        startActivity(intent);
    }

    public void handlePaiement(){

    }

    public void handleSuccessLogin(UserData u){
        this.displayMyBanking();
    }

    public void handleErrorLogin(){
        this.displayMonCompte();
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

    public void handleErrorPayment(String s){
        Toast toast = Toast.makeText(this, s, Toast.LENGTH_LONG);
        toast.show();
    }

    public void handleSuccessPayment(){
        final LinearLayout lay = (LinearLayout) findViewById(R.id.formulaire2);
        lay.setVisibility(View.GONE);

        final TextView tv = (TextView) findViewById(R.id.textView);
        tv.setVisibility(View.VISIBLE);

        final Button buttonback = (Button) findViewById(R.id.buttonvalidation);
        buttonback.setVisibility(View.VISIBLE);
    }
}
