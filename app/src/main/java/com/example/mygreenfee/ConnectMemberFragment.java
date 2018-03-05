package com.example.mygreenfee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ConnectMemberFragment extends Fragment {
    private ConnectMemberRepository connectMemberRepository ;
    private boolean is_login_ok;

    // Méthode appelée quand l'utilisateur appuie sur le bouton de login
    // Appelle la BDD et vérifie si l'utilisateur existe
    public void handleValidation(){
        updateLoginStatus();
        if(is_login_ok) {
            EditText editText = (EditText) getView().findViewById(R.id.email);
            String email = editText.getText().toString();

            editText = (EditText) getView().findViewById(R.id.password);
            String password = editText.getText().toString();

            SharedPreferences sharedPref = getContext().getSharedPreferences("appData", Context.MODE_PRIVATE);
            String lang = sharedPref.getString("language", "EN");

            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString("user_password", password);
            editor.commit();

            connectMemberRepository.update(lang, email, password);
        }
        else{
            Toast toast = Toast.makeText(getContext(), R.string.connect_validateError, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void handleCrea(){
        HomeMapsActivity hom = (HomeMapsActivity)getContext();
        hom.displayCrea();
    }

    public void handleOubli(){
        SharedPreferences sharedPref = getContext().getSharedPreferences("appData", Context.MODE_PRIVATE);
        String lang = sharedPref.getString("language", "EN");

        EditText editText = (EditText) getView().findViewById(R.id.email);
        String email = editText.getText().toString();
        if(email.length()<2) {
            Toast toast = Toast.makeText(getContext(), R.string.connect_errorMailMdp, Toast.LENGTH_LONG);
            toast.show();
        }
        else {
            connectMemberRepository.updateMdpOubli(lang, email);
        }
    }

    public void handleErrorMdp(String s){
        Toast toast = Toast.makeText(getContext(), s, Toast.LENGTH_LONG);
        toast.show();
    }

    // Méthode appelée quand le login est refusé (avec message d'erreur) !
    public void handleSuccessMdp(){
        Toast toast = Toast.makeText(getContext(), R.string.connect_successMdp, Toast.LENGTH_LONG);
        toast.show();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Créé la vue et retourne une carte vide
        final View view = inflater.inflate(R.layout.activity_connect_member, container, false);

        if(getContext() instanceof HomeMapsActivity) {
            SharedPreferences sharedPref = getContext().getSharedPreferences("appData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("home_state", "3");
            editor.commit();
        }

        is_login_ok = false;
        //Code qui permet de gérer le contrôle de surface sur le mail
        EditText login = view.findViewById(R.id.email);
        login.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    updateLoginStatus();
                }
                else{
                    TextView loginError = view.findViewById(R.id.error_email);
                    loginError.setText("");
                }
            }
        });
        TextView loginError = view.findViewById(R.id.error_email);
        loginError.setText("");

        //Bind des buttons avec les méthodes correspondantes
        final Button buttonValidation = view.findViewById(R.id.buttonvalidation);
        buttonValidation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleValidation();
            }
        });

        final TextView buttonCreaCompte = view.findViewById(R.id.creaCompte);
        buttonCreaCompte.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleCrea();
            }
        });

        final TextView mdpOubli = view.findViewById(R.id.mdpoubli);
        mdpOubli.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleOubli();
            }
        });

        this.connectMemberRepository = new ConnectMemberRepository(this);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Attribution du layout et instanciation du repo.
        super.onCreate(savedInstanceState);
    }

    // Méthode appelée quand le login est réussi !
    public void handleSuccess(UserData u){
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
            hom.displayMonProfil();
        }
        else{
            int arg1 = sharedPref.getInt("order_arg1", 0);
            String arg2 = sharedPref.getString("order_arg2", "false");
            String arg3 = sharedPref.getString("order_arg3", "false");
            int arg4 = sharedPref.getInt("order_arg4", 0);
            String arg5 = sharedPref.getString("order_arg5", "false");
            String arg6 = sharedPref.getString("order_arg6", "false");
            String arg7 = sharedPref.getString("order_arg7", "false");

            connectMemberRepository.book(lang, arg1, arg2, arg3, arg4, arg5, u.email, arg6, arg7);
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

    public void handleError(String s){
        Toast toast = Toast.makeText(getContext(), s, Toast.LENGTH_LONG);
        toast.show();
    }

    public void updateLoginStatus(){
        EditText editText = (EditText) getView().findViewById(R.id.email);
        String email = editText.getText().toString();
        is_login_ok = email.contains("@") && email.contains(".");
        if(!is_login_ok){
            TextView loginError = (TextView) getView().findViewById(R.id.error_email);
            loginError.setText(R.string.connect_errorLogin);
        }
    }
}
