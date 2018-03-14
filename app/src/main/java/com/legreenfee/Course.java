package com.legreenfee;

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
    private Tee[] tees;

    public Course() {

    }

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

            JSONArray s = json.getJSONArray("tees");
            this.tees = new Tee[s.length()];
            for (int i = 0 ; i < s.length() ; i++){
                this.tees[i] = new Tee(s.getJSONObject(i));
                Log.d("DEBUG","tee " + this.tees[i].getPublic_id() + " ajouté");
            }

            if (json.has("colors")) {
                JSONArray a = json.getJSONArray("colors");
                if (a != null) {
                    json = a.getJSONObject(0);
                    if (json.has("color")) {
                        this.color = json.getString("color");
                    }
                    else {
                        Log.d("DEBUG","Manque le color");
                    }


                    JSONArray responseJSON2 = json.getJSONArray("holes");
                    this.holes = new Hole[responseJSON2.length()];
                    for (int i = 0; i < responseJSON2.length(); i++) {
                        this.holes[i] = new Hole(responseJSON2.getJSONObject(i));
                        Log.d("DEBUG", "trou " + this.holes[i].getNumber() + " ajouté");
                    }
                    this.length = responseJSON2.length();
                }
            }
            else {
                Log.d("DEBUG","Manque les trous");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("DEBUG","J'ajoute le parcours : "+ this.name);
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

    public Tee[] getTees() {
        return tees;
    }

    public void setTees(Tee[] tees) {
        this.tees = tees;
    }
}
