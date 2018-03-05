package com.example.mygreenfee;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;

import static android.location.LocationManager.GPS_PROVIDER;

public class MapsFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback, LocationListener, ActivityCompat.OnRequestPermissionsResultCallback  {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 1234;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    private FusedLocationProviderClient mFusedLocationClient;
    Location mLastKnownLocation;
    MapsFragmentRepository clubsRepo;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    Marker mCurrLocationMarker;
    private HashMap<String, ClubData> clubMarkers = new HashMap<String, ClubData>();
    FloatingActionButton fabClubResa;
    FloatingActionButton fabClubInfo;


    public static final String TAG = MapsFragment.class.getSimpleName();
    private ClubData currentClub;
    private LatLng myPosition;
    private TextView clubNameView;
    private CoordinatorLayout clubItems;

    public MapsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mLocationPermissionGranted = false;

        SharedPreferences sharedPref = getContext().getSharedPreferences("appData", Context.MODE_PRIVATE);
        String lang = sharedPref.getString("language", "EN");

        this.clubsRepo = new MapsFragmentRepository((HomeMapsActivity) getActivity(), this);
        this.clubsRepo.updateFromMaps(lang);
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

        destroyGoogleAPIClient();
    }

    @Override
    public void onStop(){
        super.onStop();
        destroyGoogleAPIClient();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Créé la vue et retourne une carte vide
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        if(getContext() instanceof HomeMapsActivity){
            SharedPreferences sharedPref = getContext().getSharedPreferences("appData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("home_state", "1");
            editor.commit();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        clubItems = view.findViewById(R.id.maps_club_items);

        clubNameView = view.findViewById(R.id.maps_club_name);

        fabClubResa = view.findViewById(R.id.fabClubResa);
        fabClubResa.setVisibility(View.INVISIBLE);
        fabClubResa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BookingActivity.class);
                intent.putExtra("currentClub", currentClub);
                startActivity(intent);
            }
        });

        fabClubInfo = view.findViewById(R.id.fabClubInfo);
        fabClubInfo.setVisibility(View.INVISIBLE);
        fabClubInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ClubActivity.class);
                intent.putExtra("currentClub", currentClub);
                startActivity(intent);
            }
        });

        return view;
    }

    private void addMarkers() {
        if (clubsRepo.clubsData != null && clubsRepo.clubsData.clubsdata != null && mMap != null) {
            for (ClubData club : clubsRepo.clubsData.clubsdata) {
                Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(club.latitude, club.longitude)).title(club.name).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin)));
                clubMarkers.put(marker.getId(), club);
            }
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            handleError("location requested");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
        }

    }

    // Méthode déclenchée automatiquement dès que la map est prête, c'est ici que tu peupleras avec les golfs je pense
    @Override
    public void onMapReady(GoogleMap map) {

        mMap = map;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mLocationPermissionGranted = true;
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                currentClub = clubMarkers.get(marker.getId());
                clubNameView.setText(currentClub.name);
                clubItems.setVisibility(View.VISIBLE);
                fabClubResa.setVisibility(View.VISIBLE);
                fabClubInfo.setVisibility(View.VISIBLE);
                return true;
            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                clubItems.setVisibility(View.INVISIBLE);
                fabClubResa.setVisibility(View.INVISIBLE);
                fabClubInfo.setVisibility(View.INVISIBLE);
            }
        });

        updateLocationUI();
        addMarkers();

    }

    @Override
    public void onLocationChanged(Location location)
    {
        handleError("location changed");
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        myPosition = new LatLng(location.getLatitude(), location.getLongitude());

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(myPosition).zoom(11f).build()));


    }

    // Méthode qui checke si l'utilisateur a accepté ou pas d'être localisé
    private void getLocationPermission(){
        Log.d("DEBUG", "Je lance la requete de permission");
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("DEBUG", "Permission OK");
            mLocationPermissionGranted = true;
            updateLocationUI();
            getDeviceLocation();
        } else {
            Log.d("DEBUG", "Permission KO");
            mLocationPermissionGranted = false;
            this.requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            Log.d("DEBUG", "Autorisation demandée");
        }

        try{
            if (mLocationPermissionGranted) {
                mLastKnownLocation = LocationServices.FusedLocationApi
                        .getLastLocation(mGoogleApiClient);
            }
        }
        catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        mFusedLocationClient.flushLocations();
    }

    // Callback quand l'utilisateur accepte ou refuse de se localiser - attention pour l'instant je
    // ne traite pas le cas ou il refuse !
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        Log.d("OnRequestPR", "J'entre dans la fonction");
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("OnRequestPR", "Permission accordée !");
                    mLocationPermissionGranted = true;
                    updateLocationUI();
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
            }
        }
        Log.d("OnRequestPR", "Statut de la permission"+mLocationPermissionGranted);
    }

    // Méthode appelée quand la map est affichée pour déterminer si l'utilisateur a activé ou pas
    // la géoloc sur son device
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                Log.d("UpdateLocation", "Permission ok");
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                getDeviceLocation();
            } else {
                Log.d("UpdateLocation", "Permission KO");
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    protected synchronized void buildGoogleApiClient() {
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
            mGoogleApiClient.connect();
        }
    }

    private void destroyGoogleAPIClient() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyGoogleAPIClient();
    }

    public void handleError(String s){
        //Toast toast = Toast.makeText(getActivity(), s, Toast.LENGTH_LONG);
        //toast.show();
    }

    public void handleSuccess(){
        //Toast toast = Toast.makeText(getActivity(), "Succès de la récupération des clubs :)", Toast.LENGTH_LONG);
        //toast.show();

        addMarkers();

    }

    public LatLng getMyPosition() {
        return myPosition;
    }

    public void setMyPosition(LatLng myPosition) {
        this.myPosition = myPosition;
    }
}
