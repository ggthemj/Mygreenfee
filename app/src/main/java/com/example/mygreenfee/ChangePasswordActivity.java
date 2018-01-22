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

public class ChangePasswordActivity extends AppCompatActivity {
    private ChangePasswordRepository changePasswordRepository ;

    private boolean is_mdp_ok;
    private boolean is_mdpc1_ok;
    private boolean is_mdpc2_ok;

    // Méthode appelée quand l'utilisateur appuie sur le bouton de login
    // Appelle la BDD et vérifie si l'utilisateur existe
    public void handleValidation(){
        updateMdpStatus();
        updateMdpcStatus();
        updateMdpc2Status();
        if(is_mdp_ok && is_mdpc1_ok && is_mdpc2_ok) {
            EditText editText = (EditText) findViewById(R.id.mdp);
            String oldmdp = editText.getText().toString();

            editText = (EditText) findViewById(R.id.conf1);
            String conf1 = editText.getText().toString();

            SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString("user_password", conf1);
            editor.commit();

            int id = sharedPref.getInt("user_id", 12);
            String sid = ""+id;

            changePasswordRepository.update(oldmdp, conf1, sid);
        }
        else{
            Toast toast = Toast.makeText(this, R.string.changePassword_Error, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Attribution du layout et instanciation du repo.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        this.changePasswordRepository = new ChangePasswordRepository(this);
        is_mdp_ok = false;
        is_mdpc1_ok = false;
        is_mdpc2_ok = false;

        //Mise en place de la custom app bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar2);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Code qui permet de gérer le contrôle de surface sur le mail
        EditText login = findViewById(R.id.mdp);
        login.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    updateMdpStatus();
                }
                else{
                    TextView loginError = findViewById(R.id.error_mdp);
                    loginError.setText("");
                }
            }
        });
        TextView loginError = findViewById(R.id.error_mdp);
        loginError.setText("");

        //Code qui permet de gérer le contrôle de surface sur le mail
        EditText mdp = findViewById(R.id.conf1);
        mdp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    updateMdpStatus();
                }
                else{
                    TextView loginError = findViewById(R.id.error_conf1);
                    loginError.setText("");
                }
            }
        });
        TextView  conf1Error= findViewById(R.id.error_conf1);
        conf1Error.setText("");

        //Code qui permet de gérer le contrôle de surface sur le mail
        EditText mdp2 = findViewById(R.id.conf2);
        mdp2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    updateMdpStatus();
                }
                else{
                    TextView loginError = findViewById(R.id.error_conf2);
                    loginError.setText("");
                }
            }
        });
        TextView conf2Error = findViewById(R.id.error_conf2);
        conf2Error.setText("");

        //Bind des buttons avec les méthodes correspondantes
        final Button buttonValidation = findViewById(R.id.buttonvalidation);
        buttonValidation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleValidation();
            }
        });
    }

    // Méthode appelée quand le login est réussi !
    public void handleSuccess(){
        Intent intent = new Intent(this, MonCompteActivity.class);
        startActivity(intent);
    }

    // Méthode appelée quand le login est refusé (avec message d'erreur) !
    public void handleError(String s){
        Toast toast = Toast.makeText(this, s, Toast.LENGTH_LONG);
        toast.show();
    }

    public void updateMdpStatus(){
        EditText editText = findViewById(R.id.mdp);
        String mdp = editText.getText().toString();
        is_mdp_ok = (mdp.length() > 0);
        if(!is_mdp_ok){
            TextView loginError = findViewById(R.id.error_mdp);
            loginError.setText(R.string.creationCompte_ErreurMdp);
        }
    }

    public void updateMdpcStatus(){
        EditText editText = findViewById(R.id.conf1);
        String mdp = editText.getText().toString();
        is_mdpc1_ok = (mdp.length() > 0);
        if(!is_mdpc1_ok){
            TextView loginError = findViewById(R.id.error_conf1);
            loginError.setText(R.string.creationCompte_ErreurMdp);
        }
    }

    public void updateMdpc2Status(){
        EditText editText = findViewById(R.id.conf1);
        String mdp = editText.getText().toString();
        editText = (EditText) findViewById(R.id.conf2);
        String mdpc = editText.getText().toString();
        is_mdpc2_ok = (mdpc.equals(mdp));
        if(!is_mdpc2_ok){
            TextView loginError = findViewById(R.id.error_conf2);
            loginError.setText(R.string.creationCompte_ErreurMdpc);
        }
    }

}
