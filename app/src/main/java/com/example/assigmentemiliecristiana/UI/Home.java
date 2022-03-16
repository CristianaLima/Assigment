package com.example.assigmentemiliecristiana.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assigmentemiliecristiana.R;

import java.util.ArrayList;
import java.util.Calendar;

public class Home extends AppCompatActivity {

    ScrollView scrollView;
    TextView date_view;
    TextView homepage_date;
    ListView listView;
    DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageButton calendar_button = (ImageButton) findViewById(R.id.date_button);
        ImageButton profile_button = (ImageButton) findViewById(R.id.profile_button);
        ImageButton add_assignment = (ImageButton) findViewById(R.id.add_assingment);
        date_view =(TextView) findViewById(R.id.date_view);
        listView = (ListView) findViewById(R.id.home_list);


        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Android studio home page");
        arrayList.add("Decorator pattern exo 2");
        arrayList.add("Go to the gym");

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

        add_assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, AddAssignment.class));

            }
        });

        profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, MyProfile.class));

            }
        });



        calendar_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            Home.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, year, month, day);
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
                homepage_date.setText(date);
            }
        };

    }
/*
    public boolean onCreateOptionMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }*/

}