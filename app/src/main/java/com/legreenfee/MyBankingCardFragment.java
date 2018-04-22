package com.legreenfee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MyBankingCardFragment extends Fragment {

    BankingCardRepository bcRepository ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("DEBUG", "Je créée la vue profile");

        // Créé la vue et retourne une carte vide
        final View view = inflater.inflate(R.layout.activity_my_banking_card, container, false);

        if(getContext() instanceof HomeMapsActivity){
            SharedPreferences sharedPref = getContext().getSharedPreferences("appData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("home_state", "4");
            editor.commit();
        }

        bcRepository = new BankingCardRepository(this);
        SharedPreferences sharedPref = getContext().getSharedPreferences("appData", Context.MODE_PRIVATE);
       String lang = sharedPref.getString("language", "EN");

        String user_mail = sharedPref.getString("user_email", "false");
        String user_currency = "EUR";
        bcRepository.checkCard(lang, user_mail, user_currency);

        final ProgressBar simpleProgressBar = (ProgressBar) view.findViewById(R.id.simpleProgressBar);
        simpleProgressBar.setVisibility(View.VISIBLE);

        final Button bv = (Button) view.findViewById(R.id.buttonvalidation);
        bv.setVisibility(View.GONE);

        //Bind des buttons avec les méthodes correspondantes
        final Button buttonValidation = view.findViewById(R.id.buttonvalidation);
        buttonValidation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleValidation();
            }
        });


        return view;
    }

    public void handleSuccess3(){
        final ProgressBar simpleProgressBar = (ProgressBar) getView().findViewById(R.id.simpleProgressBar);
        simpleProgressBar.setVisibility(View.GONE);

        final TextView alias = (TextView) getView().findViewById(R.id.alias);
        alias.setVisibility(View.VISIBLE);

        final TextView datetw = (TextView) getView().findViewById(R.id.date);
        datetw.setVisibility(View.VISIBLE);

        final Button button = (Button) getView().findViewById(R.id.buttonvalidation);
        if(getContext() instanceof HomeMapsActivity) {
            button.setVisibility(View.VISIBLE);
        }

        final ImageView imagev = (ImageView) getView().findViewById(R.id.CB);
        imagev.setVisibility(View.VISIBLE);
    }

    public void handleSuccess(CardData carddata){

        final TextView alias = (TextView) getView().findViewById(R.id.alias);
        alias.setText(carddata.alias);
        alias.setVisibility(View.VISIBLE);

        final TextView datetw = (TextView) getView().findViewById(R.id.date);
        datetw.setText(carddata.expiration_date.substring(0,2)+"/20"+carddata.expiration_date.substring(2,4));
        datetw.setVisibility(View.VISIBLE);

        final ImageView imagev = (ImageView) getView().findViewById(R.id.CB);
        imagev.setVisibility(View.VISIBLE);

        final Button button = (Button) getView().findViewById(R.id.buttonvalidation);
        if(getContext() instanceof HomeMapsActivity) {
            button.setVisibility(View.VISIBLE);
        }
        else{
            OrderActivity ord = (OrderActivity) getContext();
            ord.displayPaymentButton();
        }

        final ProgressBar simpleProgressBar = (ProgressBar) getView().findViewById(R.id.simpleProgressBar);
        simpleProgressBar.setVisibility(View.GONE);
    }

    public void handleError(String s){
        Toast toast = Toast.makeText(getContext(), s, Toast.LENGTH_LONG);
        toast.show();

        if(getContext() instanceof HomeMapsActivity){
            HomeMapsActivity hom = (HomeMapsActivity)getContext();
            hom.displayAddPaymentCard();
        }
        else{
            OrderActivity hom = (OrderActivity) getContext();
            hom.displayAddPaymentCard();
        }
    }

    public void handleValidation(){
        if(getContext() instanceof HomeMapsActivity){
            HomeMapsActivity hom = (HomeMapsActivity)getContext();
            hom.displayAddPaymentCard();
        }
    }
}
