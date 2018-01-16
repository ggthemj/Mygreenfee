package com.example.mygreenfee;

import android.support.annotation.NonNull;
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
    ClubListActivity clubListContext;
    public ClubsData clubsData;
    MapsFragment fragmentContext;
    HomeMapsActivity activityContext;
    Map<String, String> mHeaders;


    public MapsFragmentRepository(HomeMapsActivity h, MapsFragment m){
        this.fragmentContext = m ;
        this.activityContext = h;
    }

    public MapsFragmentRepository(ClubListActivity clubListActivity) {
        this.clubListContext = clubListActivity;
    }

    public void updateFromSearch(){
        RequestQueue queue = Volley.newRequestQueue(clubListContext);
        //Récupération des clubs de golf
        Log.d("DEBUG", "UPDATE REPO CLUBS");
        mHeaders = new HashMap<String, String>();
        mHeaders.put("X-API-KEY", clubListContext.getResources().getString(R.string.API_KEY));
        mHeaders.put("CONTENT-LANGUAGE", clubListContext.getResources().getString(R.string.CONTENT_LANGUAGE));

        String url = clubListContext.getResources().getString(R.string.URL_getAllClubs);
        StringRequest stringRequest = updateClubs(url);
        queue.add(stringRequest);
    }

    public void updateFromMaps(){
        //Récupération des clubs de golf
        Log.d("DEBUG", "UPDATE REPO CLUBS");
        mHeaders = new HashMap<String, String>();
        mHeaders.put("X-API-KEY", activityContext.getResources().getString(R.string.API_KEY));
        mHeaders.put("CONTENT-LANGUAGE", activityContext.getResources().getString(R.string.CONTENT_LANGUAGE));
        RequestQueue queue = Volley.newRequestQueue(activityContext);
        String url = activityContext.getResources().getString(R.string.URL_getAllClubs);
        StringRequest stringRequest = updateClubs(url);
        queue.add(stringRequest);
    }

    @NonNull
    private StringRequest updateClubs(String url) {

        return new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("DEBUG", "réponse webservice clubs : "+response);

                    try {
                        clubsData = new ClubsData(new JSONObject(response));
                        if (fragmentContext != null) {
                            fragmentContext.handleSuccess();
                        }
                        if (clubListContext != null) {
                            clubListContext.updateclubs();
                        }
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
                    if (fragmentContext != null) {
                        fragmentContext.handleError(messageErreur.getString("error_message"));
                    }
                    if (clubListContext != null) {
                        clubListContext.handleError(messageErreur.getString("error_message"));
                    }
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
    }


}
