package com.example.mygreenfee;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class BookingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    private TextView titleView;
    private TextView dateView;
    private Spinner coursesSpinner;
    private CoursesRepository coursesRepo;
    private Button minusButton;
    private Button plusButton;
    private TextView playerView;
    private int nbPlayers = 1;
    private ListView teeTimesList;
    private ArrayAdapter<TeeTime> arrayAdapter;

    private int clubId;
    private String teeID;
    private String dateSelected;
    private ClubData club;
    private Calendar calendarSelected;
    public String clubSelected;
    public String priceSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // Récupération du club séletionné
        Intent intent = getIntent();
        club = (ClubData) intent.getParcelableExtra("currentClub");

        //showPopUp(club.description);

        // Récupération des parcours du club
        this.coursesRepo = new CoursesRepository(this);
        coursesRepo.update(club);

        // Titre de l'écran
        titleView = (TextView) findViewById(R.id.bookingTitleView);
        titleView.setText(club.name);
        clubSelected = club.name;

        // Calendar
        dateView = (TextView) findViewById(R.id.bookingDateView);
        Calendar rightNow = Calendar.getInstance();
        if (rightNow.get(Calendar.HOUR_OF_DAY) > 16) {
            rightNow.add(Calendar.DATE, 1);
        }
        setDateView(rightNow);

        // Courses
        coursesSpinner = (Spinner) findViewById(R.id.bookingCoursesSpinner);
        coursesSpinner.setOnItemSelectedListener(this);

        // number of players
        plusButton = (Button) findViewById(R.id.bookingPlusButton);
        minusButton = (Button) findViewById(R.id.bookingMinusButton);
        playerView = (TextView) findViewById(R.id.bookingPlayerView);

        // TeeTimes
        teeTimesList = (ListView) findViewById(R.id.bookingListView);
        arrayAdapter = new TeeTimesAdapter(this);
                /*new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                new ArrayList<String>());*/

        teeTimesList.setAdapter(arrayAdapter);

        final TextView buttonInfoTerrain = findViewById(R.id.buttonInfoTerrain);
        buttonInfoTerrain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showPopUp(club.description);
                buttonInfoTerrain.setVisibility(View.GONE);
            }
        });
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
        calendarSelected = calendar;
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String today = "";
        Calendar currentCal = Calendar.getInstance();
        if (calendar.get(Calendar.YEAR) == currentCal.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == currentCal.get(Calendar.DAY_OF_YEAR)) {
            today = " - " + getResources().getString(R.string.today);
        }

        dateView.setText(dateFormat.format(calendar.getTime()) + today);
        dateSelected = dateFormat.format(calendar.getTime());
        final DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

        coursesRepo.updateTeeTimes(new Course(), clubId, dateFormat2.format(calendar.getTime()), teeID);
        updateTeeTimes();
    }

    public void addPlayer(View view)
    {
        if (nbPlayers < 4) {
            nbPlayers++;
        }
        else {
            nbPlayers = 1;
        }
        setPlayerView();

    }

    public void removePlayer(View view)
    {
        if (nbPlayers > 1) {
            nbPlayers--;
        }
        else {
            nbPlayers = 4;
        }
        setPlayerView();
    }

    private void setPlayerView() {
        playerView.setText(getResources().getQuantityString(R.plurals.players, nbPlayers, nbPlayers));
    }

    public void datePicker(View view){
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "date");
    }

    public void updateCourses() {

        List<TeeSpinnerDTO> courses = new ArrayList<TeeSpinnerDTO>();

        for (Course course : coursesRepo.getCourses()) {
            if (course.getTees() != null) {
                for (Tee tee : course.getTees()) {
                    String fmt = getResources().getText(R.string.holes).toString();

                    courses.add(new TeeSpinnerDTO(tee.getPublic_id(), course.getName() + " " + MessageFormat.format(fmt, course.getLength()) + " - " + tee.getName()));
                }
            }
            else {
                courses.add(new TeeSpinnerDTO("0", course.getName() + " - Tee 1"));
            }
        }
        ArrayAdapter<TeeSpinnerDTO> dataAdapter = new ArrayAdapter<TeeSpinnerDTO>(this, R.layout.spinner_item_booking, courses);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        coursesSpinner.setAdapter(dataAdapter);

    }

    public void updateTeeTimes() {
        TeeTime[] teeTimeArray = coursesRepo.getTeeTimes();
        if (arrayAdapter != null) {
            arrayAdapter.clear();
            //for (TeeTime tt : teeTimeArray) {
            //    arrayAdapter.add(tt.getSlots_free() + " " + getResources().getText(R.string.slotsFree) + tt.getTime() + " " + tt.getSale_price());
            //
            // }
            arrayAdapter.addAll(teeTimeArray);
            arrayAdapter.notifyDataSetChanged();
        }

        final TextView buttonInfoTerrain = findViewById(R.id.buttonInfoTerrain);
        buttonInfoTerrain.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TeeSpinnerDTO item = (TeeSpinnerDTO) parent.getItemAtPosition(position);
        //User user = (User) ( (Spinner) findViewById(R.id.user) ).getSelectedItem();
        teeID = item.getId();
        final DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

        coursesRepo.updateTeeTimes(new Course(), getClubId(), dateFormat2.format(calendarSelected.getTime()), item.getId());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void showPopUp(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(text)
                .setCancelable(true)
                .setTitle(R.string.info_title);

        /*LayoutInflater layoutInflater = getLayoutInflater();

        View customView = layoutInflater.inflate(R.layout.custom_pop_dialog, null);

        TextView tv = (TextView) customView.findViewById(R.id.text_info);
        tv.setText(text);

        builder.setView(customView);*/
        AlertDialog alert = builder.create();
        alert.getWindow().setLayout(600, 400);
        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(final DialogInterface arg0) {
                final TextView buttonInfoTerrain = findViewById(R.id.buttonInfoTerrain);
                buttonInfoTerrain.setVisibility(View.VISIBLE);
            }
        });
        builder.show();

    }

    public void book(String orderId, String orderClub, String orderPrice, String orderDate) {
        SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.order_id), orderId);
        editor.putString("order_price", orderPrice);
        editor.putString("order_club", orderClub);
        editor.putString("order_date", orderDate);
        editor.commit();


        Intent intent = new Intent(getApplicationContext(), MyBankingCardActivity.class);
        startActivity(intent);
    }

    public int getClubId() {
        return clubId;
    }

    public void setClubId(int clubId) {
        this.clubId = clubId;
    }

    public String getDateSelected() {
        return dateSelected;
    }

    public void setDateSelected(String dateSelected) {
        this.dateSelected = dateSelected;
    }

    public String getTeeID() {
        return teeID;
    }

    public void setTeeID(String teeID) {
        this.teeID = teeID;
    }

    public int getNbPlayers() {
        return nbPlayers;
    }

    public void setNbPlayers(int nbPlayers) {
        this.nbPlayers = nbPlayers;
    }

    public Calendar getCalendarSelected() {
        return calendarSelected;
    }

    public void setCalendarSelected(Calendar calendarSelected) {
        this.calendarSelected = calendarSelected;
    }
}
