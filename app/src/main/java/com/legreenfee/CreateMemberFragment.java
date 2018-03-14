package com.legreenfee;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
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

public class CreateMemberFragment extends Fragment {
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
            Spinner mySpinner = getView().findViewById(R.id.spinnerCivilite);
            if ((int) mySpinner.getSelectedItemId() != 0) {
                civ = mySpinner.getSelectedItem().toString();
            }

            EditText editText = getView().findViewById(R.id.nom);
            String nom = editText.getText().toString();

            editText = getView().findViewById(R.id.prenom);
            String pre = editText.getText().toString();

            editText = getView().findViewById(R.id.phone);
            String pho = editText.getText().toString();

            editText = getView().findViewById(R.id.email);
            String ema = editText.getText().toString();

            editText = getView().findViewById(R.id.password);
            String pwd = editText.getText().toString();

            editText = getView().findViewById(R.id.bithday);
            String dob = editText.getText().toString();

            mySpinner = getView().findViewById(R.id.spinnerPays);
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

            mySpinner = getView().findViewById(R.id.spinnerRegions);
            int rid = (int) mySpinner.getSelectedItemId();
            String region_id;
            if(rid==0) {
                region_id = "";
            }
            else{
                region_id = this.regionsData.regionsData[rid - 1].public_id;
            }

            SharedPreferences sharedPref = getContext().getSharedPreferences("appData", Context.MODE_PRIVATE);
            String lang = sharedPref.getString("language", "EN");

            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString("user_password", pwd);
            editor.commit();
            createMemberRepository.update(lang, civ, nom, pre, ema, dob, pwd, pay, region_id, pho);
        }
        else{
            Toast toast = Toast.makeText(getContext(), R.string.creationCompte_ErreurValidation, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         // Créé la vue et retourne une carte vide
        final View view = inflater.inflate(R.layout.activity_create_member, container, false);
        this.createMemberRepository = new CreateMemberRepository(this);

        final Button buttonValidation = view.findViewById(R.id.buttonvalidation);
        buttonValidation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleValidation();
            }
        });

        if(getContext() instanceof HomeMapsActivity) {
            SharedPreferences sharedPref = getContext().getSharedPreferences("appData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("home_state", "4");
            editor.commit();
        }

        final TextView tw2 = view.findViewById(R.id.cgr);
        tw2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                displayCGV();
            }
        });

        final TextView tw = view.findViewById(R.id.mentionslegales);
        tw.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                displayML();
            }
        });

        SharedPreferences sharedPref = getContext().getSharedPreferences("appData", Context.MODE_PRIVATE);
        String is_order = sharedPref.getString("order_id", "false");

        if(!is_order.equals("false")){
            buttonValidation.setText(getResources().getString(R.string.creationCompte_Tunnel));
        }

        //initialisation des textes d'erreur
        TextView prenomError = view.findViewById(R.id.error_prenom);
        prenomError.setText("");
        TextView nomError = view.findViewById(R.id.error_nom);
        nomError.setText("");
        TextView phoneError = view.findViewById(R.id.error_tel);
        phoneError.setText("");
        TextView mailError = view.findViewById(R.id.error_email);
        mailError.setText("");
        TextView mdpError = view.findViewById(R.id.error_mdp);
        mdpError.setText("");
        TextView mdpcError = view.findViewById(R.id.error_mdpc);
        mdpcError.setText("");
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

        //Code qui permet de gérer le contrôle de surface sur le mot de passe
        EditText mdp = view.findViewById(R.id.password);
        mdp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    updateLnameStatus();
                }
                else{
                    TextView loginError = view.findViewById(R.id.error_mdp);
                    loginError.setText("");
                }
            }
        });

        //Code qui permet de gérer le contrôle de surface sur le mot de passe de confirmation
        EditText mdpc = view.findViewById(R.id.passwordconfirmation);
        mdpc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    updateLnameStatus();
                }
                else{
                    TextView loginError = view.findViewById(R.id.error_mdpc);
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
        final ArrayAdapter<String> spinnerArrayAdapterPays = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,getContext().getResources().getStringArray(R.array.countries_array)){
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
                    SharedPreferences sharedPref = getContext().getSharedPreferences("appData", Context.MODE_PRIVATE);
                    String lang = sharedPref.getString("language", "EN");

                    createMemberRepository.updateRegions(lang, paysISO);
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

        String is_club = sharedPref.getString("order_club", "false");
        String is_price = sharedPref.getString("order_price", "false");
        String is_date = sharedPref.getString("order_date", "false");

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

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //Update le champ date de naissance en le mettant au format attendu par le WS
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        EditText edittext = getView().findViewById(R.id.bithday);
        edittext.setText(sdf.format(dobCalendar.getTime()));
    }

    public void displayCGV(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://mygreenfee.com/m/#/info/terms_of_use"));
        startActivity(browserIntent);
    }

    public void displayML(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://mygreenfee.com/m/#/info/legal_info"));
        startActivity(browserIntent);
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

    public void updateMdpStatus(){
        EditText editText = getView().findViewById(R.id.password);
        String mdp = editText.getText().toString();
        is_mdp_ok = (mdp.length() > 0);
        if(!is_mdp_ok){
            TextView loginError = getView().findViewById(R.id.error_mdp);
            loginError.setText(R.string.creationCompte_ErreurMdp);
        }
    }

    public void updateMdpcStatus(){
        EditText editText = getView().findViewById(R.id.password);
        String mdp = editText.getText().toString();
        editText = (EditText) getView().findViewById(R.id.passwordconfirmation);
        String mdpc = editText.getText().toString();
        is_mdpc_ok = (mdpc.equals(mdp));
        if(!is_mdpc_ok){
            TextView loginError = getView().findViewById(R.id.error_mdpc);
            loginError.setText(R.string.creationCompte_ErreurMdpc);
        }
    }

    public void handleSuccess(UserData u){
        Toast toast = Toast.makeText(getContext(), R.string.creationCompte_InscriptionReussie, Toast.LENGTH_LONG);
        toast.show();

        //Remplit le fichier offline avec les informations de l'utilisateur - a optimiser pour stockage in app
        SharedPreferences sharedPref = getContext().getSharedPreferences("appData", Context.MODE_PRIVATE);
        String lang = sharedPref.getString("language", "EN");

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
            HomeMapsActivity hom = (HomeMapsActivity)getContext();
            hom.displayCompte();
        }
        else{
            int arg1 = sharedPref.getInt("order_arg1", 0);
            String arg2 = sharedPref.getString("order_arg2", "false");
            String arg3 = sharedPref.getString("order_arg3", "false");
            int arg4 = sharedPref.getInt("order_arg4", 0);
            String arg5 = sharedPref.getString("order_arg5", "false");
            String arg6 = sharedPref.getString("order_arg6", "false");
            String arg7 = sharedPref.getString("order_arg7", "false");

            createMemberRepository.book(lang, arg1, arg2, arg3, arg4, arg5, u.email, arg6, arg7);
        }
    }

    public void handleError(String s){
        Toast toast = Toast.makeText(getContext(), s, Toast.LENGTH_LONG);
        toast.show();

        SharedPreferences sharedPref = getContext().getSharedPreferences("appData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("order_id", "false");
        editor.commit();

        if(getContext() instanceof OrderActivity) {
            Intent intent = new Intent(getContext(), HomeMapsActivity.class);
            startActivity(intent);
        }
    }

    public void handleBookSuccess(String orderId, String orderClub, String orderPrice, String orderDate) {
        SharedPreferences sharedPref = getContext().getSharedPreferences("appData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.order_id), orderId);
        editor.commit();

        if(getContext() instanceof OrderActivity) {
            OrderActivity ord = (OrderActivity) getContext();
            ord.displayMyBanking();
        }
    }

    public void handleSuccessRegions(RegionsData r){

        this.regionsData = r;

        final Spinner spinnerRegions = getView().findViewById(R.id.spinnerRegions);
        String[] arraySpinner = new String[this.regionsData.regionsData.length+1];
        arraySpinner[0] = "Région";
        for (int i = 0 ; i < this.regionsData.regionsData.length ; i++) {
            arraySpinner[i+1] = this.regionsData.regionsData[i].name;
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
        spinnerArrayAdapterRegions.setDropDownViewResource(R.layout.spinner_item);
        spinnerRegions.setAdapter(spinnerArrayAdapterRegions);
    }

    public void handleErrorRegions(String s){
        Toast toast = Toast.makeText(getContext(), s, Toast.LENGTH_LONG);
        toast.show();
    }
}
