package com.example.mygreenfee;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ReservationsActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    public boolean hasSucceeded;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private boolean isEnCours;
    private ReservationsRepository reservationsRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations);
        this.hasSucceeded = false;
        isEnCours = true;

        //Mise en place de la custom app bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar2);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
        String user_id = ""+sharedPref.getInt("user_id", 1);

        reservationsRepository = new ReservationsRepository(this);
        reservationsRepository.getReservations(user_id);

        final ProgressBar simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
        simpleProgressBar.setVisibility(View.VISIBLE);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabby);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(hasSucceeded) {
                    Log.d("DEBUG", "Succès "+tab.getPosition());
                    if (tab.getPosition()==0) {
                        isEnCours = true;
                        handleSuccess(reservationsRepository.enCours, reservationsRepository.closedResas);
                    }
                    else {
                        isEnCours = false;
                        handleSuccess(reservationsRepository.enCours, reservationsRepository.closedResas);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void handleError(String s){
        final ProgressBar simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
        simpleProgressBar.setVisibility(View.GONE);

        TextView enCours = findViewById(R.id.compteurResa);
        if(isEnCours) {
            enCours.setText("0 " + getResources().getString(R.string.mesResas_commande) + " " + getResources().getString(R.string.mesResas_enCours));
        }
        else{
            enCours.setText("0 " + getResources().getString(R.string.mesResas_commande) + " " + getResources().getString(R.string.mesResas_status));
        }
        Toast toast = Toast.makeText(this, s, Toast.LENGTH_LONG);
        toast.show();
    }

    public void handleSuccess(ReservationData[] open, ReservationData[] closed){

        Log.d("DEBUG", "SUCCESS "+isEnCours);
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
        this.hasSucceeded = true;

    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_reservations, container, false);
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).


            return PlaceholderFragment.newInstance(position + 1);


        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }
}
