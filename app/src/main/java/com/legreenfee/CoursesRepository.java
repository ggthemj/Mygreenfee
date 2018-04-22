package com.legreenfee;

import android.text.Html;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CoursesRepository {
    //L'activité parente, appelée pour déclencher certaines méthodes selon les retours des WS
    BookingActivity bookingContext;
    ClubListActivity clubListContext;

    //Les paramètres de la requête http
    Map<String, String> mHeaders;
    Map<String, String> mParams;

    private Course[] courses;
    private TeeTime[] teeTimes;

    //Constructeur
    public CoursesRepository(final String lang, BookingActivity c){
        this.bookingContext = c ;
        mHeaders = new HashMap<String, String>();
        mHeaders.put("X-API-KEY", bookingContext.getResources().getString(R.string.API_KEY));
        mHeaders.put("CONTENT-LANGUAGE", lang);
    }

    public CoursesRepository(ClubListActivity clubListActivity) {
        clubListContext = clubListActivity;
    }

    public void update(final String lan, final ClubData club){
        Log.d("DEBUG", "Debut de la requete de création de parcours");

        //Préparation de la requête
        RequestQueue queue = Volley.newRequestQueue(this.bookingContext);
        String url = bookingContext.getResources().getString(R.string.URL_courses) + "&data%5Bclub_id%5D=" + club.public_id;

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
                            if (getCourses().length != 0) {
                                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                Calendar rightNow = Calendar.getInstance();
                                if (rightNow.get(Calendar.HOUR_OF_DAY) > 16) {
                                    rightNow.add(Calendar.DATE, 1);
                                }
                                Date today = rightNow.getTime();
                                updateTeeTimes(lan, club.public_id, df.format(today), "0");
                                bookingContext.setClubId(club.public_id);
                            }
                            bookingContext.updateCourses();
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

    public void updateTeeTimes(final String lan, int clubId, String date, String teeId){
        Log.d("DEBUG", "Debut de la requete de récupération des tee times");

        //Préparation de la requête
        RequestQueue queue = Volley.newRequestQueue(this.bookingContext);
        String stringParams = "&data%5Bclub_id%5D=" + clubId;
        stringParams += "&data%5Bdate%5D=" + date;
        if (teeId != null && !"".equals(teeId) && !"0".equals(teeId)) {
            stringParams += "&data%5Btee_id%5D=" + teeId;
        }
        String url = bookingContext.getResources().getString(R.string.URL_teetimes) + stringParams;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("DEBUG", "response : "+response);
                        try {
                            JSONObject json = new JSONObject(response);
                            JSONArray reponseJSON = json.getJSONArray("teetimes");
                            setTeeTimes(new TeeTime[reponseJSON.length()]);
                            for (int i = 0 ; i < reponseJSON.length() ; i++){
                                getTeeTimes()[i] = new TeeTime(reponseJSON.getJSONObject(i));
                                Log.d("DEBUG","TeeTime " + getTeeTimes()[i].getTee_public_id() + " ajouté");
                            }
                            bookingContext.updateTeeTimes();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setTeeTimes(new TeeTime[0]);
                Log.d("DEBUG","That didn't work! " + error.getMessage());
            }
        }) {
            @Override
            //protected Map<String, String> getParams() {
            //    return mParams;
            //}
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

    public void updateAd(final String lan, final String courseId, int clubId) {
        Log.d("DEBUG", "Debut de la requete de récupération des ads");

        //Préparation de la requête
        RequestQueue queue = Volley.newRequestQueue(this.bookingContext);
        String stringParams = "/" + clubId + "/ad";
        stringParams += "&data%5Bcourse_id%5D=" + courseId;


        String url = bookingContext.getResources().getString(R.string.URL_advertisement) + stringParams;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d("DEBUG", "response : "+response);
                        try {
                            JSONObject json = new JSONObject(response);

                            String ad = json.getString("ad");

                            bookingContext.updateAd(Html.fromHtml(ad).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setTeeTimes(new TeeTime[0]);
                Log.d("DEBUG","That didn't work! " + error.getMessage());
            }
        }) {
            @Override
            //protected Map<String, String> getParams() {
            //    return mParams;
            //}
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

    public TeeTime[] getTeeTimes() {
        return teeTimes;
    }

    public void setTeeTimes(TeeTime[] teeTimes) {
        this.teeTimes = teeTimes;
    }
}
