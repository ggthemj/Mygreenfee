package com.legreenfee;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClubCard {

    private int public_id;
    private String name;
    private Integer discount;
    private List<Integer> coursesId;

    public ClubCard() {

    }

    // Constructeur d'un parcours avec toutes les données nécessaires
    public ClubCard(JSONObject json){
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
            if (json.has("discount")) {
                this.discount = json.getInt("discount");
            }
            else {
                Log.d("DEBUG","Manque le discount");
            }

            this.coursesId = new ArrayList<Integer>();
            if (json.has("courses")) {
                JSONArray a = json.getJSONArray("courses");
                if (a != null && a.length() != 0) {
                    for (int i = 0 ; i < a.length() ; i ++) {
                        this.coursesId.add(a.getInt(i));
                    }
                } else {
                    Log.d("DEBUG", "Manque le course id for discount card");
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public List<Integer> getCoursesId() {
        return coursesId;
    }

    public void setCoursesId(List<Integer> coursesId) {
        this.coursesId = coursesId;
    }

    @Override
    public String toString() {
        return name;
    }
}
