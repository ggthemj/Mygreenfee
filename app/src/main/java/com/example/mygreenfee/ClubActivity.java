package com.example.mygreenfee;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ClubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        Intent intent = getIntent();
        final ClubData club = (ClubData) intent.getParcelableExtra("currentClub");

        TextView title = (TextView) findViewById(R.id.club_title);
        title.setText(club.name);

        TextView titleView = (TextView) findViewById(R.id.club_info);
        titleView.setText(club.description);

        TextView titleView2 = (TextView) findViewById(R.id.club_adresse);
        titleView2.setText(club.address);

        TextView titleView3 = (TextView) findViewById(R.id.club_internet);
        titleView3.setText(club.email);

        TextView titleView4 = (TextView) findViewById(R.id.club_contact);
        titleView4.setText(club.url);

        if (!"".equals(club.image_url)) {
            ImageView imageView = (ImageView) findViewById(R.id.club_app_bar_image);
            imageView.setImageURI(Uri.parse(club.image_url));
        }

        FloatingActionButton fabBook = findViewById(R.id.fab_book);
        fabBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BookingActivity.class);
                intent.putExtra("currentClub", club);
                startActivity(intent);
            }
        });

        //CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        //collapsingToolbarLayout.setTitle(club.name);
        //collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

    }
}
