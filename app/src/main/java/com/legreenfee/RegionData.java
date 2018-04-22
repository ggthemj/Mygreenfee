package com.legreenfee;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class RegionData {

    public String public_id;
    public String name;

    //Constructeur d'une région avec toutes les données nécessaires
    public RegionData(JSONObject json){
        try {
            if (json.has("id")) {
                this.public_id = json.getString("id");
            }
            else{
                Log.d("DEBUG","Manque l'ID");
            }
            if (json.has("name")) {
                this.name = json.getString("name");
            }
            else{
                Log.d("DEBUG","Manque le nom");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d("DEBUG","J'ajoute la région : "+ this.name);
    }
}
