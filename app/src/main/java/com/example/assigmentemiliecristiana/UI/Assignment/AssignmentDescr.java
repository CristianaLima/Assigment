package com.example.assigmentemiliecristiana.UI.Assignment;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.assigmentemiliecristiana.R;
import com.example.assigmentemiliecristiana.UI.Home;
import com.example.assigmentemiliecristiana.UI.Profile.MyProfile;
import com.example.assigmentemiliecristiana.database.entity.AssignmentEntity;
import com.example.assigmentemiliecristiana.util.OnAsyncEventListener;
import com.example.assigmentemiliecristiana.viewmodel.assignment.AssignmentViewModel;

import java.util.Calendar;
import java.util.Date;

//this activity is for see the detail of an assignment
public class AssignmentDescr extends AppCompatActivity {
    private static final String TAG = "AssignmentDetailActivity";

    private AssignmentEntity assignment;
    private EditText descr;
    private EditText course;
    private EditText note;
    private TextView displayDate;
    private Spinner spinnerStatus;
    private Spinner spinnerType;
    private Button delete;
    private Button save;
    private ImageButton date;
    private String user;

    private Long dateL;
    private Long assignmentId;

    private AssignmentViewModel viewModel;
    DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //link this activity with the layout
        setContentView(R.layout.assignment_details);

        //use ActionBar utility methods
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("AssignmentApp");

        // methods to display the icon in the ActionBar
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //get the username of the user
        SharedPreferences settings = getSharedPreferences(Home.PREFS_NAME, 0);
        user = settings.getString(Home.PREFS_USER, null);

        //get the assignmentId of the assignment clicked
        assignmentId = getIntent().getLongExtra("assignmentId",0L);

        //link the variables in this activity with that in the layout
        descr = findViewById(R.id.description);
        course = findViewById(R.id.course);
        note = findViewById(R.id.note);
        delete = findViewById(R.id.delete_assignment);
        save = findViewById(R.id.save_assigment);
        date = findViewById(R.id.date_assignment);
        displayDate = findViewById(R.id.date_input);

        //set the status spinner
        spinnerStatus = findViewById(R.id.status);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_list, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerStatus.setAdapter(adapter);

        //set the type spinner
        spinnerType = findViewById(R.id.type);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.type_list, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter2);

        //go research the assignments clicked
        AssignmentViewModel.Factory factory = new AssignmentViewModel.Factory(
                getApplication(),assignmentId);
        viewModel = new ViewModelProvider(this,factory).get(AssignmentViewModel.class);
        viewModel.getAssignment().observe(this,assignmentEntity -> {
            if (assignmentEntity!= null){
                assignment = assignmentEntity;
                //It is a external method which is below
                updateContent();
            }
        });

        //set what to do when you click on the delete button
        //the deleteAssignment method is a external method which is below
        delete.setOnClickListener(view -> deleteAssignment());

        //set what to do when you click on the save button
        //the saveChange method is a external method which is below
        save.setOnClickListener(view -> saveChange());

        //set a calendar
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        //set what to do when you click on the calendar button
        date.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                {
                    //will show you a DatePickerDialog which you need to choose a date
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            AssignmentDescr.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, year, month, day);
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
                dateL = new Date(year,month,day).getTime();
                String dateS = day + "/" + month + "/" + year;

                //to display the date
                displayDate.setText(dateS);
                }
        };
    }
    //It will put the data to EditTexts
    private void updateContent(){
        //see if the assignment is not null
        if (assignment !=null){
            //put the data to EditTexts
            descr.setText(assignment.getName());
            course.setText(assignment.getCourse());
            note.setText(assignment.getDescription());

            //put the good status to the status spinner
            ArrayAdapter arrayAdapter = (ArrayAdapter) spinnerStatus.getAdapter();
            int idStatus = arrayAdapter.getPosition(assignment.getStatus());
            spinnerStatus.setSelection(idStatus);

            //put the good type to the type spinner
            ArrayAdapter arrayAdapter2 = (ArrayAdapter) spinnerType.getAdapter();
            int idType = arrayAdapter2.getPosition(assignment.getType());
            spinnerType.setSelection(idType);

            //pick the date, change it to String and put to the date ViewText
            dateL= assignment.getDate();
            Date date = new Date();
            date.setTime(assignment.getDate());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
             int year = calendar.get(Calendar.YEAR);
             int month = calendar.get(Calendar.MONTH);
             int day = calendar.get(Calendar.DAY_OF_MONTH);
            year = year-1900;
            String dateS = day + "/" + month + "/" + year;
            displayDate.setText(dateS);

            Log.i(TAG,"Activity populated.");

        }
    }

    //to save the change of the assignment
    private void saveChange(){
        //pick the new assignment
        String name = descr.getText().toString();
        String courseA = course.getText().toString();
        String description = note.getText().toString();
        String status = spinnerStatus.getSelectedItem().toString();
        String type = spinnerType.getSelectedItem().toString();

        //put it to the current assignment
        assignment.setName(name);
        assignment.setCourse(courseA);
        assignment.setDescription(description);
        assignment.setStatus(status);
        assignment.setType(type);
        assignment.setDate(dateL);

        //update the database
        AssignmentViewModel.Factory factory = new AssignmentViewModel.Factory(
                getApplication(),assignmentId);
        viewModel = new ViewModelProvider(this,factory).get(AssignmentViewModel.class);
        viewModel.updateAssignment(assignment, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG,"Modify assignment success");
                setResponse(true);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG,"Modify assignment fail");
                setResponse(false);
            }
        });

    }
    private void setResponse(Boolean response){
        if(response){
            //display a little message to say that the assignment is modified
            Toast toast = Toast.makeText(this,"Assignment modified",Toast.LENGTH_LONG);
            toast.show();
            //go to Home
            startActivity(new Intent(AssignmentDescr.this, Home.class));
        }
        else{
            //display a little message to say that the assignment isn't modified
            Toast toast = Toast.makeText(this,"Fail",Toast.LENGTH_LONG);
            toast.show();
        }
    }

    //delete the assignment from the database
    private void deleteAssignment(){
        AssignmentViewModel.Factory factory = new AssignmentViewModel.Factory(getApplication(),assignmentId);
        viewModel = new ViewModelProvider(this,factory).get(AssignmentViewModel.class);
        viewModel.deleteAssignment(assignment, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG,"Delete assignment success");
                setResponseD(true);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG,"Delete assignment fail");
                setResponseD(false);
            }
        });
    }
    private void setResponseD(Boolean response){
        if(response){
            //display a little message to say that the assignment is deleted
            Toast toast = Toast.makeText(this,"Assignment deleted",Toast.LENGTH_LONG);
            toast.show();
            //go to Home
            startActivity(new Intent(AssignmentDescr.this, Home.class));
        }
        else{
            //display a little message to say that the assignment isn't deleted
            Toast toast = Toast.makeText(this,"Fail",Toast.LENGTH_LONG);
            toast.show();
        }
    }

    // method to inflate the options menu when
    // the user opens the menu for the first time
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    // methods to control the operations that will
    // happen when user clicks on the action buttons
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.profile_taskbar:
                Intent intent = new Intent(AssignmentDescr.this, MyProfile.class);
                intent.putExtra("username", user);
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
