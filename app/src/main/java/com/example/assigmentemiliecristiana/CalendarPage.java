package com.example.assigmentemiliecristiana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class CalendarPage extends AppCompatActivity {

    CalendarView calendar;
    TextView date_view;
    EditText chosen_date;
    DatePickerDialog.OnDateSetListener setListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        date_view = findViewById(R.id.date_view);
        chosen_date = findViewById(R.id.chosen_date);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        date_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            CalendarPage.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, year, month, day);
                    datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    datePickerDialog.show();
                }
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                date_view.setText(date);

            }
        };

        chosen_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CalendarPage.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        chosen_date.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();

            }
        });


        //---------- WORKING MAIS PAS OUF ------------------

  /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


        CalendarView calendarView = findViewById(R.id.calendar);
        date_view =(TextView) findViewById(R.id.date_view);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth)
            {
                String Date = dayOfMonth + "-" + (month+1)+ "-"+ year;

                date_view.setText(Date);

            }
        });*/
        // }
    }}