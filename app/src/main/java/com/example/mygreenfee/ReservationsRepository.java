package com.example.mygreenfee;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReservationsRepository {
    //L'activité parente, appelée pour déclencher certaines méthodes selon les retours des WS
    ReservationsActivity context;

    //les données de l'utilisateur qui seront mises à jour
    ReservationData reservationData ;

    //Les paramètres de la requête http
    Map<String, String> mHeaders;
    Map<String, String> mParams;

    public ReservationsRepository(ReservationsActivity c){
        this.context = c ;
    }

    public void getReservations(final String member_id){
        Log.d("DEBUG", "Debut de la requete de récupération des régions");

        //Préparation de la requête
        RequestQueue queue = Volley.newRequestQueue(this.context);
        String url = context.getResources().getString(R.string.URL_getReservations)+"&data[member_id]="+member_id;
        mHeaders = new HashMap<String, String>();
        mHeaders.put("X-API-KEY", context.getResources().getString(R.string.API_KEY));
        mHeaders.put("CONTENT-LANGUAGE", context.getResources().getString(R.string.CONTENT_LANGUAGE));
        mParams = new HashMap<String, String>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DEBUG", "response : "+response);
                        try {
                            JSONObject rep = new JSONObject(response);
                            JSONArray open = rep.getJSONArray("open");
                            ReservationData[] reservationData = new ReservationData[open.length()] ;
                            for (int i = 0 ; i < open.length(); i++){
                                reservationData[i] = new ReservationData(open.getJSONObject(i));
                            }
                            JSONArray closed = rep.getJSONArray("open");
                            ReservationData[] reservationData2 = new ReservationData[closed.length()] ;
                            for (int i = 0 ; i < closed.length(); i++){
                                reservationData2[i] = new ReservationData(closed.getJSONObject(i));
                            }
                            context.handleSuccess(reservationData, reservationData2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DEBUG","That didn't work! "+error.getMessage());
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

                return volleyError;
            }
        };
        queue.add(stringRequest);
    }


}
