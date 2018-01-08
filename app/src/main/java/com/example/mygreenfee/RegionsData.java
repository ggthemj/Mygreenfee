package com.example.mygreenfee;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegionsData {

    public RegionData[] regionsData;

    //Constructeur du tableau de clubs qui sera utilisé pour l'affichage au format carte
    public RegionsData(JSONObject json) {
        JSONArray reponseJSON = null;
        try {
            reponseJSON = json.getJSONArray("regions");
            this.regionsData = new RegionData[reponseJSON.length()];
            for (int i = 0 ; i < reponseJSON.length() ;i++){
                this.regionsData[i] = new RegionData(reponseJSON.getJSONObject(i));
                Log.d("DEBUG","Région "+this.regionsData[i].name+" ajouté");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
