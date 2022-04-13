package com.example.assigmentemiliecristiana.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assigmentemiliecristiana.R;
import com.example.assigmentemiliecristiana.UI.Assignment.AssignmentDescr;
import com.example.assigmentemiliecristiana.UI.Assignment.CreateAssignment;
import com.example.assigmentemiliecristiana.UI.Profile.MyProfile;
import com.example.assigmentemiliecristiana.adapter.RecyclerAdapter;
import com.example.assigmentemiliecristiana.database.entity.AssignmentEntity;
import com.example.assigmentemiliecristiana.util.RecyclerViewItemClickListener;
import com.example.assigmentemiliecristiana.viewmodel.assignment.AssignmentListDateViewModel;
import com.example.assigmentemiliecristiana.viewmodel.assignment.AssignmentListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * this is the page after log in
 */
public class Home extends AppCompatActivity {

    public static final String PREFS_NAME = "SharefPrefs";
    public static final String PREFS_EMAIL = "LoggedIn";
    private static final String TAG = "AssigmentActivity";
    private List<AssignmentEntity> assignments;
    private AssignmentListViewModel viewModel;
    private AssignmentListDateViewModel dateViewModel;
    private RecyclerView recyclerView;
    private String email;

    private Button showAll;
    private TextView date_view;
    private TextView homepage_date;
    private DatePickerDialog.OnDateSetListener setListener;

    private RecyclerAdapter<AssignmentEntity> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //link this activity with the layout
        setContentView(R.layout.home);

        //use ActionBar utility methods
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("AssignmentApp");

        // methods to display the icon in the ActionBar
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //link the variables in this activity with that in the layout
        showAll = findViewById(R.id.show_all);
        date_view = findViewById(R.id.show_all);
        homepage_date = findViewById(R.id.homepage_date);
        recyclerView = findViewById(R.id.home_list);
        //create and put the LayoutManager in the RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //create and add the DividerItemDecoration to the RecyclerView
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        //get the username of the user
        email = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //link the buttons in this activity with that in the layout
        ImageButton calendar_button =  findViewById(R.id.date_button);
        FloatingActionButton add_assignment = findViewById(R.id.add_assingment);

        //set the assignment list
        assignments = new ArrayList<>();
        //set the adapter to know what to do when you click to one of the assignments
        adapter = new RecyclerAdapter<>(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d(TAG, "Clicked position " + position);
                Log.d(TAG, "Clicked on " + assignments.get(position).getName());

                //go to AssignmentDescr activity with the assignment ID
                Intent intent = new Intent(Home.this, AssignmentDescr.class);
                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                Intent.FLAG_ACTIVITY_NO_HISTORY
                );
                intent.putExtra("assignmentId", assignments.get(position).getId());
                startActivity(intent);
            }
        });
        //call a methode which is below
        //it will go research the assignments from the database
        getInitialData();

        //create and set the calendar
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        //set what to do when you click on the more button
        add_assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to CreateAssignment with the username of the user
                Intent intent = new Intent(Home.this, CreateAssignment.class);
                startActivity(intent);
            }
        });

        //set what to do when you click on the showAll button
        showAll.setOnClickListener(view -> getInitialData());

        //set what to do when you click on the calendar button
        calendar_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                {
                    //will show you a DatePickerDialog which you need to choose a date
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            Home.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, year, month, day);
                    datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    datePickerDialog.show();

                }
            }
        });

        //set what to do when you choose a date
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //put more 1 to the month to have the date chosen
                month = month + 1;
                //put he date in a String and a Long
                Long date = new Date(year, month, day).getTime();
                String dateS = day + "/" + month + "/" + year;

                //to display the date
                homepage_date.setText(dateS);

                //go research all the assignments with the date chosen
                AssignmentListDateViewModel.Factory factory = new AssignmentListDateViewModel.Factory(getApplication(), email, date);
                dateViewModel = new ViewModelProvider(Home.this, factory).get(AssignmentListDateViewModel.class);

                dateViewModel.getDateAssignments().observe(Home.this, assignmentEntities -> {
                    if (assignmentEntities != null) {
                        assignments = assignmentEntities;
                        adapter.setData(assignments);
                    }
                });

                //display the assignments
                recyclerView.setAdapter(adapter);
            }
        };

    }

    /**
     * to get all the assignments of the user and put in the assignment list
     */
    private void getInitialData() {
        //go research all the assignments of the user
        AssignmentListViewModel.Factory factory = new AssignmentListViewModel.Factory(getApplication(), email);
        viewModel = new ViewModelProvider(this, factory).get(AssignmentListViewModel.class);

        viewModel.getOwnAssignments().observe(this, assignmentEntities -> {
            if (assignmentEntities != null) {
                assignments = assignmentEntities;
                adapter.setData(assignments);
            }
        });
        //display the assignments
        recyclerView.setAdapter(adapter);

        //to display nothing because you will see all the assignments not just the ones sort per date
        homepage_date.setText("");
    }

    /**
     * method to inflate the options menu when
     * the user opens the menu for the first time
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    /**
     * methods to control the operations that will
     * happen when user clicks on the action buttons
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.profile_taskbar:
                Intent intent = new Intent(Home.this, MyProfile.class);
                startActivity(intent);
                break;

            case R.id.about_taskbar:
                final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("About");
                alertDialog.setCancelable(false);
                alertDialog.setMessage("This project has been done to understand how Android Studio work and to implement the different architectures patterns that we have learned.");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Ok",(dialog, which)->alertDialog.dismiss());
                alertDialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
