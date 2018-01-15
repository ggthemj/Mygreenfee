package com.example.mygreenfee;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddBankingCardActivity extends AppCompatActivity {

    AddBankingCardRepository addBankingCardRepository ;
    private Calendar dobCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_banking_card);

        //Mise en place de la custom app bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar2);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Bind des buttons avec les méthodes correspondantes
        final Button buttonValidation = findViewById(R.id.buttonvalidation);
        buttonValidation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleValidation();
            }
        });

        this.dobCalendar = Calendar.getInstance();

        final EditText edittext = findViewById(R.id.dateexpiration);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                dobCalendar.set(Calendar.YEAR, year);
                dobCalendar.set(Calendar.MONTH, monthOfYear);
                dobCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        //Listerner qui déclenche l'apparition du date picker
        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                /*
                DatePickerDialog dpd =  new DatePickerDialog(AddBankingCardActivity.this, date, dobCalendar
                        .get(Calendar.YEAR), dobCalendar.get(Calendar.MONTH),
                        dobCalendar.get(Calendar.DAY_OF_MONTH));
                try{
                    Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
                    for (Field datePickerDialogField : datePickerDialogFields) {
                        if (datePickerDialogField.getName().equals("mDatePicker")) {
                            datePickerDialogField.setAccessible(true);
                            DatePicker datePicker = (DatePicker) datePickerDialogField.get(dpd);
                            Field datePickerFields[] = datePickerDialogField.getType().getDeclaredFields();
                            for (Field datePickerField : datePickerFields) {
                                if ("mDayPicker".equals(datePickerField.getName())) {
                                    datePickerField.setAccessible(true);
                                    Object dayPicker = new Object();
                                    dayPicker = datePickerField.get(datePicker);
                                    ((View) dayPicker).setVisibility(View.GONE);
                                }
                            }
                        }

                    }
                }catch(Exception ex){
                }
                */

                
            }
        });
    }

    //Update le champ date de naissance en le mettant au format attendu par le WS
    private void updateLabel() {
        String myFormat = "MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        EditText edittext = findViewById(R.id.dateexpiration);
        edittext.setText(sdf.format(dobCalendar.getTime()));
    }

    public void handleSuccess(CardData carddata){
        Intent intent = new Intent(this, MyBankingCardActivity.class);
        startActivity(intent);
    }

    public void handleError(String s){
        Toast toast = Toast.makeText(this, s, Toast.LENGTH_LONG);
        toast.show();
    }

    public void handleValidation(){
        SharedPreferences sharedPref = getSharedPreferences("appData", Context.MODE_PRIVATE);
        String user_mail = sharedPref.getString("user_email", "false");
        String user_currency = "EUR";

        EditText editText = findViewById(R.id.numerocarte);
        String num = editText.getText().toString();

        editText = findViewById(R.id.dateexpiration);
        String dat = editText.getText().toString();

        editText = findViewById(R.id.code);
        String cod = editText.getText().toString();

        addBankingCardRepository.addCard(user_mail, dat, num, cod, user_currency);
    }
}
