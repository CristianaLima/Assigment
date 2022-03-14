package com.example.assigmentemiliecristiana.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.assigmentemiliecristiana.R;

import java.util.Calendar;

public class CalendarPage extends AppCompatActivity {

    CalendarView calendar;
    TextView date_view;
    TextView homepage_date;
    EditText chosen_date;
    ImageButton calendar_button;
    DatePickerDialog.OnDateSetListener setListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        date_view = findViewById(R.id.date_view);
        chosen_date = findViewById(R.id.chosen_date);
        homepage_date = findViewById(R.id.homepage_date);
        calendar_button = findViewById(R.id.date_button);

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



    }}