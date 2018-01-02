package com.example.mygreenfee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class HomeMapsActivity extends AppCompatActivity {

    protected void displayMaps(){
        MapsFragment newFragment = new MapsFragment();
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
    }

    protected void displayReservations(){

    }

    protected void displayCompte(){
        SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
        String is_connected = sharedPref.getString("is_connected", "false");

        if(is_connected.equals("false")) {
            Intent intent = new Intent(this, ConnectMemberActivity.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_maps);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setItemTextColor(bottomNavigationView.getItemTextColor());

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar2);
        setSupportActionBar(myToolbar);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String is_connected = sharedPref.getString("home_state", "1");
        if(is_connected.equals("1")){
            displayMaps();
            bottomNavigationView.setSelectedItemId(R.id.navigation_Golfs);
        }
        else if(is_connected.equals("2")){
            displayReservations();
            bottomNavigationView.setSelectedItemId(R.id.navigation_MesResa);
        }
        else if(is_connected.equals("3")){
            displayCompte();
            bottomNavigationView.setSelectedItemId(R.id.navigation_MonCompte);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_Golfs:
                            displayMaps();
                            return true;
                        case R.id.navigation_MesResa:
                            displayReservations();
                            //mTextMessage.setText(R.string.title_dashboard);
                            return true;
                        case R.id.navigation_MonCompte:
                            displayCompte();
                            return true;
                    }
                    return false;
                }
            }
        );
    }

    /**
     * AlertDialog.Builder builder;
     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
     builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
     } else {
     builder = new AlertDialog.Builder(context);
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
