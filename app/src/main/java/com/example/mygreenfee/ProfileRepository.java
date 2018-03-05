package com.example.mygreenfee;

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

public class ProfileRepository {
    //L'activité parente, appelée pour déclencher certaines méthodes selon les retours des WS
    ProfileFragment context;

    UserData userData ;

    //Les paramètres de la requête http
    Map<String, String> mHeaders;
    Map<String, String> mParams;

    //Constructeur
    public ProfileRepository(ProfileFragment c){
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
}
