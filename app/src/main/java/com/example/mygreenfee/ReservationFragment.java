package com.example.mygreenfee;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ReservationFragment extends Fragment {

    public ReservationFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static ReservationFragment newInstance() {

        ReservationFragment f = new ReservationFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Créé la vue et retourne une carte vide
        View view = inflater.inflate(R.layout.fragment_reservations, container, false);

        return view;
    }
}
