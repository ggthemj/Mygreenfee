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

public class CoursesRepository {
    //L'activité parente, appelée pour déclencher certaines méthodes selon les retours des WS
    BookingActivity context;

    //Les paramètres de la requête http
    Map<String, String> mHeaders;
    HashMap<String, String> mParams;
    private Course[] courses;

    //Constructeur
    public CoursesRepository(BookingActivity c){
        this.context = c ;
    }

    public void update(final ClubData club){
        Log.d("DEBUG", "Debut de la requete de création de compte");

        //Préparation de la requête
        RequestQueue queue = Volley.newRequestQueue(this.context);
        String url = context.getResources().getString(R.string.URL_courses);
        mHeaders = new HashMap<String, String>();
        mHeaders.put("X-API-KEY", context.getResources().getString(R.string.API_KEY));
        mHeaders.put("CONTENT-LANGUAGE", context.getResources().getString(R.string.CONTENT_LANGUAGE));
        mParams = new HashMap<String, String>();
        mParams.put("data[club_id]", String.valueOf(club.public_id));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("DEBUG", "response : "+response);
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray reponseJSON = json.getJSONArray("courses");
                            setCourses(new Course[reponseJSON.length()]);
                            for (int i = 0 ; i < reponseJSON.length() ; i++){
                                getCourses()[i] = new Course(reponseJSON.getJSONObject(i));
                                Log.d("DEBUG","Course " + getCourses()[i].getName() + " ajouté");
                            }
                            context.updateCourses();
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
            protected Map<String, String> getParams() {
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

    public Course[] getCourses() {
        return courses;
    }

    public void setCourses(Course[] courses) {
        this.courses = courses;
    }
}
