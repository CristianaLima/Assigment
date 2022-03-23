package com.example.assigmentemiliecristiana.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.assigmentemiliecristiana.R;
import com.example.assigmentemiliecristiana.database.entity.AssignmentEntity;
import com.example.assigmentemiliecristiana.util.OnAsyncEventListener;
import com.example.assigmentemiliecristiana.viewmodel.assignment.AssignmentViewModel;

import java.util.Calendar;
import java.util.Date;

public class AssignmentDescr extends AppCompatActivity {
    private static final String TAG = "AccountDetailActivity";

    private static final int EDIT_ACCOUNT = 1;

    private AssignmentEntity assignment;
    private EditText descr;
    private EditText course;
    private EditText note;
    private TextView displayDate;
    private Spinner spinner;
    private Spinner spinner_type;
    private Button delete;
    private Button save;
    private ImageButton date;

    private Long dateL;
    private Long assignmentId;

    private AssignmentViewModel viewModel;
    DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assignment_details);

        assignmentId = getIntent().getLongExtra("assignmentId",0L);

        descr = findViewById(R.id.description);
        course = findViewById(R.id.course);
        note = findViewById(R.id.note);
        delete = findViewById(R.id.delete_assignment);
        save = findViewById(R.id.save_assigment);
        date = findViewById(R.id.date_assignment);
        displayDate = findViewById(R.id.date_input);

        spinner = (Spinner) findViewById(R.id.status);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_list, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner_type = (Spinner) findViewById(R.id.type);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.type_list, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_type.setAdapter(adapter2);

        AssignmentViewModel.Factory factory = new AssignmentViewModel.Factory(
                getApplication(),assignmentId);
        viewModel = new ViewModelProvider(this,factory).get(AssignmentViewModel.class);
        viewModel.getAssignment().observe(this,assignmentEntity -> {
            if (assignmentEntity!= null){
                assignment = assignmentEntity;
                updateContent();
            }
        });

        delete.setOnClickListener(view -> deleteAssignment());

        save.setOnClickListener(view -> saveChange());

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        date.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            AssignmentDescr.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, year, month, day);
                    datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    datePickerDialog.show();

                }
            }
        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                dateL = new Date(year,month,day).getTime();
                String dateS = day + "/" + month + "/" + year;

                displayDate.setText(dateS);
                }
        };
    }
    private void updateContent(){
        if (assignment !=null){
            descr.setText(assignment.getName());
            course.setText(assignment.getCourse());
            note.setText(assignment.getDescription());

            ArrayAdapter arrayAdapter = (ArrayAdapter) spinner.getAdapter();
            int idStatut = arrayAdapter.getPosition(assignment.getStatus());
            spinner.setSelection(idStatut);

            ArrayAdapter arrayAdapter2 = (ArrayAdapter) spinner_type.getAdapter();
            int idType = arrayAdapter2.getPosition(assignment.getType());
            spinner_type.setSelection(idType);

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

    private void saveChange(){
       String name = descr.getText().toString();
       String courseA = course.getText().toString();
       String description = note.getText().toString();
       String status = spinner.getSelectedItem().toString();
       String type = spinner_type.getSelectedItem().toString();

       assignment.setName(name);
       assignment.setCourse(courseA);
       assignment.setDescription(description);
       assignment.setStatus(status);
       assignment.setType(type);
       assignment.setDate(dateL);

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
            Toast toast = Toast.makeText(this,"Assignment modify",Toast.LENGTH_LONG);
            toast.show();
            startActivity(new Intent(AssignmentDescr.this, Home.class));
        }
        else{
            Toast toast = Toast.makeText(this,"Fail",Toast.LENGTH_LONG);
            toast.show();
        }
    }

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
            Toast toast = Toast.makeText(this,"Assignment deleted",Toast.LENGTH_LONG);
            toast.show();
            startActivity(new Intent(AssignmentDescr.this, Home.class));
        }
        else{
            Toast toast = Toast.makeText(this,"Fail",Toast.LENGTH_LONG);
            toast.show();
        }
    }

}
