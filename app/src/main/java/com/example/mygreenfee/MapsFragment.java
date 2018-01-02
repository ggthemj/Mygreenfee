package com.example.mygreenfee;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class MapsFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback {
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    Location mLastKnownLocation;
    MapsFragmentRepository clubsRepo;

    public MapsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationPermissionGranted = false;

        this.clubsRepo = new MapsFragmentRepository((HomeMapsActivity)getActivity(), this);
        this.clubsRepo.update();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // Créé la vue et retourne une carte vide
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();
        return view ;
    }

    //Il faudra les surcharger pour traiter les cas d'erreurs -> JBU
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnected(Bundle connectionHint) {

    }

    // Méthode déclenchée automatiquement dès que la map est prête, c'est ici que tu peupleras avec les golfs je pense
    @Override
    public void onMapReady(GoogleMap map) {
        Log.d("DEBUG", "La map est prête, JBU le bizarre");
        mMap = map;
        updateLocationUI();
    }

    // Méthode qui checke si l'utilisateur a accepté ou pas d'être localisé (j'ai codé cette partie)
    private void getLocationPermission(){
        Log.d("DEBUG", "Je lance la requete de permission");
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("DEBUG", "Permission OK");
            mLocationPermissionGranted = true;
            updateLocationUI();
        } else {
            Log.d("DEBUG", "Permission KO");
            mLocationPermissionGranted = false;
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    1234);
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

    private void getDeviceLocation() {
        // a coder - méthode qui centrera la map sur la position de l'utilisateur
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
            case 1234: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("OnRequestPR", "Permission accordée !");
                    mLocationPermissionGranted = true;
                    updateLocationUI();
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

    public void handleError(String s){
        Toast toast = Toast.makeText(getActivity(), s, Toast.LENGTH_LONG);
        toast.show();
    }

    public void handleSuccess(){
        Toast toast = Toast.makeText(getActivity(), "Succès de la récupération des clubs :)", Toast.LENGTH_LONG);
        toast.show();
    }
}
