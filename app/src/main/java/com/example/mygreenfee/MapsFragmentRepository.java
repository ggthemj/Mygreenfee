package com.example.mygreenfee;

import android.content.Context;
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

public class MapsFragmentRepository {
    public ClubsData clubsData;
    MapsFragment fragmentContext;
    HomeMapsActivity activityContext;
    Map<String, String> mHeaders;

    public MapsFragmentRepository(HomeMapsActivity h, MapsFragment m){
        this.fragmentContext = m ;
        this.activityContext = h;
    }

    public void update(){
        //Récupération des clubs de golf
        Log.d("DEBUG", "UPDATE REPO CLUBS");
        RequestQueue queue = Volley.newRequestQueue(activityContext);
        String url = activityContext.getResources().getString(R.string.URL_getAllClubs);

        mHeaders = new HashMap<String, String>();
        mHeaders.put("X-API-KEY", activityContext.getResources().getString(R.string.API_KEY));
        mHeaders.put("CONTENT-LANGUAGE", activityContext.getResources().getString(R.string.CONTENT_LANGUAGE));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("DEBUG", "réponse webservice clubs : "+response);

                    try {
                        clubsData = new ClubsData(new JSONObject(response));
                        fragmentContext.handleSuccess();
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
                    fragmentContext.handleError(messageErreur.getString("error_message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }) {
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
