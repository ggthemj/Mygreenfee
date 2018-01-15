package com.example.mygreenfee;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Tee {

    private String public_id;
    private String name;

    // Constructeur d'un parcours avec toutes les données nécessaires
    public Tee(JSONObject json){
        try {
            if (json.has("public_id")) {
                this.public_id = json.getString("public_id");
            }
            else{
                Log.d("DEBUG","Manque l'ID");
            }
            if (json.has("name")) {
                this.name = json.getString("name");
            }
            else {
                Log.d("DEBUG","Manque le nom");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("DEBUG","J'ajoute le tee : "+ this.name);
    }


    public String getPublic_id() {
        return public_id;
    }

    public void setPublic_id(String public_id) {
        this.public_id = public_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
