package com.legreenfee;

import android.app.DatePickerDialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class AddBankingCardFragment extends Fragment {

    AddBankingCardRepository addBankingCardRepository ;
    private Calendar dobCalendar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("DEBUG", "Je créée la vue profile");

        // Créé la vue et retourne une carte vide
        final View view = inflater.inflate(R.layout.activity_add_banking_card, container, false);

        if(getContext() instanceof HomeMapsActivity){
            SharedPreferences sharedPref = getContext().getSharedPreferences("appData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("home_state", "4");
            editor.commit();
        }

        addBankingCardRepository = new AddBankingCardRepository(this);

        //Bind des buttons avec les méthodes correspondantes
        final Button buttonValidation = view.findViewById(R.id.buttonvalidation);
        buttonValidation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleValidation();
            }
        });

        this.dobCalendar = Calendar.getInstance();

        final EditText edittext = view.findViewById(R.id.dateexpiration);

        final MonthYearPickerDialog pd = new MonthYearPickerDialog();
        pd.setListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                pd.setValues(i1,i);
                if(i1>9) {
                    edittext.setText(""+i1+(i-2000));
                }
                else{
                    edittext.setText("0"+i1+(i-2000));
                }
            }
        });

        //Listener qui déclenche l'apparition du date picker
        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show(getFragmentManager(), "MonthYearPickerDialog");
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void handleSuccess(CardData carddata){
        if(getContext() instanceof HomeMapsActivity){
            HomeMapsActivity hom = (HomeMapsActivity)getContext();
            hom.displayMaCarte();
        }
        else{
            OrderActivity hom = (OrderActivity) getContext();
            hom.displayMyBanking();
        }
    }

    public void handleError(String s){
        Toast toast = Toast.makeText(getContext(), s, Toast.LENGTH_LONG);
        toast.show();
    }

    public void handleValidation(){
        SharedPreferences sharedPref = getContext().getSharedPreferences("appData", Context.MODE_PRIVATE);
        String lang = sharedPref.getString("language", "EN");
        String user_mail = sharedPref.getString("user_email", "false");
        String user_currency = "EUR";

        EditText editText = getView().findViewById(R.id.numerocarte);
        String num = editText.getText().toString();

        editText = getView().findViewById(R.id.dateexpiration);
        String dat = editText.getText().toString();

        editText = getView().findViewById(R.id.code);
        String cod = editText.getText().toString();

        addBankingCardRepository.addCard(lang, user_mail, dat, num, cod, user_currency);
    }
}
