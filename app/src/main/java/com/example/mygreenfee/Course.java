package com.example.mygreenfee;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Course{

    private int length;
    private int public_id;
    private String name;
    private String color;
    private Hole[] holes;

    // Constructeur d'un parcours avec toutes les données nécessaires
    public Course(JSONObject json){
        try {
            if (json.has("public_id")) {
                this.public_id = json.getInt("public_id");
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
            if (json.has("colors")) {
                this.color = json.getJSONObject("colors").getString("oolor");
                JSONObject holesData = json.getJSONObject("holes");

                JSONArray reponseJSON = json.getJSONArray("holes");
                this.holes = new Hole[reponseJSON.length()];
                for (int i = 0 ; i < reponseJSON.length() ; i++){
                    this.holes[i] = new Hole(reponseJSON.getJSONObject(i));
                    Log.d("DEBUG","trou " + this.holes[i].getNumber() + " ajouté");
                }
                this.length = reponseJSON.length();
            }
            else {
                Log.d("DEBUG","Manque les trous");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("DEBUG","J'ajoute le club : "+ this.name);
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getPublic_id() {
        return public_id;
    }

    public void setPublic_id(int public_id) {
        this.public_id = public_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Hole[] getHoles() {
        return holes;
    }

    public void setHoles(Hole[] holes) {
        this.holes = holes;
    }
}
