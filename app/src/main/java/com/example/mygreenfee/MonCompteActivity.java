package com.example.mygreenfee;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MonCompteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_compte);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_MonCompte);
        bottomNavigationView.setItemTextColor(bottomNavigationView.getItemTextColor());

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_Golfs:
                                changeFragment();
                                return true;
                            case R.id.navigation_MesResa:
                                //mTextMessage.setText(R.string.title_dashboard);
                                return true;
                            case R.id.navigation_MonCompte:
                                return true;
                        }
                        return false;
                    }
                });
    }

    protected void changeFragment(){
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
}
