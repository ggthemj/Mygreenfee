package com.example.mygreenfee;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ClubListActivity extends AppCompatActivity {

    private MapsFragmentRepository clubsRepo;
    private ClubsAdapter arrayAdapter;
    private ListView clubsList;
    private SearchView searchView;
    private LatLng myPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_list);
        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("latitude", 0);
        double lng = intent.getDoubleExtra("longitude", 0);
        myPosition = new LatLng(lat, lng);

        clubsList = (ListView) findViewById(R.id.club_list_view);

        searchView = findViewById(R.id.club_list_search);

        arrayAdapter = new ClubsAdapter(getApplicationContext());
        arrayAdapter = new ClubsAdapter(getApplicationContext());
        clubsRepo = new MapsFragmentRepository(this);
        clubsRepo.updateFromSearch();

        clubsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClubData item = (ClubData) arrayAdapter.getItem(position);

                Intent intent = new Intent(getApplicationContext(), BookingActivity.class);
                intent.putExtra("currentClub", item);
                startActivity(intent);
            }

        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.filter(newText);
                return false;
            }
        });

    }

    public void updateclubs() {
        ClubData clubData[] = clubsRepo.clubsData.clubsdata;
        List<ClubData> listClub = Arrays.asList(clubData);
        if (arrayAdapter != null) {
            arrayAdapter.clear();
            final Location myLocation = new Location("my location");
            myLocation.setLatitude(myPosition.latitude);
            myLocation.setLongitude(myPosition.longitude);
            List<Location> locations = new ArrayList<Location>();
            for (ClubData cd : clubData) {
                Location i = new Location(cd.name);
                i.setLatitude(cd.latitude);
                i.setLongitude(cd.longitude);
                if (myLocation.getLongitude() == 0 && myLocation.getLatitude() == 0) {
                    cd.distance = 0;
                }
                else {
                    cd.distance = i.distanceTo(myLocation)/1000;
                }

                locations.add(i);
            }

            Collections.sort(listClub, new Comparator<ClubData>() {
                @Override
                public int compare(ClubData o1, ClubData o2) {
                    return (int) ((o1.distance - o2.distance) * 1000 );
                }
            });
            arrayAdapter.addAll(listClub);
            arrayAdapter.notifyDataSetChanged();
        }

        clubsList.setAdapter(arrayAdapter);
    }

    public void handleError(String error_message) {

    }
}
