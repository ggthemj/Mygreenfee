package com.example.mygreenfee;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
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

public class OrderRepository {
    //L'activité parente, appelée pour déclencher certaines méthodes selon les retours des WS
    OrderActivity context;
    CardData cardData ;
    UserData userData ;

    //Les paramètres de la requête http
    Map<String, String> mHeaders;
    Map<String, String> mParams;

    //Constructeur
    public OrderRepository(OrderActivity c){
        this.context = c ;
    }

    //Tentative de login :)
    public void login(final String lan, final String email, final String pwd){
        Log.d("DEBUG", "Début de la requête login avec les identifiants "+email+"/"+pwd);

        //Préparation de la requête
        RequestQueue queue = Volley.newRequestQueue(context);
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
                            context.handleSuccessLogin(userData);
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
                    context.handleErrorLogin();
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

    //Tentative de login :)
    public void confirm(final String lan, final String id, final String email){

        Log.d("DEBUG", "Début de la requête "+id+"/"+email);
        //Préparation de la requête
        RequestQueue queue = Volley.newRequestQueue(this.context);
        String url = "https://dev.mygreenfee.fr/api.php?sid=orders/"+id+"/confirm";
        mHeaders = new HashMap<String, String>();
        mHeaders.put("X-API-KEY", context.getResources().getString(R.string.API_KEY));
        mHeaders.put("CONTENT-LANGUAGE", lan);
        mParams = new HashMap<String, String>();
        mParams.put("data[email]", email);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DEBUG", "réponse login : "+response);

                        context.handleConfirm();
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

    //Tentative de login :)
    public void pay(final String lan, final String id, final String email, final String csc){

        Log.d("DEBUG", "PAIEMENT URL : "+"https://dev.mygreenfee.fr/api.php?sid=orders/"+id+"/card");

        Log.d("DEBUG", "Paramètre data[email] : "+email);
        Log.d("DEBUG", "Paramètre data[csc] : "+csc);
        Log.d("DEBUG", "Paramètre data[return_url] : "+"http://legreenfee.framework.payment.callback");

        //Préparation de la requête
        RequestQueue queue = Volley.newRequestQueue(this.context);

        String url = "https://dev.mygreenfee.fr/api.php?sid=orders/"+id+"/card";
        mHeaders = new HashMap<String, String>();
        mHeaders.put("X-API-KEY", context.getResources().getString(R.string.API_KEY));
        mHeaders.put("CONTENT-LANGUAGE", lan);
        mParams = new HashMap<String, String>();
        mParams.put("data[email]", email);
        mParams.put("data[csc]", csc);
        mParams.put("data[return_url]", "http://legreenfee.framework.payment.callback");
        Log.d("DEBUG", "Début de la requête "+id+"/"+email);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DEBUG", "REPONSE PAIEMENT : "+response);
                        context.handleSuccessPayment();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DEBUG","Erreur !"+error.getMessage());
                try {
                    JSONObject messageErreur = new JSONObject(error.getMessage());
                    context.handleErrorPayment(messageErreur.getString("error_message"));
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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    public void book(final String lan, int clubId, String arg1, String arg2, int nbPlaces, final String date, String user_email, final String clubS, final String priceS) {
        Log.d("DEBUG", "Début de la requête book avec les identifiants "+clubId);

        RequestQueue queue = Volley.newRequestQueue(context);
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
                            context.handleBookSuccess(order_id);
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
