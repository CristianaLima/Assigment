package com.example.assigmentemiliecristiana.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assigmentemiliecristiana.R;
import com.example.assigmentemiliecristiana.adapter.RecyclerAdapter;
import com.example.assigmentemiliecristiana.database.entity.AssignmentEntity;
import com.example.assigmentemiliecristiana.database.repository.StudentRepository;
import com.example.assigmentemiliecristiana.viewmodel.assignment.AssignmentListViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Home extends AppCompatActivity {

    public static final String PREFS_NAME = "SharefPrefs";
    public static final String PREFS_USER = "LoggedIn";

    private static final String TAG = "AssigmentActivity";

    private List<AssignmentEntity> assignments;
    private AssignmentListViewModel viewModel;

    Toolbar toolbar;
    TextView date_view;
    TextView homepage_date;
    ListView listView;
    DatePickerDialog.OnDateSetListener setListener;

    private RecyclerAdapter <AssignmentEntity> adapter;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ListView listView = findViewById(R.id.home_list);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        SharedPreferences settings = getSharedPreferences(Home.PREFS_NAME, 0);
        String user = settings.getString(Home.PREFS_USER,null);

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

       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(Home.this, AssignmentDescr.class));
           }
       });

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

}