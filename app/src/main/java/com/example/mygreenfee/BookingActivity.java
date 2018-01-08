package com.example.mygreenfee;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class BookingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    private TextView titleView;
    private TextView dateView;
    private Calendar calendar;
    private Spinner coursesSpinner;
    private int year, month, day;
    private CoursesRepository coursesRepo;
    private Button minusButton;
    private Button plusButton;
    private TextView playerView;
    private int nbPlayers = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // Récupération du club séletionné
        Intent intent = getIntent();
        ClubData club = (ClubData) intent.getParcelableExtra("currentClub");

        // Récupération des parcours du club
        this.coursesRepo = new CoursesRepository(this);
        coursesRepo.update(club);

        // Titre de l'écran
        titleView = (TextView) findViewById(R.id.bookingTitleView);
        titleView.setText(club.name);

        // Calendar
        dateView = (TextView) findViewById(R.id.bookingDateView);
        setDateView(Calendar.getInstance());

        // Courses
        coursesSpinner = (Spinner) findViewById(R.id.bookingCoursesSpinner);

        // number of players
        plusButton = (Button) findViewById(R.id.bookingPlusButton);
        minusButton = (Button) findViewById(R.id.bookingMinusButton);
        playerView = (TextView) findViewById(R.id.bookingPlayerView);


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);
        setDateView(cal);
    }

    private void setDateView(final Calendar calendar) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String today = "";
        Calendar currentCal = Calendar.getInstance();
        if (calendar.get(Calendar.YEAR) == currentCal.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == currentCal.get(Calendar.DAY_OF_YEAR)) {
            today = " - " + getResources().getString(R.string.today);
        }
        dateView.setText(dateFormat.format(calendar.getTime()) + today);

    }

    public void addPlayer(View view)
    {
        if (nbPlayers > 3) {
            nbPlayers++;
        }
        else {
            nbPlayers = 1;
        }
        //playerView.setText(R.plurals.);

    }

    public void removePlayer(View view)
    {
        nbPlayers--;
    }

    public void datePicker(View view){
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "date");
    }

    public void updateCourses() {
        List<String> courses = new ArrayList<String>();

        for (Course course : coursesRepo.getCourses()) {
            courses.add(course.getName());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, courses);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coursesSpinner.setAdapter(dataAdapter);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
