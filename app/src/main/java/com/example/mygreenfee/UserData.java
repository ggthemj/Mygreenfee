package com.example.mygreenfee;

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
            if (json.has("public_id")) {
                this.public_id = json.getInt("public_id");
            }
            else{
                Log.d("DEBUG","Manque l'ID");
            }
            if (json.has("title")) {
                this.title = json.getString("title");
            }
            else{
                Log.d("DEBUG","Manque la civilité");
            }
            if (json.has("fname")) {
                this.fname = json.getString("fname");
            }
            else{
                Log.d("DEBUG","Manque le prénom");
            }
            if (json.has("lname")) {
                this.lname = json.getString("lname");
            }
            else{
                Log.d("DEBUG","Manque le nom");
            }
            if (json.has("email")) {
                this.email = json.getString("email");
            }
            else{
                Log.d("DEBUG","Manque l'email");
            }
            if (json.has("dob")) {
                this.dob = json.getString("dob");
            }
            else{
                Log.d("DEBUG","Manque la date de naissance");
            }
            if (json.has("country")) {
                this.country = json.getString("country");
            }
            else{
                Log.d("DEBUG","Manque le pays");
            }
            if (json.has("region_id")) {
                this.region_id = json.getInt("region_id");
            }
            else{
                Log.d("DEBUG","Manque la région");
            }
            if (json.has("phone")) {
                this.phone = json.getString("phone");
            }
            else{
                Log.d("DEBUG","Manque le numéro de téléphone");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("DEBUG","J'ajoute l'utilisateur : "+ this.public_id);
    }


}
