package com.example.assigmentemiliecristiana.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assigmentemiliecristiana.R;
import com.example.assigmentemiliecristiana.adapter.RecyclerAdapter;
import com.example.assigmentemiliecristiana.database.entity.AssignmentEntity;
import com.example.assigmentemiliecristiana.util.RecyclerViewItemClickListener;
import com.example.assigmentemiliecristiana.viewmodel.assignment.AssignmentListDateViewModel;
import com.example.assigmentemiliecristiana.viewmodel.assignment.AssignmentListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Home extends AppCompatActivity {

    public static final String PREFS_NAME = "SharefPrefs";
    public static final String PREFS_USER = "LoggedIn";
    private static final String TAG = "AssigmentActivity";
    private List<AssignmentEntity> assignments;
    private AssignmentListViewModel viewModel;
    private AssignmentListDateViewModel dateViewModel;

    Button showAll;
    TextView date_view;
    TextView homepage_date;
    DatePickerDialog.OnDateSetListener setListener;

    private RecyclerAdapter <AssignmentEntity> adapter;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        showAll = findViewById(R.id.show_all);
        homepage_date = findViewById(R.id.homepage_date);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.home_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

       SharedPreferences settings = getSharedPreferences(Home.PREFS_NAME, 0);
       String user = settings.getString(Home.PREFS_USER,null);

        ImageButton calendar_button = (ImageButton) findViewById(R.id.date_button);
        FloatingActionButton profile_button = (FloatingActionButton) findViewById(R.id.profile_button);
        FloatingActionButton add_assignment = (FloatingActionButton) findViewById(R.id.add_assingment);
        date_view =(TextView) findViewById(R.id.date_view);

        assignments = new ArrayList<>();
        adapter = new RecyclerAdapter<>(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d(TAG, "Clicked position " +position);
                Log.d(TAG, "Clicked on "+assignments.get(position).getName());

                Intent intent = new Intent(Home.this, AssignmentDescr.class);
                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                Intent.FLAG_ACTIVITY_NO_HISTORY
                );
                intent.putExtra("assignmentId", assignments.get(position).getId());
                startActivity(intent);
            }
        });
        AssignmentListViewModel.Factory factory = new AssignmentListViewModel.Factory(getApplication(),user);
        viewModel = new ViewModelProvider(this,factory).get(AssignmentListViewModel.class);

        viewModel.getOwnAssignments().observe(this,assignmentEntities -> {
            if(assignmentEntities!=null){
                assignments=assignmentEntities;
                adapter.setData(assignments);
            }
        });

        recyclerView.setAdapter(adapter);

       /* ArrayList<String> arrayList = new ArrayList<String>();


        arrayList.add("Android studio home page");
        arrayList.add("Decorator pattern exo 2");
        arrayList.add("Go to the gym");*/

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        add_assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, CreateAssignment.class));
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
                Long date = new Date(year,month,day).getTime();
                String dateS = day + "/" + month + "/" + year;

                homepage_date.setText(dateS);

                AssignmentListDateViewModel.Factory factory = new AssignmentListDateViewModel.Factory(getApplication(),user,date);
                dateViewModel = new ViewModelProvider(Home.this,factory).get(AssignmentListDateViewModel.class);

                dateViewModel.getDateAssignments().observe(Home.this,assignmentEntities -> {
                    if(assignmentEntities!=null){
                        assignments=assignmentEntities;
                        adapter.setData(assignments);
                    }
                });

                recyclerView.setAdapter(adapter);
            }
        };

    }

}