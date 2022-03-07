package com.example.assigmentemiliecristiana;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    ListView listView;

@Override
    protected void onCreate (Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    listView=(ListView) findViewById(R.id.home_list);

    ArrayList<String> arrayList = new ArrayList<String>();
    arrayList.add("Android studio home page");
    arrayList.add("Decorator pattern exo 2");
    arrayList.add("Go to the gym");

    ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList);
    listView.setAdapter(arrayAdapter);

}

}
