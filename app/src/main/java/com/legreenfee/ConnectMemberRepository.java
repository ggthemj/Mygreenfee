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

public class ConnectMemberRepository {
    //L'activité parente, appelée pour déclencher certaines méthodes selon les retours des WS
    ConnectMemberFragment context;

    UserData userData ;

    //Les paramètres de la requête http
    Map<String, String> mHeaders;
    Map<String, String> mParams;

    //Constructeur
    public ConnectMemberRepository(ConnectMemberFragment c){
        this.context = c ;
    }

    //Tentative de login :)
    public void update(final String lan, final String email, final String pwd){
        Log.d("DEBUG", "Début de la requête login avec les identifiants "+email+"/"+pwd);

        //Préparation de la requête
        RequestQueue queue = Volley.newRequestQueue(context.getContext());
        String url = context.getResources().getString(R.string.URL_validateMember)+"&data[email]="+email+"&data[pwd]="+pwd;
        mHeaders = new HashMap<String, String>();
        mHeaders.put("X-API-KEY", context.getResources().getString(R.string.API_KEY));
        mHeaders.put("CONTENT-LANGUAGE", lan);
        mParams = new HashMap<String, String>();
        mParams.put("data[email]", email);
        mParams.put("data[pwd]", pwd);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
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

                return volleyError;
            }
        };
        queue.add(stringRequest);
    }

    public void updateMdpOubli(final String lan, final String ema){
        Log.d("DEBUG", "Debut de la requete de récupération du mot de passe");

        //Préparation de la requête
        RequestQueue queue = Volley.newRequestQueue(this.context.getContext());
        String url = context.getResources().getString(R.string.URL_oubliMdp)+"&data[email]="+ema;
        mHeaders = new HashMap<String, String>();
        mHeaders.put("X-API-KEY", context.getResources().getString(R.string.API_KEY));
        mHeaders.put("CONTENT-LANGUAGE", lan);
        mParams = new HashMap<String, String>();
        mParams.put("data[country]", ema);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        context.handleSuccessMdp();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DEBUG","That didn't work! "+error.getMessage());
                try {
                    JSONObject messageErreur = new JSONObject(error.getMessage());
                    context.handleErrorMdp(messageErreur.getString("error_message"));
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


    public void book(final String lan, int clubId, String arg1, String arg2, int nbPlaces, final String date, String user_email, final String clubS, final String priceS) {
        Log.d("DEBUG", "Début de la requête book avec les identifiants "+clubId);

        RequestQueue queue = Volley.newRequestQueue(context.getContext());
        String url = context.getResources().getString(R.string.URL_order);

        mHeaders = new HashMap<String, String>();
        mHeaders.put("X-API-KEY", context.getResources().getString(R.string.API_KEY));
        mHeaders.put("CONTENT-LANGUAGE", lan);
        mParams = new HashMap<String, String>();
        mParams.put("data[club_id]", ""+clubId);
        mParams.put("data[tee_id]", arg1);
        mParams.put("data[date]", date);
        mParams.put("data[time]", arg2);
        mParams.put("data[nb_places]", ""+nbPlaces);
        mParams.put("data[email]", ""+user_email);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("DEBUG", "response : "+response);
                        try {
                            JSONObject json = new JSONObject(response);

                            String order_id = json.getString("order_id");
                            context.handleBookSuccess(order_id, clubS, priceS, date);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DEBUG","That didn't work! " + error.getMessage());
            }
        }) {
            @Override
            //protected Map<String, String> getParams() {
            //    return mParams;
            //}
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
