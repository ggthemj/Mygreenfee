package com.example.mygreenfee;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class TeeTime {

    public static final String BOOKING_NAME = "Booking";
    private int tee_public_id;
    private String date;
    private String time;
    private double sale_price;
    private double total_price;
    private int reduction;
    private int slots_free;
    private int slots_max;
    private int bon_choix;
    private int special;


    // Constructeur d'un parcours avec toutes les données nécessaires
    public TeeTime(JSONObject json){
        try {
            if (json.has("tee_public_id")) {
                this.tee_public_id = json.getInt("tee_public_id");
            }
            else{
                Log.d("DEBUG","Manque l'ID");
            }
            if (json.has("date")) {
                this.date = json.getString("date");
            }
            else {
                Log.d("DEBUG","Manque la date");
            }
            if (json.has("time")) {
                this.time = json.getString("time");
            }
            else{
                Log.d("DEBUG","Manque le time");
            }
            if (json.has("sale_price")) {
                this.sale_price = json.getDouble("sale_price");
            }
            else {
                Log.d("DEBUG","Manque le sale price");
            }
            if (json.has("total_price")) {
                this.total_price = json.getDouble("total_price");
            }
            else{
                Log.d("DEBUG","Manque le total_price");
            }
            if (json.has("reduction")) {
                this.reduction = json.getInt("reduction");
            }
            else {
                Log.d("DEBUG","Manque la reduction");
            }
            if (json.has("slots_free")) {
                this.slots_free = json.getInt("slots_free");
            }
            else{
                Log.d("DEBUG","Manque les slots free");
            }
            if (json.has("slots_max")) {
                this.slots_max = json.getInt("slots_max");
            }
            else {
                Log.d("DEBUG","Manque les slots max");
            }
            if (json.has("bon_choix")) {
                this.bon_choix = json.getInt("bon_choix");
            }
            else{
                Log.d("DEBUG","Manque le bon choix");
            }
            if (json.has("special")) {
                this.special = json.getInt("special");
            }
            else {
                Log.d("DEBUG","Manque la special");
            }




        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("DEBUG","J'ajoute le teetimes : "+ this.tee_public_id);
    }

    public int getTee_public_id() {
        return tee_public_id;
    }

    public void setTee_public_id(int tee_public_id) {
        this.tee_public_id = tee_public_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getSale_price() {
        return sale_price;
    }

    public void setSale_price(double sale_price) {
        this.sale_price = sale_price;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public int getReduction() {
        return reduction;
    }

    public void setReduction(int reduction) {
        this.reduction = reduction;
    }

    public int getSlots_free() {
        return slots_free;
    }

    public void setSlots_free(int slots_free) {
        this.slots_free = slots_free;
    }

    public int getSlots_max() {
        return slots_max;
    }

    public void setSlots_max(int slots_max) {
        this.slots_max = slots_max;
    }

    public int getBon_choix() {
        return bon_choix;
    }

    public void setBon_choix(int bon_choix) {
        this.bon_choix = bon_choix;
    }

    public int getSpecial() {
        return special;
    }

    public void setSpecial(int special) {
        this.special = special;
    }
}
