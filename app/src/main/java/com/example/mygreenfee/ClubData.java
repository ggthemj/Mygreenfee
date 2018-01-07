package com.example.mygreenfee;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

public class ClubData implements Parcelable{

    public int public_id;
    public String name;
    public String description;
    public String address;
    public String email;
    public String url;
    public double longitude;
    public double latitude;
    public String country_code;
    public String currency;
    public String image_url;
    public String rating;

    //Constructeur d'un club avec toutes les données nécessaires
    public ClubData(JSONObject json){
        try {
            if (json.has("public_id")) {
                this.public_id = json.getInt("public_id");
            }
            else{
                Log.d("DEBUG","Manque l'ID");
            }
            if (json.has("name")) {
                this.name = json.getString("name");
            }
            else{
                Log.d("DEBUG","Manque le name");
            }
            if (json.has("description")) {
                this.description = json.getString("description");
            }
            else{
                Log.d("DEBUG","Manque la description");
            }
            if (json.has("address")) {
                this.address = json.getString("address");
            }
            else{
                Log.d("DEBUG","Manque l'adresse");
            }
            if (json.has("email")) {
                this.email = json.getString("email");
            }
            else{
                Log.d("DEBUG","Manque l'email");
            }
            if (json.has("url")) {
                this.url = json.getString("url");
            }
            else{
                Log.d("DEBUG","Manque l'url");
            }
            if (json.has("longitude")) {
                this.longitude = json.getDouble("longitude");
            }
            else{
                Log.d("DEBUG","Manque l'ID");
            }
            if (json.has("latitude")) {
                this.latitude = json.getDouble("latitude");
            }
            else{
                Log.d("DEBUG","Manque la latitude");
            }
            if (json.has("country_code")) {
                this.country_code = json.getString("country_code");
            }
            else{
                Log.d("DEBUG","Manque le code country");
            }
            if (json.has("currency")) {
                this.currency = json.getString("currency");
            }
            else{
                Log.d("DEBUG","Manque la currency");
            }
            if (json.has("image_url")) {
                this.image_url = json.getString("image_url");
            }
            else{
                Log.d("DEBUG","Manque l'image url");
            }
            if (json.has("rating")) {
                this.rating = json.getString("rating");
            }
            else{
                Log.d("DEBUG","Manque le rating");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("DEBUG","J'ajoute le club : "+ this.name);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(public_id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(address);
        dest.writeString(email);
        dest.writeString(url);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeString(country_code);
        dest.writeString(currency);
        dest.writeString(image_url);
        dest.writeString(rating);
    }

    public static final Parcelable.Creator<ClubData> CREATOR = new Parcelable.Creator<ClubData>() {
        public ClubData createFromParcel(Parcel in) {
            return new ClubData(in);
        }

        public ClubData[] newArray(int size) {
            return new ClubData[size];
        }
    };

    private ClubData(Parcel in) {
        public_id = in.readInt();
        name = in.readString();
        description = in.readString();
        address = in.readString();
        email = in.readString();
        url = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
        country_code = in.readString();
        currency = in.readString();
        image_url = in.readString();
        rating = in.readString();
    }
}
