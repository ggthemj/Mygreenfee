package com.example.mygreenfee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class HomeMapsActivity extends AppCompatActivity {

    private Fragment newFragment;
    String is_connected;

    protected void displayMaps(){
        Log.d("DEBUG", "J'affiche maps");
        newFragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putInt("1", 1);
        newFragment.setArguments(args);

        // Create fragment and give it an argument specifying the article it should show
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.constraintLayout2, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

        final TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewTitle.setVisibility(View.GONE);

        final ImageButton rb = (ImageButton) findViewById(R.id.recherche_button);
        rb.setVisibility(View.VISIBLE);

        final ImageView tt = (ImageView) findViewById(R.id.maps_title);
        tt.setVisibility(View.VISIBLE);
    }

    protected void displayReservations(){
        newFragment = new ReservationsFragment();
        Bundle args = new Bundle();

        // Create fragment and give it an argument specifying the article it should show
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.constraintLayout2, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

        final TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewTitle.setText("Mes réservations");
        textViewTitle.setVisibility(View.VISIBLE);

        final ImageButton rb = (ImageButton) findViewById(R.id.recherche_button);
        rb.setVisibility(View.GONE);

        final ImageView tt = (ImageView) findViewById(R.id.maps_title);
        tt.setVisibility(View.GONE);
    }

    protected void displayAddPaymentCard(){
        newFragment = new AddBankingCardFragment();
        Bundle args = new Bundle();

        // Create fragment and give it an argument specifying the article it should show
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.constraintLayout2, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

        final TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewTitle.setText(getResources().getString(R.string.addCard_Title));
        textViewTitle.setVisibility(View.VISIBLE);

        final ImageButton rb = (ImageButton) findViewById(R.id.recherche_button);
        rb.setVisibility(View.GONE);

        final ImageView tt = (ImageView) findViewById(R.id.maps_title);
        tt.setVisibility(View.GONE);
    }

    protected void displayOubli(){
        newFragment = new ChangePasswordFragment();
        Bundle args = new Bundle();

        // Create fragment and give it an argument specifying the article it should show
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.constraintLayout2, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

        final TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewTitle.setText(getResources().getString(R.string.changePassword_Title));
        textViewTitle.setVisibility(View.VISIBLE);

        final ImageButton rb = (ImageButton) findViewById(R.id.recherche_button);
        rb.setVisibility(View.GONE);

        final ImageView tt = (ImageView) findViewById(R.id.maps_title);
        tt.setVisibility(View.GONE);
    }

    protected void displayCrea(){
        newFragment = new CreateMemberFragment();
        Log.d("DEBUG", "Je passe à CREA");
        Bundle args = new Bundle();

        // Create fragment and give it an argument specifying the article it should show
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.constraintLayout2, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

        final TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewTitle.setText(getResources().getString(R.string.creationCompte_Title));
        textViewTitle.setVisibility(View.VISIBLE);

        final ImageButton rb = (ImageButton) findViewById(R.id.recherche_button);
        rb.setVisibility(View.GONE);

        final ImageView tt = (ImageView) findViewById(R.id.maps_title);
        tt.setVisibility(View.GONE);
    }

    protected void displayCompte(){
        Log.d("DEBUG", "Je DISPLAYCOMPTE");
        newFragment = new ProfileFragment();
        Bundle args = new Bundle();

        // Create fragment and give it an argument specifying the article it should show
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.constraintLayout2, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

        final TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewTitle.setText(getResources().getString(R.string.profile_Title));
        textViewTitle.setVisibility(View.VISIBLE);

        final ImageButton rb = (ImageButton) findViewById(R.id.recherche_button);
        rb.setVisibility(View.GONE);

        final ImageView tt = (ImageView) findViewById(R.id.maps_title);
        tt.setVisibility(View.GONE);
    }

    protected void displayLogin(){
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

        final TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewTitle.setText(getResources().getString(R.string.connect_Title));
        textViewTitle.setVisibility(View.VISIBLE);

        final ImageButton rb = (ImageButton) findViewById(R.id.recherche_button);
        rb.setVisibility(View.GONE);

        final ImageView tt = (ImageView) findViewById(R.id.maps_title);
        tt.setVisibility(View.GONE);
    }

    protected void displayMonProfil(){
        newFragment = new MonCompteFragment();
        Bundle args = new Bundle();

        // Create fragment and give it an argument specifying the article it should show
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.constraintLayout2, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

        final TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewTitle.setText(getResources().getString(R.string.mesInfos_Title));
        textViewTitle.setVisibility(View.VISIBLE);

        final ImageButton rb = (ImageButton) findViewById(R.id.recherche_button);
        rb.setVisibility(View.GONE);

        final ImageView tt = (ImageView) findViewById(R.id.maps_title);
        tt.setVisibility(View.GONE);
    }

    protected void displayMaCarte(){
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

        final TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewTitle.setText(getResources().getString(R.string.myBanking_Title));
        textViewTitle.setVisibility(View.VISIBLE);

        final ImageButton rb = (ImageButton) findViewById(R.id.recherche_button);
        rb.setVisibility(View.GONE);

        final ImageView tt = (ImageView) findViewById(R.id.maps_title);
        tt.setVisibility(View.GONE);
    }

    public void handleUpdateResa(ReservationData[] open, ReservationData[] closed, boolean isEnCours, boolean hasSucceeded){
        final ProgressBar simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
        simpleProgressBar.setVisibility(View.GONE);

        LinearLayout fragContainer = (LinearLayout) findViewById(R.id.formulaire);
        TextView enCours = findViewById(R.id.compteurResa);

        if(isEnCours){
            if(hasSucceeded){
                for (int i = 0; i < closed.length; i++) {
                    getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag("someTag" + i)).commit();
                    Log.d("DEBUG", "Je remove "+i);
                }
            }
            if(open!=null) {
                for (int i = 0; i < open.length; i++) {
                    getSupportFragmentManager().beginTransaction().add(fragContainer.getId(), ReservationFragment.newInstance(open[i]), "someTag"+i).commit();
                    Log.d("DEBUG", "en cours "+i);
                }
                String texteCompteur;
                if (open.length > 1) {
                    texteCompteur = open.length + " " + getResources().getString(R.string.mesResas_commande) + "s " + getResources().getString(R.string.mesResas_enCours) ;
                } else {
                    texteCompteur = open.length + " " + getResources().getString(R.string.mesResas_commande) + " " + getResources().getString(R.string.mesResas_enCours);
                }
                enCours.setText(texteCompteur);
            }
            else{
                String texteCompteur;
                texteCompteur =  "0 " + getResources().getString(R.string.mesResas_commande) + " " + getResources().getString(R.string.mesResas_enCours);

                enCours.setText(texteCompteur);
            }
        }
        else{
            if(hasSucceeded){
                for (int i = 0; i < open.length; i++) {
                    getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag("someTag" + i)).commit();
                }
            }
            if(closed!=null) {

                for (int i = 0; i < closed.length; i++) {
                    getSupportFragmentManager().beginTransaction().add(fragContainer.getId(), ReservationFragment.newInstance(closed[i]), "someTag"+i).commit();
                    //android.app.Fragment fragment = getFragmentManager().findFragmentByTag("someTag"+i);
                    Log.d("DEBUG", "closed "+i);
                }
                String texteCompteur;
                if (closed.length > 1) {
                    texteCompteur = closed.length + " " + getResources().getString(R.string.mesResas_commande) + "s " + getResources().getString(R.string.mesResas_status) + "s";
                } else {
                    texteCompteur = closed.length + " " + getResources().getString(R.string.mesResas_commande) + " " + getResources().getString(R.string.mesResas_status);
                }
                enCours.setText(texteCompteur);
            }
            else{
                String texteCompteur;
                texteCompteur =  "0 " + getResources().getString(R.string.mesResas_commande) + " " + getResources().getString(R.string.mesResas_enCours);

                enCours.setText(texteCompteur);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("DEBUG", "Je crée home maps");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_maps);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setItemTextColor(bottomNavigationView.getItemTextColor());
        this.chooseMenuItem(1);

        ImageButton imageButton = (ImageButton) findViewById(R.id.recherche_button);

        final TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewTitle.setVisibility(View.GONE);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar2);
        setSupportActionBar(myToolbar);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        this.is_connected = sharedPref.getString("home_state", "0");

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("order_id", "false");
        editor.commit();

        if(is_connected.equals("1")){
            displayMaps();
            bottomNavigationView.setSelectedItemId(R.id.navigation_Golfs);
        }
        else if(is_connected.equals("2")){
            displayReservations();
            bottomNavigationView.setSelectedItemId(R.id.navigation_MesResa);
        }
        else if(is_connected.equals("3")){
            Log.d("DEBUG", "A CREATION COMPTE");
            displayCompte();
            bottomNavigationView.setSelectedItemId(R.id.navigation_MonCompte);
        }

        editor.putString("home_state", "1");
        editor.commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Log.d("DEBUG", "SWITCH "+is_connected);
                    switch (item.getItemId()) {
                        case R.id.navigation_Golfs:

                            if(!is_connected.equals("1")) {

                                SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("home_state", "1");
                                editor.commit();
                                is_connected = "1";

                                displayMaps();
                            }
                            return true;
                        case R.id.navigation_MesResa:
                            if(!is_connected.equals("2")) {
                                SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("home_state", "2");
                                editor.commit();
                                is_connected = "2";
                                displayReservations();
                            }
                            //mTextMessage.setText(R.string.title_dashboard);
                            return true;
                        case R.id.navigation_MonCompte:
                            if(!is_connected.equals("3")) {
                                SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("home_state", "3");
                                editor.commit();
                                is_connected = "4";
                                displayCompte();
                            }
                            return true;
                    }
                    return false;
                }
            }
        );

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClubListActivity.class);
                MapsFragment maps = (MapsFragment)newFragment;
                if (maps.getMyPosition() != null) {
                    intent.putExtra("latitude", maps.getMyPosition().latitude);
                    intent.putExtra("longitude", maps.getMyPosition().longitude);
                }
                startActivity(intent);
            }
        });
    }

    public void chooseMenuItem(int i){
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setItemTextColor(bottomNavigationView.getItemTextColor());


        if(i==1){
            bottomNavigationView.setSelectedItemId(R.id.navigation_Golfs);
        }
        else if(i==2){
            bottomNavigationView.setSelectedItemId(R.id.navigation_MesResa);
        }
        else if(i==3){
            bottomNavigationView.setSelectedItemId(R.id.navigation_MonCompte);
        }


    }

    /**
     * AlertDialog.Builder builder;
     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
     builder = new AlertDialog.Builder(bookingContext, android.R.style.Theme_Material_Dialog_Alert);
     } else {
     builder = new AlertDialog.Builder(bookingContext);
     }
     builder.setTitle("Delete entry")
     .setMessage("Are you sure you want to delete this entry?")
     .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
     public void onClick(DialogInterface dialog, int which) {
     // continue with delete
     }
     })
     .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
     public void onClick(DialogInterface dialog, int which) {
     // do nothing
     }
     })
     .setIcon(android.R.drawable.ic_dialog_alert)
     .show();
     **/
}
