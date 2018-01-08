package com.example.mygreenfee;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Julien on 07/01/2018.
 */

public class Hole {

    private int number;
    private int si;
    private int length;
    private int par;

    public Hole(JSONObject json) {
        try {

            if (json.has("number")) {
                this.number = json.getInt("number");
            }
            else {
                Log.d("DEBUG", "Manque le number");
            }
            if (json.has("si")) {
                this.si = json.getInt("si");
            }
            else {
                Log.d("DEBUG", "Manque le si");
            }
            if (json.has("length")) {
                this.length = json.getInt("length");
            }
            else {
                Log.d("DEBUG", "Manque le length");
            }
            if (json.has("par")) {
                this.par = json.getInt("par");
            }
            else {
                Log.d("DEBUG", "Manque le par");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("DEBUG","J'ajoute le trou : "+ this.number);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSi() {
        return si;
    }

    public void setSi(int si) {
        this.si = si;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getPar() {
        return par;
    }

    public void setPar(int par) {
        this.par = par;
    }
}
