package com.legreenfee;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MonCompteFragment extends Fragment {
    private UpdateMemberRepository updateMemberRepository ;
    private Calendar dobCalendar;
    private boolean is_phone_ok;
    private boolean is_fname_ok;
    private boolean is_lname_ok;
    private boolean is_mail_ok;
    private boolean is_bday_ok;
    private RegionsData regionsData;
    private ArrayAdapter<String> spinnerArrayAdapterRegions;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("DEBUG", "Je créée la vue profile");

        // Créé la vue et retourne une carte vide
        final View view = inflater.inflate(R.layout.activity_mon_compte, container, false);

        if(getContext() instanceof HomeMapsActivity){
            SharedPreferences sharedPref = getContext().getSharedPreferences("appData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("home_state", "4");
            editor.commit();
        }

        SharedPreferences sharedPref = getContext().getSharedPreferences("appData", Context.MODE_PRIVATE);
        String lang = sharedPref.getString("language", "EN");

        updateMemberRepository = new UpdateMemberRepository(this);

        final Button buttonValidation = view.findViewById(R.id.buttonvalidation);
        buttonValidation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleValidation();
            }
        });

        final ProgressBar simpleProgressBar = (ProgressBar) view.findViewById(R.id.simpleProgressBar);
        simpleProgressBar.setVisibility(View.VISIBLE);

        //initialisation des textes d'erreur
        TextView prenomError = view.findViewById(R.id.error_prenom);
        prenomError.setText("");
        TextView nomError = view.findViewById(R.id.error_nom);
        nomError.setText("");
        TextView phoneError = view.findViewById(R.id.error_tel);
        phoneError.setText("");
        TextView mailError = view.findViewById(R.id.error_email);
        mailError.setText("");
        TextView dobError = view.findViewById(R.id.error_dob);
        dobError.setText("");

        //Code qui permet de gérer le contrôle de surface sur le prénom
        EditText prenom = view.findViewById(R.id.prenom);
        prenom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    updateFnameStatus();
                }
                else{
                    TextView loginError = view.findViewById(R.id.error_prenom);
                    loginError.setText("");
                }
            }
        });

        //Code qui permet de gérer le contrôle de surface sur le nom
        EditText nom = view.findViewById(R.id.nom);
        nom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    updateLnameStatus();
                }
                else{
                    TextView loginError = view.findViewById(R.id.error_nom);
                    loginError.setText("");
                }
            }
        });

        //Code qui permet de gérer le contrôle de surface sur le nom
        EditText mail = view.findViewById(R.id.email);
        mail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    updateLnameStatus();
                }
                else{
                    TextView loginError = view.findViewById(R.id.error_email);
                    loginError.setText("");
                }
            }
        });

        //Code qui permet de gérer le contrôle de surface sur le nom
        EditText phone = view.findViewById(R.id.phone);
        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    updateLnameStatus();
                }
                else{
                    TextView loginError = view.findViewById(R.id.error_tel);
                    loginError.setText("");
                }
            }
        });

        //Spinner civilité
        final Spinner spinnerCivilite = view.findViewById(R.id.spinnerCivilite);
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.civiliteArray)){
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

        String civilite = sharedPref.getString("user_title", "");
        int positionCivilite = 0 ;
        if(civilite.equals("Ms")) {
            positionCivilite = 1 ;
        }
        else if(civilite.equals("Mrs")) {
            if(lang.equals("DE")) {
                positionCivilite = 1;
            }
            else{
                positionCivilite = 2;
            }
        }
        else if(civilite.equals("Mr")) {
            if(lang.equals("DE")) {
                positionCivilite = 2;
            }
            else{
                positionCivilite = 3;
            }
        }
        spinnerCivilite.setSelection(positionCivilite);

        spinnerCivilite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Spinner pays
        final Spinner spinnerPays = view.findViewById(R.id.spinnerPays);
        final ArrayAdapter<String> spinnerArrayAdapterPays = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.countries_array)){
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

        String pays = sharedPref.getString("user_country", "");
        int positionPays = 0 ;
        if(pays.equals("ZA")) {
            positionPays = 1 ;
        }
        else if(pays.equals("DE")) {
            positionPays = 2 ;
        }
        else if(pays.equals("AT")) {
            positionPays = 3 ;
        }
        else if(pays.equals("FR")) {
            positionPays = 4 ;
        }
        else if(pays.equals("CH")) {
            positionPays = 5 ;
        }
        else if(pays.equals("BE")) {
            positionPays = 6 ;
        }
        spinnerPays.setSelection(positionPays);

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
                    else if(position==6) {
                        paysISO = "BE" ;
                    }
                    SharedPreferences sharedPref = getContext().getSharedPreferences("appData", Context.MODE_PRIVATE);
                    String lang = sharedPref.getString("language", "EN");

                    Log.d("DEBUG", "Je recharge les régions");

                    updateMemberRepository.updateRegions(lang, paysISO);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Spinner régions
        final Spinner spinnerRegions = view.findViewById(R.id.spinnerRegions);
        this.spinnerArrayAdapterRegions = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.regionsArray)){
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

        final EditText edittext = view.findViewById(R.id.bithday);
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
                new DatePickerDialog(getContext(), date, dobCalendar
                        .get(Calendar.YEAR), dobCalendar.get(Calendar.MONTH),
                        dobCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Bind des buttons avec les méthodes correspondantes
        final Button buttonMdp = view.findViewById(R.id.buttonvalidation2);
        buttonMdp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleMdp();
            }
        });


        sharedPref = getContext().getSharedPreferences("appData", Context.MODE_PRIVATE);

        String mailenr = sharedPref.getString("user_email", "false");
        String mdp = sharedPref.getString("user_password", "false");

        Log.d("DEBUG", "Je récupère les données de profil "+mailenr+"/"+mdp);

        updateMemberRepository.update(lang, mailenr, mdp);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void handleMdp(){
        HomeMapsActivity hom = (HomeMapsActivity)getContext();
        hom.displayOubli();
    }

    //Update le champ date de naissance en le mettant au format attendu par le WS
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        EditText edittext = getView().findViewById(R.id.bithday);
        edittext.setText(sdf.format(dobCalendar.getTime()));
    }

    private void updateRegions() {
        SharedPreferences sharedPref = getContext().getSharedPreferences("appData", Context.MODE_PRIVATE);
        String lang = sharedPref.getString("language", "EN");

        String pays = sharedPref.getString("user_country", "");

        if(pays.length()==2){
            updateMemberRepository.updateRegions(lang, pays);
        }
    }

    private void handleSuccessRegions(){

    }

    public void handleValidation() {
        updateFnameStatus();
        updateLnameStatus();
        updatePhoneStatus();
        updateMailStatus();
        updateBdayStatus();

        SharedPreferences sharedPref = getContext().getSharedPreferences("appData", Context.MODE_PRIVATE);
        String lang = sharedPref.getString("language", "EN");

        if (is_mail_ok && is_phone_ok && is_lname_ok && is_fname_ok && is_bday_ok) {
            //Récupération des données du formulaires et passation au Repo
            String civ = "";
            Spinner mySpinner = getView().findViewById(R.id.spinnerCivilite);
            if ((int) mySpinner.getSelectedItemId() != 0) {
                if((int) mySpinner.getSelectedItemId() == 1){
                    civ = "Ms";
                }
                else if((int) mySpinner.getSelectedItemId() == 2){
                    if(lang.equals("DE")) {
                        civ = "Mr";
                    }
                    else {
                        civ = "Mrs";
                    }
                }
                else if((int) mySpinner.getSelectedItemId() == 3){
                    civ = "Mr";
                }
            }

            EditText editText = getView().findViewById(R.id.nom);
            String nom = editText.getText().toString();

            editText = getView().findViewById(R.id.prenom);
            String pre = editText.getText().toString();

            editText = getView().findViewById(R.id.phone);
            String pho = editText.getText().toString();

            editText = getView().findViewById(R.id.email);
            String ema = editText.getText().toString();

            editText = getView().findViewById(R.id.bithday);
            String dob = editText.getText().toString();

            mySpinner = getView().findViewById(R.id.spinnerPays);
            int pays = mySpinner.getSelectedItemPosition();
            String pay="";
            switch (pays) {
                case 1:
                    pay = "ZA";
                    break;
                case 2:
                    pay = "DE";
                    break;
                case 3:
                    pay = "AT";
                    break;
                case 4:
                    pay = "FR";
                    break;
                case 5:
                    pay = "CH";
                    break;
                case 6:
                    pay = "BE";
                    break;
            }

            int id = sharedPref.getInt("user_id", 12);
            String sid = ""+id;

            mySpinner = getView().findViewById(R.id.spinnerRegions);
            int rid = (int) mySpinner.getSelectedItemId();
            int region_id;
            if (rid == 0) {
                region_id = 0;
            } else {
                region_id = Integer.parseInt(this.regionsData.regionsData[rid - 1].public_id);
            }

            final ProgressBar simpleProgressBar = (ProgressBar) getView().findViewById(R.id.simpleProgressBar);
            simpleProgressBar.setVisibility(View.GONE);

            LinearLayout lay = (LinearLayout)getView().findViewById(R.id.hori1);
            lay.setVisibility(View.GONE);
            lay = (LinearLayout)getView().findViewById(R.id.hori2);
            lay.setVisibility(View.GONE);
            lay = (LinearLayout)getView().findViewById(R.id.hori3);
            lay.setVisibility(View.GONE);
            lay = (LinearLayout)getView().findViewById(R.id.hori4);
            lay.setVisibility(View.GONE);
            lay = (LinearLayout)getView().findViewById(R.id.hori5);
            lay.setVisibility(View.GONE);
            lay = (LinearLayout)getView().findViewById(R.id.hori6);
            lay.setVisibility(View.GONE);
            lay = (LinearLayout)getView().findViewById(R.id.hori7);
            lay.setVisibility(View.GONE);
            lay = (LinearLayout)getView().findViewById(R.id.hori8);
            lay.setVisibility(View.GONE);

            Button but = (Button)getView().findViewById(R.id.buttonvalidation);
            but.setVisibility(View.GONE);
            but = (Button)getView().findViewById(R.id.buttonvalidation2);
            but.setVisibility(View.GONE);

            updateMemberRepository.updateMember(lang, sid, civ, nom, pre, ema, dob, pay, ""+region_id, pho);
        } else {
            Toast toast = Toast.makeText(getContext(), R.string.creationCompte_ErreurValidation, Toast.LENGTH_LONG);
            toast.show();
        }
    }


        //Met à jour le statut du champ prénom, et affiche éventuellement les erreurs s'il y en a
    public void updateFnameStatus(){
        EditText editText = getView().findViewById(R.id.prenom);
        String prenom = editText.getText().toString();
        is_fname_ok = (prenom.length()>0);
        if(!is_fname_ok){
            TextView loginError = getView().findViewById(R.id.error_prenom);
            loginError.setText(R.string.creationCompte_ErreurPrenom);
        }
    }

    //Met à jour le statut du champ prénom, et affiche éventuellement les erreurs s'il y en a
    public void updateBdayStatus(){
        EditText editText = getView().findViewById(R.id.bithday);
        String bday = editText.getText().toString();
        is_bday_ok = (bday.length()>0);
        if(!is_bday_ok){
            TextView loginError = getView().findViewById(R.id.error_dob);
            loginError.setText(R.string.creationCompte_ErreurBday);
        }
    }

    //Met à jour le statut du champ nom, et affiche éventuellement les erreurs s'il y en a
    public void updateLnameStatus(){
        EditText editText = getView().findViewById(R.id.nom);
        String nom = editText.getText().toString();
        is_lname_ok = (nom.length()>0);
        if(!is_lname_ok){
            TextView loginError = getView().findViewById(R.id.error_nom);
            loginError.setText(R.string.creationCompte_ErreurNom);
        }
    }

    //Met à jour le statut du champ phone, et affiche éventuellement les erreurs s'il y en a
    public void updatePhoneStatus(){
        EditText editText = getView().findViewById(R.id.phone);
        String phone = editText.getText().toString();
        is_phone_ok = (phone.length() > 0);
        if(!is_phone_ok){
            TextView loginError = getView().findViewById(R.id.error_tel);
            loginError.setText(R.string.creationCompte_ErreurPhone);
        }
    }

    //Met à jour le statut du champ mail, et affiche éventuellement les erreurs s'il y en a
    public void updateMailStatus(){
        EditText editText = getView().findViewById(R.id.email);
        String mail = editText.getText().toString();
        is_mail_ok = (mail.length()>0);
        if(!is_mail_ok){
            TextView loginError = getView().findViewById(R.id.error_email);
            loginError.setText(R.string.creationCompte_ErreurMail);
        }
    }

    public void handleSuccessRegions(RegionsData r){
        this.regionsData = r;

        SharedPreferences sharedPref = getContext().getSharedPreferences("appData", Context.MODE_PRIVATE);
        int regionid = sharedPref.getInt("user_region", -1);
        int positionTo = 0;

        final Spinner spinnerRegions = getView().findViewById(R.id.spinnerRegions);
        String[] arraySpinner = new String[this.regionsData.regionsData.length+1];
        arraySpinner[0] = "Région";
        for (int i = 0 ; i < this.regionsData.regionsData.length ; i++) {
            arraySpinner[i+1] = this.regionsData.regionsData[i].name;
            //Log.d("DEBUG", "Je compare -"+this.regionsData.regionsData[i].public_id+"- et -"+regionid);
            if(this.regionsData.regionsData[i].public_id.equals(""+regionid)){
                positionTo = i+1;
            }
        }

        this.spinnerArrayAdapterRegions = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,arraySpinner){
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
        Log.d("DEBUG", "J'initialise le spinner région en position "+positionTo);

        spinnerArrayAdapterRegions.setDropDownViewResource(R.layout.spinner_item);
        spinnerRegions.setAdapter(spinnerArrayAdapterRegions);

        spinnerRegions.setSelection(positionTo);
    }

    public void handleErrorRegions(String s){
        Toast toast = Toast.makeText(getContext(), s, Toast.LENGTH_LONG);
        toast.show();
    }

    // Méthode appelée quand le login est réussi !
    public void handleSuccess(UserData u){
        //Remplit le fichier offline avec les informations de l'utilisateur - a optimiser pour stockage in app
        SharedPreferences sharedPref = getContext().getSharedPreferences("appData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
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

        EditText nom = getView().findViewById(R.id.nom);
        nom.setText(u.lname);
        EditText prenom = getView().findViewById(R.id.prenom);
        prenom.setText(u.fname);
        EditText phone = getView().findViewById(R.id.phone);
        phone.setText(u.phone);
        EditText mail = getView().findViewById(R.id.email);
        mail.setText(u.email);
        EditText dob = getView().findViewById(R.id.bithday);
        dob.setText(u.dob);

        final ProgressBar simpleProgressBar = (ProgressBar) getView().findViewById(R.id.simpleProgressBar);
        simpleProgressBar.setVisibility(View.GONE);

        LinearLayout lay = (LinearLayout)getView().findViewById(R.id.hori1);
        lay.setVisibility(View.VISIBLE);
        lay = (LinearLayout)getView().findViewById(R.id.hori2);
        lay.setVisibility(View.VISIBLE);
        lay = (LinearLayout)getView().findViewById(R.id.hori3);
        lay.setVisibility(View.VISIBLE);
        lay = (LinearLayout)getView().findViewById(R.id.hori4);
        lay.setVisibility(View.VISIBLE);
        lay = (LinearLayout)getView().findViewById(R.id.hori5);
        lay.setVisibility(View.VISIBLE);
        lay = (LinearLayout)getView().findViewById(R.id.hori6);
        lay.setVisibility(View.VISIBLE);
        lay = (LinearLayout)getView().findViewById(R.id.hori7);
        lay.setVisibility(View.VISIBLE);
        lay = (LinearLayout)getView().findViewById(R.id.hori8);
        lay.setVisibility(View.VISIBLE);

        Button but = (Button)getView().findViewById(R.id.buttonvalidation);
        but.setVisibility(View.VISIBLE);
        but = (Button)getView().findViewById(R.id.buttonvalidation2);
        but.setVisibility(View.VISIBLE);
    }

    // Méthode appelée quand le login est réussi !
    public void handleSuccessUpdate(String civ, String prenom, String nom, String dob, String email, String country, int rid, String phone){
        SharedPreferences sharedPref = getContext().getSharedPreferences("appData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("user_title", civ);
        editor.putString("user_fname", prenom);
        editor.putString("user_lname", nom);
        editor.putString("user_dob", dob);
        editor.putString("user_email", email);
        editor.putString("user_country", country);
        editor.putInt("user_region", rid);
        editor.putString("user_phone", phone);
        editor.commit();

        HomeMapsActivity hom = (HomeMapsActivity)getContext();
        hom.displayCompte();
    }

    // Méthode appelée quand le login est refusé (avec message d'erreur) !
    public void handleError(String s){
        Toast toast = Toast.makeText(getContext(), s, Toast.LENGTH_LONG);
        toast.show();

        final ProgressBar simpleProgressBar = (ProgressBar) getView().findViewById(R.id.simpleProgressBar);
        simpleProgressBar.setVisibility(View.GONE);

        LinearLayout lay = (LinearLayout)getView().findViewById(R.id.hori1);
        lay.setVisibility(View.VISIBLE);
        lay = (LinearLayout)getView().findViewById(R.id.hori2);
        lay.setVisibility(View.VISIBLE);
        lay = (LinearLayout)getView().findViewById(R.id.hori3);
        lay.setVisibility(View.VISIBLE);
        lay = (LinearLayout)getView().findViewById(R.id.hori4);
        lay.setVisibility(View.VISIBLE);
        lay = (LinearLayout)getView().findViewById(R.id.hori5);
        lay.setVisibility(View.VISIBLE);
        lay = (LinearLayout)getView().findViewById(R.id.hori6);
        lay.setVisibility(View.VISIBLE);
        lay = (LinearLayout)getView().findViewById(R.id.hori7);
        lay.setVisibility(View.VISIBLE);
        lay = (LinearLayout)getView().findViewById(R.id.hori8);
        lay.setVisibility(View.VISIBLE);

        Button but = (Button)getView().findViewById(R.id.buttonvalidation);
        but.setVisibility(View.VISIBLE);
        but = (Button)getView().findViewById(R.id.buttonvalidation2);
        but.setVisibility(View.VISIBLE);
    }
}
