package com.example.mygreenfee;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateMemberActivity extends AppCompatActivity {
    private CreateMemberRepository createMemberRepository ;
    private Calendar dobCalendar;
    private boolean is_phone_ok;
    private boolean is_fname_ok;
    private boolean is_lname_ok;
    private boolean is_mail_ok;
    private boolean is_mdp_ok;
    private boolean is_mdpc_ok;
    private boolean is_bday_ok;
    private RegionsData regionsData;
    private ArrayAdapter<String> spinnerArrayAdapterRegions;

    //Méthode qui traite le bouton validation
    public void handleValidation(){
        updateFnameStatus();
        updateLnameStatus();
        updatePhoneStatus();
        updateMailStatus();
        updateMdpStatus();
        updateMdpcStatus();
        updateBdayStatus();

        if(is_mail_ok && is_phone_ok && is_lname_ok && is_fname_ok && is_mdp_ok && is_mdpc_ok && is_bday_ok) {
            //Récupération des données du formulaires et passation au Repo
            String civ = "";
            Spinner mySpinner = findViewById(R.id.spinnerCivilite);
            if ((int) mySpinner.getSelectedItemId() != 0) {
                civ = mySpinner.getSelectedItem().toString();
            }

            EditText editText = findViewById(R.id.nom);
            String nom = editText.getText().toString();

            editText = findViewById(R.id.prenom);
            String pre = editText.getText().toString();

            editText = findViewById(R.id.phone);
            String pho = editText.getText().toString();

            editText = findViewById(R.id.email);
            String ema = editText.getText().toString();

            editText = findViewById(R.id.password);
            String pwd = editText.getText().toString();

            editText = findViewById(R.id.bithday);
            String dob = editText.getText().toString();

            mySpinner = findViewById(R.id.spinnerPays);
            String pay = mySpinner.getSelectedItem().toString();
            switch (pay) {
                case "Afrique du Sud":
                    pay = "ZA";
                    break;
                case "Allemagne":
                    pay = "DE";
                    break;
                case "Autriche":
                    pay = "AT";
                    break;
                case "France":
                    pay = "FR";
                    break;
                case "Suisse":
                    pay = "CH";
                    break;
                case "Belgique":
                    pay = "BE";
                    break;
            }

            mySpinner = findViewById(R.id.spinnerRegions);
            int rid = (int) mySpinner.getSelectedItemId();
            String region_id;
            if(rid==0) {
                region_id = "";
            }
            else{
                region_id = this.regionsData.regionsData[rid - 1].public_id;
            }

            SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString("user_password", pwd);
            editor.commit();
            createMemberRepository.update(civ, nom, pre, ema, dob, pwd, pay, region_id, pho);
        }
        else{
            Toast toast = Toast.makeText(this, R.string.creationCompte_ErreurValidation, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_member);
        this.createMemberRepository = new CreateMemberRepository(this);

        //Association du bouton de validation à la méthode correspondante
        final Button buttonValidation = findViewById(R.id.buttonvalidation);
        buttonValidation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleValidation();
            }
        });

        //Mise en place de la custom app bar
        Toolbar myToolbar = findViewById(R.id.my_toolbar2);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
        String is_order = sharedPref.getString("order_id", "false");

        if(!is_order.equals("false")){
            buttonValidation.setText(getResources().getString(R.string.creationCompte_Tunnel));
        }

        //initialisation des textes d'erreur
        TextView prenomError = findViewById(R.id.error_prenom);
        prenomError.setText("");
        TextView nomError = findViewById(R.id.error_nom);
        nomError.setText("");
        TextView phoneError = findViewById(R.id.error_tel);
        phoneError.setText("");
        TextView mailError = findViewById(R.id.error_email);
        mailError.setText("");
        TextView mdpError = findViewById(R.id.error_mdp);
        mdpError.setText("");
        TextView mdpcError = findViewById(R.id.error_mdpc);
        mdpcError.setText("");
        TextView dobError = findViewById(R.id.error_dob);
        dobError.setText("");

        //Code qui permet de gérer le contrôle de surface sur le prénom
        EditText prenom = findViewById(R.id.prenom);
        prenom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    updateFnameStatus();
                }
                else{
                    TextView loginError = findViewById(R.id.error_prenom);
                    loginError.setText("");
                }
            }
        });

        //Code qui permet de gérer le contrôle de surface sur le nom
        EditText nom = findViewById(R.id.nom);
        nom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    updateLnameStatus();
                }
                else{
                    TextView loginError = findViewById(R.id.error_nom);
                    loginError.setText("");
                }
            }
        });

        //Code qui permet de gérer le contrôle de surface sur le nom
        EditText mail = findViewById(R.id.email);
        mail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    updateLnameStatus();
                }
                else{
                    TextView loginError = findViewById(R.id.error_email);
                    loginError.setText("");
                }
            }
        });

        //Code qui permet de gérer le contrôle de surface sur le nom
        EditText phone = findViewById(R.id.phone);
        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    updateLnameStatus();
                }
                else{
                    TextView loginError = findViewById(R.id.error_tel);
                    loginError.setText("");
                }
            }
        });

        //Code qui permet de gérer le contrôle de surface sur le mot de passe
        EditText mdp = findViewById(R.id.password);
        mdp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    updateLnameStatus();
                }
                else{
                    TextView loginError = findViewById(R.id.error_mdp);
                    loginError.setText("");
                }
            }
        });

        //Code qui permet de gérer le contrôle de surface sur le mot de passe de confirmation
        EditText mdpc = findViewById(R.id.passwordconfirmation);
        mdpc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    updateLnameStatus();
                }
                else{
                    TextView loginError = findViewById(R.id.error_mdpc);
                    loginError.setText("");
                }
            }
        });

        //Spinner civilité
        final Spinner spinnerCivilite = findViewById(R.id.spinnerCivilite);
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.civiliteArray)){
            @Override
            public boolean isEnabled(int position){
                return position != 0;
            }
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
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
                                        @NonNull ViewGroup parent) {
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

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Spinner pays
        final Spinner spinnerPays = findViewById(R.id.spinnerPays);
        final ArrayAdapter<String> spinnerArrayAdapterPays = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.countries_array)){
            @Override
            public boolean isEnabled(int position){
                return position != 0;
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
                    String paysISO = "XX" ;
                    if(position==1) {
                        paysISO = "ZA" ;
                    }
                    else if(position==2) {
                        paysISO = "DE" ;
                    }
                    else if(position==3) {
                        paysISO = "AT" ;
                    }
                    else if(position==4) {
                        paysISO = "FR" ;
                    }
                    else if(position==5) {
                        paysISO = "CH" ;
                    }
                    createMemberRepository.updateRegions(paysISO);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Spinner régions
        final Spinner spinnerRegions = findViewById(R.id.spinnerRegions);
        this.spinnerArrayAdapterRegions = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.regionsArray)){
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
        spinnerRegions.setFocusable(false);
        spinnerRegions.setFocusableInTouchMode(false);

        spinnerRegions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        this.dobCalendar = Calendar.getInstance();

        final EditText edittext = findViewById(R.id.bithday);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                dobCalendar.set(Calendar.YEAR, year);
                dobCalendar.set(Calendar.MONTH, monthOfYear);
                dobCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        //Listerner qui déclenche l'apparition du date picker
        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CreateMemberActivity.this, date, dobCalendar
                        .get(Calendar.YEAR), dobCalendar.get(Calendar.MONTH),
                        dobCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    //Update le champ date de naissance en le mettant au format attendu par le WS
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        EditText edittext = findViewById(R.id.bithday);
        edittext.setText(sdf.format(dobCalendar.getTime()));
    }

    //Met à jour le statut du champ prénom, et affiche éventuellement les erreurs s'il y en a
    public void updateFnameStatus(){
        EditText editText = findViewById(R.id.prenom);
        String prenom = editText.getText().toString();
        is_fname_ok = (prenom.length()>0);
        if(!is_fname_ok){
            TextView loginError = findViewById(R.id.error_prenom);
            loginError.setText(R.string.creationCompte_ErreurPrenom);
        }
    }

    //Met à jour le statut du champ prénom, et affiche éventuellement les erreurs s'il y en a
    public void updateBdayStatus(){
        EditText editText = findViewById(R.id.bithday);
        String bday = editText.getText().toString();
        is_bday_ok = (bday.length()>0);
        if(!is_bday_ok){
            TextView loginError = findViewById(R.id.error_dob);
            loginError.setText(R.string.creationCompte_ErreurBday);
        }
    }

    //Met à jour le statut du champ nom, et affiche éventuellement les erreurs s'il y en a
    public void updateLnameStatus(){
        EditText editText = findViewById(R.id.nom);
        String nom = editText.getText().toString();
        is_lname_ok = (nom.length()>0);
        if(!is_lname_ok){
            TextView loginError = findViewById(R.id.error_nom);
            loginError.setText(R.string.creationCompte_ErreurNom);
        }
    }

    //Met à jour le statut du champ phone, et affiche éventuellement les erreurs s'il y en a
    public void updatePhoneStatus(){
        EditText editText = findViewById(R.id.phone);
        String phone = editText.getText().toString();
        is_phone_ok = (phone.length() > 0);
        if(!is_phone_ok){
            TextView loginError = findViewById(R.id.error_tel);
            loginError.setText(R.string.creationCompte_ErreurPhone);
        }
    }

    //Met à jour le statut du champ mail, et affiche éventuellement les erreurs s'il y en a
    public void updateMailStatus(){
        EditText editText = findViewById(R.id.email);
        String mail = editText.getText().toString();
        is_mail_ok = (mail.length()>0);
        if(!is_mail_ok){
            TextView loginError = findViewById(R.id.error_email);
            loginError.setText(R.string.creationCompte_ErreurMail);
        }
    }

    public void updateMdpStatus(){
        EditText editText = findViewById(R.id.password);
        String mdp = editText.getText().toString();
        is_mdp_ok = (mdp.length() > 0);
        if(!is_mdp_ok){
            TextView loginError = findViewById(R.id.error_mdp);
            loginError.setText(R.string.creationCompte_ErreurMdp);
        }
    }

    public void updateMdpcStatus(){
        EditText editText = findViewById(R.id.password);
        String mdp = editText.getText().toString();
        editText = (EditText) findViewById(R.id.passwordconfirmation);
        String mdpc = editText.getText().toString();
        is_mdpc_ok = (mdpc.equals(mdp));
        if(!is_mdpc_ok){
            TextView loginError = findViewById(R.id.error_mdpc);
            loginError.setText(R.string.creationCompte_ErreurMdpc);
        }
    }

    public void handleSuccess(UserData u){
        Toast toast = Toast.makeText(this, R.string.creationCompte_InscriptionReussie, Toast.LENGTH_LONG);
        toast.show();

        //Remplit le fichier offline avec les informations de l'utilisateur - a optimiser pour stockage in app
        SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("is_connected", "true");
        editor.putInt("user_id", u.public_id);
        editor.putString("user_title", u.title);
        editor.putString("user_fname", u.fname);
        editor.putString("user_lname", u.lname);
        editor.putString("user_dob", u.dob);
        editor.putString("user_email", u.email);
        editor.putString("user_country", u.country);
        editor.putInt("user_region", u.region_id);
        editor.putString("user_phone", u.phone);
        editor.commit();

        String is_order = sharedPref.getString("order_id", "false");

        if(is_order.equals("false")){
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
        else{
            Intent intent = new Intent(this, MyBankingCardActivity.class);
            startActivity(intent);
        }
    }

    public void handleError(String s){
        Toast toast = Toast.makeText(this, s, Toast.LENGTH_LONG);
        toast.show();
    }

    public void handleSuccessRegions(RegionsData r){
        Toast toast = Toast.makeText(this, "Régions récupérées", Toast.LENGTH_LONG);
        this.regionsData = r;

        final Spinner spinnerRegions = findViewById(R.id.spinnerRegions);
        String[] arraySpinner = new String[this.regionsData.regionsData.length+1];
        arraySpinner[0] = "Région";
        for (int i = 0 ; i < this.regionsData.regionsData.length ; i++) {
            arraySpinner[i+1] = this.regionsData.regionsData[i].name;
        }

        this.spinnerArrayAdapterRegions = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arraySpinner){
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
    }

    public void handleErrorRegions(String s){
        Toast toast = Toast.makeText(this, s, Toast.LENGTH_LONG);
        toast.show();
    }
}
