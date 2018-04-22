package com.legreenfee;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateMemberRepository {
    //L'activité parente, appelée pour déclencher certaines méthodes selon les retours des WS
    MonCompteFragment context;

    //les données de l'utilisateur qui seront mises à jour
    UserData userData ;
    RegionsData regionsData;

    //Les paramètres de la requête http
    Map<String, String> mHeaders;
    Map<String, String> mParams;

    //Constructeur
    public UpdateMemberRepository(MonCompteFragment c){
        this.context = c ;
    }

    public void updateMember(final String lan, final String mid, final String civilite, final String nom, final String prenom, final String email, final String dob, final String country, final String region_id, final String phone){
        Log.d("DEBUG", "Debut de la requete de modification de compte avec les params "+mid+"/"+civilite+"/"+prenom+"/"+nom+"/"+dob+"/"+email+"/"+country+"/"+region_id+"/"+phone);

        //Préparation de la requête
        RequestQueue queue = Volley.newRequestQueue(this.context.getContext());
        String url = context.getResources().getString(R.string.URL_updateMember);
        mHeaders = new HashMap<String, String>();
        mHeaders.put("X-API-KEY", context.getResources().getString(R.string.API_KEY));
        mHeaders.put("CONTENT-LANGUAGE", lan);
        mParams = new HashMap<String, String>();
        mParams.put("data[member_id]", mid);
        mParams.put("data[title]", civilite);
        mParams.put("data[fname]", prenom);
        mParams.put("data[lname]", nom);
        mParams.put("data[dob]", dob);
        mParams.put("data[email]", email);
        mParams.put("data[country]", country);
        mParams.put("data[region_id]", region_id);
        mParams.put("data[phone]", phone);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DEBUG", "response : "+response);

                        context.handleSuccessUpdate(civilite, prenom, nom, dob, email, country, Integer.parseInt(region_id), phone);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DEBUG","That didn't work! "+error.getMessage());
                try {
                    if(error.getMessage()!=null) {
                        JSONObject messageErreur = new JSONObject(error.getMessage());
                        context.handleError(messageErreur.getString("error_message"));
                    }
                    else{
                        context.handleError("Unknown Error...");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                return mParams;
            }
            public Map<String, String> getHeaders() {
                return mHeaders;
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError){
                if(volleyError.networkResponse != null && volleyError.networkResponse.data != null){
                    VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                    volleyError = error;
                }
                else{
                    volleyError = new VolleyError("Unknown error");
                }

                return volleyError;
            }
        };
        queue.add(stringRequest);
    }

    //Tentative de login :)
    public void update(final String lan, final String email, final String pwd){
        Log.d("DEBUG", "Début de la requête login avec les identifiants "+email+"/"+pwd);

        //Préparation de la requête;
        RequestQueue queue = Volley.newRequestQueue(this.context.getContext());
        String url = context.getResources().getString(R.string.URL_validateMember)+"&data[email]="+email+"&data[pwd]="+pwd;
        mHeaders = new HashMap<String, String>();
        mHeaders.put("X-API-KEY", context.getResources().getString(R.string.API_KEY));
        mHeaders.put("CONTENT-LANGUAGE", lan);
        mParams = new HashMap<String, String>();
        //mParams.put("data[email]", email);
        //mParams.put("data[pwd]", pwd);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("DEBUG", "réponse login : "+response);
                    try {
                        userData = new UserData(new JSONObject(response));
                        context.handleSuccess(userData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DEBUG","Erreur !"+error.getMessage());
                try {
                    JSONObject messageErreur = new JSONObject(error.getMessage());
                    context.handleError(messageErreur.getString("error_message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                return mParams;
            }
            public Map<String, String> getHeaders() {
                return mHeaders;
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError){
                if(volleyError.networkResponse != null && volleyError.networkResponse.data != null){
                    VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                    volleyError = error;
                }
                else{
                    volleyError = new VolleyError("Unknown error");
                }

                return volleyError;
            }
        };
        queue.add(stringRequest);
    }

    public void updateRegions(final String lan, final String country_id){
        Log.d("DEBUG", "Debut de la requete de récupération des régions");

        //Préparation de la requête
        RequestQueue queue = Volley.newRequestQueue(this.context.getContext());
        String url = context.getResources().getString(R.string.URL_getRegions)+"&data[country]="+country_id;
        mHeaders = new HashMap<String, String>();
        mHeaders.put("X-API-KEY", context.getResources().getString(R.string.API_KEY));
        mHeaders.put("CONTENT-LANGUAGE", lan);
        mParams = new HashMap<String, String>();
        mParams.put("data[country]", country_id);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DEBUG", "response : "+response);
                        try {
                            regionsData = new RegionsData(new JSONObject(response));
                            context.handleSuccessRegions(regionsData);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DEBUG","That didn't work! "+error.getMessage());
                if(error!=null) {
                    try {
                        JSONObject messageErreur = new JSONObject(error.getMessage());
                        context.handleErrorRegions(messageErreur.getString("error_message"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                return mParams;
            }
            public Map<String, String> getHeaders() {
                return mHeaders;
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError){
                if(volleyError.networkResponse != null && volleyError.networkResponse.data != null){
                    VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                    volleyError = error;
                }

                return volleyError;
            }
        };
        queue.add(stringRequest);
    }
}
