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

public class AddBankingCardRepository {
    //L'activité parente, appelée pour déclencher certaines méthodes selon les retours des WS
    AddBankingCardFragment context;
    CardData cardData ;

    //Les paramètres de la requête http
    Map<String, String> mHeaders;
    Map<String, String> mParams;

    //Constructeur
    public AddBankingCardRepository(AddBankingCardFragment c){
        this.context = c ;
    }

    //Tentative de login :)
    public void addCard(final String lan, final String mail, final String expiration_date, final String card_number, final String cvx, final String currency){

        Log.d("DEBUG", "Début de la requête "+card_number+"/"+expiration_date);

        //Préparation de la requête
        RequestQueue queue = Volley.newRequestQueue(this.context.getContext());
        String url = context.getResources().getString(R.string.URL_addCard);
        mHeaders = new HashMap<String, String>();
        mHeaders.put("X-API-KEY", context.getResources().getString(R.string.API_KEY));
        mHeaders.put("CONTENT-LANGUAGE", lan);
        mParams = new HashMap<String, String>();
        mParams.put("data[email]", mail);
        mParams.put("data[expiration_date]", expiration_date);
        mParams.put("data[card_number]", card_number);
        mParams.put("data[card_cvx]", cvx);
        mParams.put("data[currency]", currency);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DEBUG", "réponse login : "+response);
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONObject json2 = json.getJSONObject("card");
                            cardData = new CardData(json2);
                            context.handleSuccess(cardData);
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
}
