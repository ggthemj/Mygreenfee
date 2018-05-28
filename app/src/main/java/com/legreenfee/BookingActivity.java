package com.legreenfee;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class BookingActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener, View.OnClickListener {

    private TextView titleView;
    private TextView dateView;
    private Spinner coursesSpinner;
    private Spinner clubCardsSpinner;
    private TextView noTeeTimes;
    private CoursesRepository coursesRepo;
    private Button minusButton;
    private Button plusButton;
    private TextView playerView;
    private int nbPlayers = 1;
    private ListView teeTimesList;
    private RelativeLayout clubLayout;
    private ArrayAdapter<TeeTime> arrayAdapter;

    private int clubId;
    private String courseId;
    private List<String> teeID;
    private String dateSelected;
    private ClubData club;
    private Calendar calendarSelected;
    private PopupWindow popupWindow;
    TextView buttonInfoTerrain;
    ProgressBar progressBar;
    private TextView tv;
    private boolean popHasTobeCreated = true;
    private int clubCardId;
    private ClubCard clubCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
        String lang = sharedPref.getString("language", "EN");

        // Récupération du club séletionné
        Intent intent = getIntent();
        club = (ClubData) intent.getParcelableExtra("currentClub");

        if(club!=null) {
            // Récupération des parcours du club
            this.coursesRepo = new CoursesRepository(lang, this);
            coursesRepo.update(lang, club);
            coursesRepo.updateClubCards(lang, club);

            // Titre de l'écran
            titleView = (TextView) findViewById(R.id.bookingTitleView);
            titleView.setText(club.name);


            //Progress bar
            progressBar = (ProgressBar) findViewById(R.id.progressBarBooking);

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

            // Cards
            clubCardsSpinner = (Spinner) findViewById(R.id.bookingCardsSpinner);
            clubCardsSpinner.setOnItemSelectedListener(this);

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

            noTeeTimes = (TextView) findViewById(R.id.bookingTextNoTeetimes);
            noTeeTimes.setText(R.string.noTeeTimes);

            // Ecran principal
            clubLayout = (RelativeLayout) findViewById(R.id.constraintLayoutClub);

            buttonInfoTerrain = findViewById(R.id.buttonInfoTerrain);
            buttonInfoTerrain.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    popupWindow.showAtLocation(clubLayout, Gravity.CENTER, 0, 0);
                    buttonInfoTerrain.setVisibility(View.GONE);
                }
            });
        }
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

        SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
        String lang = sharedPref.getString("language", "EN");

        progressBar.setVisibility(View.VISIBLE);
        if (arrayAdapter != null) {
            arrayAdapter.clear();
            arrayAdapter.notifyDataSetChanged();
        }
        coursesRepo.updateAd(lang, courseId, getClubId(), dateFormat2.format(calendar.getTime()));
        coursesRepo.updateTeeTimes(lang, clubId, dateFormat2.format(calendar.getTime()));

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
        if (arrayAdapter != null) {
            arrayAdapter.notifyDataSetChanged();
        }

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
        if (arrayAdapter != null) {
            arrayAdapter.notifyDataSetChanged();
        }
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
        SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
        String lang = sharedPref.getString("language", "EN");
        final DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

        int i = 0;
        for (Course course : coursesRepo.getCourses()) {
            if (course.getTees() != null) {
                if (i == 0) {
                    coursesRepo.updateAd(lang, String.valueOf(course.getPublic_id()), getClubId(), dateFormat2.format(calendarSelected.getTime()));
                    i = 1;
                }

                List<String> ids = new ArrayList<String>();
                for (Tee tee : course.getTees()) {
                    ids.add(tee.getPublic_id());
                }
                courses.add(new TeeSpinnerDTO(course.getPublic_id(), ids, course.getName()));
            }
        }
        ArrayAdapter<TeeSpinnerDTO> dataAdapter = new ArrayAdapter<TeeSpinnerDTO>(this, R.layout.spinner_item_booking, courses);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        coursesSpinner.setAdapter(dataAdapter);



    }
    public void updateClubCard() {
        SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
        String lang = sharedPref.getString("language", "EN");
        ClubCard[] cc = new ClubCard[1];
        cc[0] = new ClubCard();
        cc[0].setDiscount(0);
        cc[0].setCoursesId(new ArrayList<Integer>());
        cc[0].setName("" + getResources().getText(R.string.clubcard));

        int aLen = cc.length;
        int bLen = coursesRepo.getClubCards().length;

        @SuppressWarnings("unchecked")
        ClubCard[] c = (ClubCard[]) Array.newInstance(cc.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(cc, 0, c, 0, aLen);
        System.arraycopy(coursesRepo.getClubCards(), 0, c, aLen, bLen);

        ArrayAdapter<ClubCard> dataAdapter = new ArrayAdapter<ClubCard>(this, R.layout.spinner_item_booking, c);


        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        clubCardsSpinner.setAdapter(dataAdapter);

    }


    public void updateTeeTimes() {
        TeeTime[] teeTimeArray = coursesRepo.getTeeTimes();
        if (teeTimeArray.length == 0) {
            noTeeTimes.setVisibility(View.VISIBLE);
            teeTimesList.setVisibility(View.GONE);
        }
        else {
            noTeeTimes.setVisibility(View.GONE);
            teeTimesList.setVisibility(View.VISIBLE);
        }
        if (arrayAdapter != null) {
            arrayAdapter.clear();
            //for (TeeTime tt : teeTimeArray) {
            //    arrayAdapter.add(tt.getSlots_free() + " " + getResources().getText(R.string.slotsFree) + tt.getTime() + " " + tt.getSale_price());
            //
            // }
            for (TeeTime teeTime : teeTimeArray) {
                for (String id : teeID) {
                    if (teeTime.getTee_public_id().equals(id)) {
                        arrayAdapter.add(teeTime);
                        break;
                    }
                }
            }
            arrayAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        }

        /*final TextView buttonInfoTerrain = findViewById(R.id.buttonInfoTerrain);
        buttonInfoTerrain.setVisibility(View.VISIBLE);*/
    }

    public void updateAd(String ad) {

        if (!"".equals(ad)) {
            if (popHasTobeCreated) {
                initPopUp(ad);
                popHasTobeCreated = false;
            } else {
                tv.setText(ad);
                popupWindow.showAtLocation(clubLayout, Gravity.CENTER, 0, 0);
                buttonInfoTerrain.setVisibility(View.GONE);
            }
        }
        else {
            if (!popHasTobeCreated) {
                popupWindow.dismiss();
                buttonInfoTerrain.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spinner = (Spinner) parent;
        if(spinner.getId() == R.id.bookingCoursesSpinner)
        {
            progressBar.setVisibility(View.VISIBLE);
            if (arrayAdapter != null) {
                arrayAdapter.clear();
                arrayAdapter.notifyDataSetChanged();
            }
            TeeSpinnerDTO item = (TeeSpinnerDTO) parent.getItemAtPosition(position);
            //User user = (User) ( (Spinner) findViewById(R.id.user) ).getSelectedItem();
            teeID = item.getId();
            courseId = item.getCourseId();
            final DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

            SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
            String lang = sharedPref.getString("language", "EN");

            coursesRepo.updateTeeTimes(lang, getClubId(), dateFormat2.format(calendarSelected.getTime()));
            coursesRepo.updateAd(lang, courseId, getClubId(), dateFormat2.format(calendarSelected.getTime()));
        }
        else if (spinner.getId() == R.id.bookingCardsSpinner)
        {
            clubCard = (ClubCard) parent.getItemAtPosition(position);
            clubCardId = clubCard.getPublic_id();
            if (arrayAdapter != null) {
                arrayAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void initPopUp(String text) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.custom_pop_dialog, null);
        popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        //popupWindow = new PopupWindow(this);

        popupWindow.setContentView(customView);
        popupWindow.setOutsideTouchable(true);
        //popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#80000000")));

        tv = (TextView) customView.findViewById(R.id.club_info);
        Button button = customView.findViewById(R.id.popup_info_close);
        button.setOnClickListener(this);

        Button button2 = customView.findViewById(R.id.button_accepter);
        button2.setOnClickListener(this);

        tv.setText(text);
        teeTimesList.post(new Runnable() {
            public void run() {
                popupWindow.showAtLocation(clubLayout, Gravity.CENTER, 0, 0);
            }
        });

    }

    @Override
    public void onClick(View v) {
        popupWindow.dismiss();
        buttonInfoTerrain.setVisibility(View.VISIBLE);
    }

    public void book(String orderId, String orderClub, String orderPrice, String orderDate) {
        SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("order_price", orderPrice);
        editor.putString("order_club", orderClub);
        editor.putString("order_date", orderDate);
        editor.putString("order_parcours", "Parcours");
        editor.commit();


        Intent intent = new Intent(getApplicationContext(), OrderActivity.class);
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

    public List<String> getTeeID() {
        return teeID;
    }

    public void setTeeID(List<String> teeID) {
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

    public ClubData getClub() {
        return club;
    }

    public void setClub(ClubData club) {
        this.club = club;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public int getClubCardId() {
        return clubCardId;
    }

    public void setClubCardId(int clubCardId) {
        this.clubCardId = clubCardId;
    }

    public ClubCard getClubCard() {
        return clubCard;
    }

    public void setClubCard(ClubCard clubCard) {
        this.clubCard = clubCard;
    }

    public void addCourse(TeeSpinnerDTO teeSpinnerDTO) {
    }
}
