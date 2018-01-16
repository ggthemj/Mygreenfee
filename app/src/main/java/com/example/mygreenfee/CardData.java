package com.example.mygreenfee;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
public class CardData {

    public int id;
    public int user_id;
    public String expiration_date;
    public String alias;
    public String card_type;
    public boolean active;
    public String validity;
    public String currency;

    //Constructeur d'un club avec toutes les données nécessaires
    public CardData(JSONObject json){
        try {
            if (json.has("id")) {
                this.id = json.getInt("id");
            }
            else{
                Log.d("DEBUG","Manque l'ID");
            }
            if (json.has("user_id")) {
                this.user_id = json.getInt("user_id");
            }
            else{
                Log.d("DEBUG","Manque le user id");
            }
            if (json.has("expiration_date")) {
                this.expiration_date = json.getString("expiration_date");
            }
            else{
                Log.d("DEBUG","Manque la expiration_date");
            }
            if (json.has("alias")) {
                this.alias = json.getString("alias");
            }
            else{
                Log.d("DEBUG","Manque l'alias");
            }
            if (json.has("card_type")) {
                this.card_type = json.getString("card_type");
            }
            else{
                Log.d("DEBUG","Manque card_type");
            }
            if (json.has("active")) {
                this.active = json.getBoolean("active");
            }
            else{
                Log.d("DEBUG","Manque active");
            }
            if (json.has("validity")) {
                this.validity = json.getString("validity");
            }
            else{
                Log.d("DEBUG","Manque validity");
            }
            if (json.has("currency")) {
                this.currency = json.getString("currency");
            }
            else{
                Log.d("DEBUG","Manque la currency");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("DEBUG","J'ajoute la carte : "+ this.alias);
    }
}
