package com.example.mygreenfee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ChangePasswordFragment extends Fragment {
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
            EditText editText = (EditText) getView().findViewById(R.id.mdp);
            String oldmdp = editText.getText().toString();

            editText = (EditText) getView().findViewById(R.id.conf1);
            String conf1 = editText.getText().toString();

            SharedPreferences sharedPref = getContext().getSharedPreferences("appData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString("user_password", conf1);
            editor.commit();

            int id = sharedPref.getInt("user_id", 12);
            String sid = ""+id;

            changePasswordRepository.update(oldmdp, conf1, sid);
        }
        else{
            Toast toast = Toast.makeText(getContext(), R.string.changePassword_Error, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("DEBUG", "Je créée la vue profile");

        // Créé la vue et retourne une carte vide
        final View view = inflater.inflate(R.layout.activity_change_password, container, false);
        this.changePasswordRepository = new ChangePasswordRepository(this);
        is_mdp_ok = false;
        is_mdpc1_ok = false;
        is_mdpc2_ok = false;

        HomeMapsActivity hm = (HomeMapsActivity)this.getContext();
        hm.status=3;
        hm.chooseMenuItem(3);


        //Code qui permet de gérer le contrôle de surface sur le mail
        EditText login = view.findViewById(R.id.mdp);
        login.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    updateMdpStatus();
                }
                else{
                    TextView loginError = view.findViewById(R.id.error_mdp);
                    loginError.setText("");
                }
            }
        });
        TextView loginError = view.findViewById(R.id.error_mdp);
        loginError.setText("");

        //Code qui permet de gérer le contrôle de surface sur le mail
        EditText mdp = view.findViewById(R.id.conf1);
        mdp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    updateMdpStatus();
                }
                else{
                    TextView loginError = view.findViewById(R.id.error_conf1);
                    loginError.setText("");
                }
            }
        });
        TextView  conf1Error= view.findViewById(R.id.error_conf1);
        conf1Error.setText("");

        //Code qui permet de gérer le contrôle de surface sur le mail
        EditText mdp2 = view.findViewById(R.id.conf2);
        mdp2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    updateMdpStatus();
                }
                else{
                    TextView loginError = view.findViewById(R.id.error_conf2);
                    loginError.setText("");
                }
            }
        });
        TextView conf2Error = view.findViewById(R.id.error_conf2);
        conf2Error.setText("");

        //Bind des buttons avec les méthodes correspondantes
        final Button buttonValidation = view.findViewById(R.id.buttonvalidation);
        buttonValidation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleValidation();
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Attribution du layout et instanciation du repo.
        super.onCreate(savedInstanceState);
    }

    // Méthode appelée quand le login est réussi !
    public void handleSuccess(){
        HomeMapsActivity hom = (HomeMapsActivity)getContext();
        hom.displayCompte();
    }

    // Méthode appelée quand le login est refusé (avec message d'erreur) !
    public void handleError(String s){
        Toast toast = Toast.makeText(getContext(), s, Toast.LENGTH_LONG);
        toast.show();
    }

    public void updateMdpStatus(){
        EditText editText = getView().findViewById(R.id.mdp);
        String mdp = editText.getText().toString();
        is_mdp_ok = (mdp.length() > 0);
        if(!is_mdp_ok){
            TextView loginError = getView().findViewById(R.id.error_mdp);
            loginError.setText(R.string.creationCompte_ErreurMdp);
        }
    }

    public void updateMdpcStatus(){
        EditText editText = getView().findViewById(R.id.conf1);
        String mdp = editText.getText().toString();
        is_mdpc1_ok = (mdp.length() > 0);
        if(!is_mdpc1_ok){
            TextView loginError = getView().findViewById(R.id.error_conf1);
            loginError.setText(R.string.creationCompte_ErreurMdp);
        }
    }

    public void updateMdpc2Status(){
        EditText editText = getView().findViewById(R.id.conf1);
        String mdp = editText.getText().toString();
        editText = (EditText) getView().findViewById(R.id.conf2);
        String mdpc = editText.getText().toString();
        is_mdpc2_ok = (mdpc.equals(mdp));
        if(!is_mdpc2_ok){
            TextView loginError = getView().findViewById(R.id.error_conf2);
            loginError.setText(R.string.creationCompte_ErreurMdpc);
        }
    }

}
