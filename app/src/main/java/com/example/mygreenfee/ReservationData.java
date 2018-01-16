package com.example.mygreenfee;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class ReservationData {

    public int public_id;
    public String date;
    public int players;
    public String time;
    public double cost;
    public String currency;
    public int club_id;
    public String club_name;
    public int order_id;

    //Constructeur d'un utilisateur avec toutes les données nécessaires
    public ReservationData(JSONObject json){
        try {
            JSONObject reponseJSON = null;
            reponseJSON = json.getJSONObject("member");

            if (reponseJSON.has("public_id")) {
                this.public_id = reponseJSON.getInt("public_id");
            }
            else{
                Log.d("DEBUG","Manque l'ID");
            }
            if (reponseJSON.has("date")) {
                this.date = reponseJSON.getString("date");
            }
            else{
                Log.d("DEBUG","Manque la date");
            }
            if (reponseJSON.has("players")) {
                this.players = reponseJSON.getInt("players");
            }
            else{
                Log.d("DEBUG","Manque le players");
            }
            if (reponseJSON.has("time")) {
                this.time = reponseJSON.getString("time");
            }
            else{
                Log.d("DEBUG","Manque le time");
            }
            if (reponseJSON.has("cost")) {
                this.cost = reponseJSON.getDouble("cost");
            }
            else{
                Log.d("DEBUG","Manque le cost");
            }
            if (reponseJSON.has("currency")) {
                this.currency = reponseJSON.getString("currency");
            }
            else{
                Log.d("DEBUG","Manque la currency");
            }
            if (reponseJSON.has("club_id")) {
                this.club_id = reponseJSON.getInt("club_id");
            }
            else{
                Log.d("DEBUG","Manque le club_id");
            }
            if (reponseJSON.has("club_name")) {
                this.club_name = reponseJSON.getString("club_name");
            }
            else{
                Log.d("DEBUG","Manque le club_name");
            }
            if (reponseJSON.has("order_id")) {
                this.order_id = reponseJSON.getInt("order_id");
            }
            else{
                Log.d("DEBUG","Manque le orderid");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("DEBUG","J'ajoute la réservation : "+ this.public_id);
    }
}
