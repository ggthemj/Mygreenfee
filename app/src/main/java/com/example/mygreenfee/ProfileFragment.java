package com.example.mygreenfee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProfileFragment extends Fragment {

    ProfileRepository profileRepository ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void handleSuccess(UserData userdata){
        TextView myAwesomeTextView = (TextView)getView().findViewById(R.id.title1);
        String toDisplay = userdata.fname+" "+userdata.lname;
        myAwesomeTextView.setText(toDisplay);

        final LinearLayout lay = (LinearLayout)getView().findViewById(R.id.simpleProgressBar);
        lay.setVisibility(View.VISIBLE);

        final ProgressBar simpleProgressBar = (ProgressBar) getView().findViewById(R.id.simpleProgressBar);
        simpleProgressBar.setVisibility(View.GONE);
    }

    public void handleError(String s){
        //todo
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("DEBUG", "Je créée la vue profile");

        // Créé la vue et retourne une carte vide
        View view = inflater.inflate(R.layout.activity_profile, container, false);
        SharedPreferences sharedPref = getContext().getSharedPreferences("appData", Context.MODE_PRIVATE);
        String mail = sharedPref.getString("user_email", "false");

        final ConstraintLayout constraintL1 = view.findViewById(R.id.element1);
        constraintL1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleMonProfil();
            }
        });

        final ConstraintLayout constraintL2 = view.findViewById(R.id.element2);
        constraintL2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleMaCarte();
            }
        });

        final ConstraintLayout constraintL3 = view.findViewById(R.id.element3);
        constraintL3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deconnexion();
            }
        });

        if(mail.equals("false")) {
            Log.d("DEBUG", "MAIL NON CONFIGURE");

            HomeMapsActivity hom = (HomeMapsActivity)getContext();
            hom.displayLogin();
        }
        else{
            Log.d("DEBUG", "MAIL CONFIGURE");
            final ProgressBar simpleProgressBar = view.findViewById(R.id.simpleProgressBar);
            simpleProgressBar.setVisibility(View.VISIBLE);
            String password = sharedPref.getString("user_password", "false");

            profileRepository = new ProfileRepository(this);
            profileRepository.update(mail, password);
        }

        return view;
    }

    public void handleMonProfil(){
        HomeMapsActivity hom = (HomeMapsActivity)getContext();
        hom.displayMonProfil();
    }

    public void handleMaCarte(){
        HomeMapsActivity hom = (HomeMapsActivity)getContext();
        hom.displayMaCarte();
    }

    public void deconnexion(){
        SharedPreferences sharedPref = getContext().getSharedPreferences("appData", Context.MODE_PRIVATE);
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
        editor.remove("user_password");
        editor.commit();

        HomeMapsActivity hom = (HomeMapsActivity)getContext();
        hom.displayLogin();
    }
}
