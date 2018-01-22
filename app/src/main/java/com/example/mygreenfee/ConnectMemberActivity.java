package com.example.mygreenfee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ConnectMemberActivity extends AppCompatActivity {
    private ConnectMemberRepository connectMemberRepository ;
    private boolean is_login_ok;

    // Méthode appelée quand l'utilisateur appuie sur le bouton de login
    // Appelle la BDD et vérifie si l'utilisateur existe
    public void handleValidation(){
        updateLoginStatus();
        if(is_login_ok) {
            EditText editText = (EditText) findViewById(R.id.email);
            String email = editText.getText().toString();

            editText = (EditText) findViewById(R.id.password);
            String password = editText.getText().toString();

            SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString("user_password", password);
            editor.commit();

            connectMemberRepository.update(email, password);
        }
        else{
            Toast toast = Toast.makeText(this, R.string.connect_validateError, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    // Futur bouton de connexion Facebook, inactif pour l'instant
    // Sert actuellement à reset le tutoriel !
    public void facebookConnection(){
        Toast toast = Toast.makeText(this, "Tutoriel remis à zéro", Toast.LENGTH_LONG);
        toast.show();
        SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("has_ended_tutorial", "false");
        editor.commit();
    }

    // Bouton de login
    // Vérifie si le compte existe
    public void handleCrea(){
        Intent intent = new Intent(this, CreateMemberActivity.class);
        startActivity(intent);
    }

    // Bouton de login
    // Vérifie si le compte existe
    public void handleOubli(){
        EditText editText = (EditText) findViewById(R.id.email);
        String email = editText.getText().toString();
        if(email.length()<2) {
            Toast toast = Toast.makeText(this, R.string.connect_errorMailMdp, Toast.LENGTH_LONG);
            toast.show();
        }
        else {
            connectMemberRepository.updateMdpOubli(email);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Attribution du layout et instanciation du repo.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_member);
        this.connectMemberRepository = new ConnectMemberRepository(this);
        is_login_ok = false;

        //Mise en place de la custom app bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar2);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Code qui permet de gérer le contrôle de surface sur le mail
        EditText login = findViewById(R.id.email);
        login.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    updateLoginStatus();
                }
                else{
                    TextView loginError = findViewById(R.id.error_email);
                    loginError.setText("");
                }
            }
        });
        TextView loginError = findViewById(R.id.error_email);
        loginError.setText("");

        //Bind des buttons avec les méthodes correspondantes
        final Button buttonValidation = findViewById(R.id.buttonvalidation);
        buttonValidation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleValidation();
            }
        });

        final TextView buttonCreaCompte = findViewById(R.id.creaCompte);
        buttonCreaCompte.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleCrea();
            }
        });

        final TextView mdpOubli = findViewById(R.id.mdpoubli);
        mdpOubli.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleOubli();
            }
        });

        final Button facebookButton = findViewById(R.id.buttonfacebook);
        facebookButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                facebookConnection();
            }
        });
    }

    // Méthode appelée quand le login est réussi !
    public void handleSuccess(UserData u){
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

        Intent intent = new Intent(this, MonCompteActivity.class);
        startActivity(intent);
    }

    // Méthode appelée quand le login est refusé (avec message d'erreur) !
    public void handleError(String s){
        Toast toast = Toast.makeText(this, s, Toast.LENGTH_LONG);
        toast.show();
    }

    // Méthode appelée quand le login est refusé (avec message d'erreur) !
    public void handleErrorMdp(String s){
        Toast toast = Toast.makeText(this, s, Toast.LENGTH_LONG);
        toast.show();
    }

    // Méthode appelée quand le login est refusé (avec message d'erreur) !
    public void handleSuccessMdp(){
        Toast toast = Toast.makeText(this, R.string.connect_successMdp, Toast.LENGTH_LONG);
        toast.show();
    }

    public void updateLoginStatus(){
        EditText editText = (EditText) findViewById(R.id.email);
        String email = editText.getText().toString();
        is_login_ok = email.contains("@") && email.contains(".");
        if(!is_login_ok){
            TextView loginError = (TextView) findViewById(R.id.error_email);
            loginError.setText(R.string.connect_errorLogin);
        }
    }
}
