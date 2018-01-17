package com.example.mygreenfee;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ReservationFragment extends Fragment {

    public ReservationFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static ReservationFragment newInstance(ReservationData r) {

        ReservationFragment f = new ReservationFragment();
        f.setTexts(r);
        return f;
    }

    public void setTexts(ReservationData r){
        TextView textView = (TextView) getView().findViewById(R.id.titreclub);
        textView.setText(r.club_name);

        textView = (TextView) getView().findViewById(R.id.date);
        textView.setText(r.date);

        textView = (TextView) getView().findViewById(R.id.date2);
        textView.setText(r.time);

        textView = (TextView) getView().findViewById(R.id.date3);
        if(r.players>1) {
            textView.setText(r.players+" "+R.string.mesResas_personne+"s - "+r.cost+" "+r.currency);
        }
        else{
            textView.setText(r.players+" "+R.string.mesResas_personne+" - "+r.cost+" "+r.currency);
        }

        SharedPreferences sharedPref = getActivity().getSharedPreferences("appData", Context.MODE_PRIVATE);
        String fname = sharedPref.getString("user_fname", "false");
        String lname = sharedPref.getString("user_lname", "false");
        textView = (TextView) getView().findViewById(R.id.date5);
        textView.setText(fname+" "+lname);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Créé la vue et retourne une carte vide
        View view = inflater.inflate(R.layout.fragment_reservation, container, false);

        return view;
    }
}
