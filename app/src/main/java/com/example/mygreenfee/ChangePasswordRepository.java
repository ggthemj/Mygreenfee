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

public class ChangePasswordRepository {
    //L'activité parente, appelée pour déclencher certaines méthodes selon les retours des WS
    ChangePasswordFragment context;

    //Les paramètres de la requête http
    Map<String, String> mHeaders;
    Map<String, String> mParams;

    //Constructeur
    public ChangePasswordRepository(ChangePasswordFragment c){
        this.context = c ;
    }

    //Tentative de login :)
    public void update(final String lan, final String oldmdp, final String newmdp, final String user_id){
        Log.d("DEBUG", "Début de la requête changepassword avec les identifiants "+oldmdp+"/"+newmdp);

        //Préparation de la requête
        RequestQueue queue = Volley.newRequestQueue(this.context.getContext());
        String url = context.getResources().getString(R.string.URL_changePassword);
        mHeaders = new HashMap<String, String>();
        mHeaders.put("X-API-KEY", context.getResources().getString(R.string.API_KEY));
        mHeaders.put("CONTENT-LANGUAGE", lan);
        mParams = new HashMap<String, String>();
        mParams.put("data[member_id]", user_id);
        mParams.put("data[old_pwd]", oldmdp);
        mParams.put("data[new_pwd]", newmdp);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("DEBUG", "réponse login : "+response);
                    context.handleSuccess();
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
