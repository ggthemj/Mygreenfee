package com.legreenfee;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ClubsData {
    public ClubData[] clubsdata;

    //Constructeur du tableau de clubs qui sera utilisé pour l'affichage au format carte
    public ClubsData(JSONObject json) {
        JSONArray reponseJSON = null;
        try {
            reponseJSON = json.getJSONArray("clubs");
            this.clubsdata = new ClubData[reponseJSON.length()];
            for (int i = 0 ; i < reponseJSON.length() ;i++){
                this.clubsdata[i] = new ClubData(reponseJSON.getJSONObject(i));
                Log.d("DEBUG","Club "+this.clubsdata[i].name+" ajouté");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
