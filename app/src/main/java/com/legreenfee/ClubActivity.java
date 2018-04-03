package com.legreenfee;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

public class ClubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        Intent intent = getIntent();
        final ClubData club = (ClubData) intent.getParcelableExtra("currentClub");

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
            Picasso.with(this).load(club.image_url).into(imageView);
        }

        final Toolbar toolbar = findViewById(R.id.club_toolbar);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(club.name);
        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.whiteColor));
        collapsingToolbarLayout.setExpandedTitleTextColor(ContextCompat.getColorStateList(getApplicationContext(), R.color.whiteColor));
        collapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(getApplicationContext(), R.color.greenAlbaColor));

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
