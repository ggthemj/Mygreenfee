package com.example.mygreenfee;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class CreateMemberActivity extends AppCompatActivity {
    private CreateMemberRepository createMemberRepository ;

    //Méthode qui traite le bouton validation
    public void handleValidation(){
        EditText editText = (EditText) findViewById(R.id.nom);
        String nom = editText.getText().toString();

        editText = (EditText) findViewById(R.id.prenom);
        String pre = editText.getText().toString();

        editText = (EditText) findViewById(R.id.email);
        String ema = editText.getText().toString();

        Spinner mySpinner=(Spinner) findViewById(R.id.spinnerCivilite);
        String civ = mySpinner.getSelectedItem().toString();

        //createMemberRepository.update(civ, pre, nom, ema);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_member);
        this.createMemberRepository = new CreateMemberRepository(this);

        final Button buttonValidation = findViewById(R.id.buttonvalidation);
        buttonValidation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleValidation();
            }
        });

        //Spinner civilité
        final Spinner spinnerCivilite = (Spinner) findViewById(R.id.spinnerCivilite);
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.civiliteArray)){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                ((TextView) v).setTextSize(16);
                if(position==0) {
                    ((TextView) v).setTextColor(
                            getResources().getColorStateList(R.color.grayColor)
                    );
                }
                else{
                    ((TextView) v).setTextColor(
                            getResources().getColorStateList(R.color.colorPrimary)
                    );
                }

                return v;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    tv.setTextColor(getResources().getColor(R.color.grayColor));
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerCivilite.setAdapter(spinnerArrayAdapter);

        spinnerCivilite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if(position > 0){
                    // Notify the selected item text

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Spinner pays
        final Spinner spinnerPays = (Spinner) findViewById(R.id.spinnerPays);
        final ArrayAdapter<String> spinnerArrayAdapterPays = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.countries_array)){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                ((TextView) v).setTextSize(16);
                if(position==0) {
                    ((TextView) v).setTextColor(
                            getResources().getColorStateList(R.color.grayColor)
                    );
                }
                else{
                    ((TextView) v).setTextColor(
                            getResources().getColorStateList(R.color.colorPrimary)
                    );
                }

                return v;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    tv.setTextColor(getResources().getColor(R.color.grayColor));
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapterPays.setDropDownViewResource(R.layout.spinner_item);
        spinnerPays.setAdapter(spinnerArrayAdapterPays);

        spinnerPays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if(position > 0){
                    // Notify the selected item text

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Spinner régions
        final Spinner spinnerRegions = (Spinner) findViewById(R.id.spinnerRegions);
        final ArrayAdapter<String> spinnerArrayAdapterRegions = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.regionsArray)){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                ((TextView) v).setTextSize(16);
                if(position==0) {
                    ((TextView) v).setTextColor(
                            getResources().getColorStateList(R.color.grayColor)
                    );
                }
                else{
                    ((TextView) v).setTextColor(
                            getResources().getColorStateList(R.color.colorPrimary)
                    );
                }

                return v;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    tv.setTextColor(getResources().getColor(R.color.grayColor));
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapterRegions.setDropDownViewResource(R.layout.spinner_item);
        spinnerRegions.setAdapter(spinnerArrayAdapterRegions);

        spinnerPays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                if(position > 0){
                    // Notify the selected item text

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
