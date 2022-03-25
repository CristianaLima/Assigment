package com.example.assigmentemiliecristiana.UI.Assignment;

import android.app.DatePickerDialog;
import android.content.Intent;
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

/**
 * this activity is for create an assignment
 */
public class CreateAssignment extends AppCompatActivity {

    private static final String TAG = "CreateActivity";

    private Button btn;
    private TextView date;
    private EditText name;
    private EditText course;
    private EditText description;
    private Spinner type;
    private Spinner status;
    private String username;
    private DatePickerDialog.OnDateSetListener setListener;

    private AssignmentViewModel viewModel;
    private AssignmentEntity newAssignment = new AssignmentEntity();
    private Long dateL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //link this activity with the layout
        setContentView(R.layout.create_assigment);

        //use ActionBar utility methods
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("AssignmentApp");

        // methods to display the icon in the ActionBar
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //link the variables in this activity with that in the layout
        btn = findViewById(R.id.create_button);
        date = findViewById(R.id.due_date);
        name = findViewById(R.id.name);
        course = findViewById(R.id.course);
        description = findViewById(R.id.description);

        //go research the username of the user
        username = getIntent().getStringExtra("username");

        //set the status spinner
        status = findViewById(R.id.status);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_list, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        status.setAdapter(adapter);

        //set the type spinner
        type = findViewById(R.id.type);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.type_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                type.setAdapter(adapter2);

        //link the ImageButton in this activity with that in the layout
        ImageButton calendar_button = findViewById(R.id.create_date);

        //set a calendar
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        //set what to do when you click on the calendar button
        calendar_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                {
                    //will show you a DatePickerDialog which you need to choose a date
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            CreateAssignment.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, year, month, day);
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
                date.setText(dateS);
            }
        };
        //set the ViewModel
        AssignmentViewModel.Factory factory = new AssignmentViewModel.Factory(
                getApplication(), newAssignment.getId());
        viewModel = new ViewModelProvider(this,factory).get(AssignmentViewModel.class);

        //set what to do when you click on the create button
        //the createAssignment method is a external method which is below
        btn.setOnClickListener(view -> createAssignment());

    }

    /**
     * it will create the assignment
     */
    private void createAssignment(){
        //see if you put a name
        if (name.getText().toString().equals("")){
            name.setError("You need to put a name");
            name.requestFocus();
            return;
        }
        //see if you put a course
        if (course.getText().toString().equals("")){
            course.setError("You need to specify the course");
            course.requestFocus();
            return;
        }
        //see if you put a description
        if (description.getText().toString().equals("")){
            description.setError("You need to put a description");
            description.requestFocus();
            return;
        }
        //see if you choose a date
        if (dateL==null){
            date.setError("You need to choose a date");
            date.requestFocus();
            return;
        }

        //it will put all the data on Strings
        String typeN= type.getSelectedItem().toString();
        String statusN= status.getSelectedItem().toString();
        String descr= description.getText().toString();
        String cour= course.getText().toString();
        String nameN= name.getText().toString();

        //create a variable assignment
        newAssignment = new AssignmentEntity(nameN,typeN,descr,dateL,statusN,username,cour);

        //create in the data the assignment
        viewModel.createAssignment(newAssignment, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "createAssignment: success");
                setResponse(true);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "createAssignment: fail");
                setResponse(false);
            }
        });

    }

    private void setResponse(Boolean response){
        if(response){
            //display a little message to say that the assignment is created
            Toast toast = Toast.makeText(this,"Assignment created",Toast.LENGTH_LONG);
            toast.show();
            //go to Home
            startActivity(new Intent(CreateAssignment.this, Home.class));
        }
        else{
            //display a little message to say that the assignment isn't created
            Toast toast = Toast.makeText(this,"Fail",Toast.LENGTH_LONG);
            toast.show();
        }
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
     */
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.profile_taskbar:
                Intent intent = new Intent(CreateAssignment.this, MyProfile.class);
                intent.putExtra("username", username);
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
