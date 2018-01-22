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

    ReservationData reservationData;
    public ReservationFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static ReservationFragment newInstance(ReservationData r) {

        ReservationFragment f = new ReservationFragment();
        f.reservationData = r;

        return f;
    }

    public void setTexts(){


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Créé la vue et retourne une carte vide
        View view = inflater.inflate(R.layout.fragment_reservation, container, false);
        TextView textView = (TextView) view.findViewById(R.id.titreclub);
        textView.setText(reservationData.club_name);

        textView = (TextView) view.findViewById(R.id.date);
        textView.setText(reservationData.date);

        textView = (TextView) view.findViewById(R.id.date2);
        textView.setText(reservationData.time);

        textView = (TextView) view.findViewById(R.id.date4);
        textView.setText(getResources().getString(R.string.mesResas_name));

        textView = (TextView) view.findViewById(R.id.date3);
        if(reservationData.players>1) {
            textView.setText(reservationData.players+" "+getResources().getString(R.string.mesResas_personne)+"s - "+reservationData.cost+" "+reservationData.currency);
        }
        else{
            textView.setText(reservationData.players+" "+getResources().getString(R.string.mesResas_personne)+" - "+reservationData.cost+" "+reservationData.currency);
        }

        SharedPreferences sharedPref = getActivity().getSharedPreferences("appData", Context.MODE_PRIVATE);
        String fname = sharedPref.getString("user_fname", "false");
        String lname = sharedPref.getString("user_lname", "false");
        textView = (TextView) view.findViewById(R.id.date5);
        textView.setText(fname+" "+lname);

        return view;
    }
}
