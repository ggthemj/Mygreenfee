package com.legreenfee;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class UserData {

    public int public_id;
    public String title;
    public String fname;
    public String lname;
    public String dob;
    public String email;
    public String country;
    public int region_id;
    public String phone;

    //Constructeur d'un utilisateur avec toutes les données nécessaires
    public UserData(JSONObject json){
        try {
            JSONObject reponseJSON = null;
            reponseJSON = json.getJSONObject("member");

            if (reponseJSON.has("public_id")) {
                this.public_id = reponseJSON.getInt("public_id");
            }
            else{
                Log.d("DEBUG","Manque l'ID");
            }
            if (reponseJSON.has("title")) {
                this.title = reponseJSON.getString("title");
            }
            else{
                Log.d("DEBUG","Manque la civilité");
            }
            if (reponseJSON.has("fname")) {
                this.fname = reponseJSON.getString("fname");
            }
            else{
                Log.d("DEBUG","Manque le prénom");
            }
            if (reponseJSON.has("lname")) {
                this.lname = reponseJSON.getString("lname");
            }
            else{
                Log.d("DEBUG","Manque le nom");
            }
            if (reponseJSON.has("email")) {
                this.email = reponseJSON.getString("email");
            }
            else{
                Log.d("DEBUG","Manque l'email");
            }
            if (reponseJSON.has("dob")) {
                this.dob = reponseJSON.getString("dob");
            }
            else{
                Log.d("DEBUG","Manque la date de naissance");
            }
            if (reponseJSON.has("country")) {
                this.country = reponseJSON.getString("country");
            }
            else{
                Log.d("DEBUG","Manque le pays");
            }
            if (reponseJSON.has("region_id")) {
                this.region_id = reponseJSON.getInt("region_id");
            }
            else{
                Log.d("DEBUG","Manque la région");
            }
            if (reponseJSON.has("phone")) {
                this.phone = reponseJSON.getString("phone");
            }
            else{
                Log.d("DEBUG","Manque le numéro de téléphone");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("DEBUG","J'ajoute l'utilisateur : "+ this.public_id);
    }

    public UserData(int id, String tit, String fna, String lna, String dob, String ema, String cou, int reg, String pho){
        this.public_id = id;
        this.title = tit;
        this.fname = fna;
        this.lname = lna;
        this.dob = dob;
        this.email = ema;
        this.country = cou;
        this.region_id = reg;
        this.phone = pho;
    }
}
