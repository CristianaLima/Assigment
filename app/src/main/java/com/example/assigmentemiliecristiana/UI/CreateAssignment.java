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
    Long dateL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_assigment);

        btn = (Button) findViewById(R.id.create_button);
        date = (TextView) findViewById(R.id.due_date);
        name = (EditText) findViewById(R.id.name);
        course = (EditText) findViewById(R.id.course);
        description = (EditText) findViewById(R.id.description);

        username = getIntent().getStringExtra("username");

        status = (Spinner) findViewById(R.id.status);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_list, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        status.setAdapter(adapter);

        type = (Spinner) findViewById(R.id.type);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.type_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                type.setAdapter(adapter2);

        ImageButton calendar_button = (ImageButton) findViewById(R.id.create_date);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        calendar_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            CreateAssignment.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener, year, month, day);
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

                date.setText(dateS);
            }
        };
        AssignmentViewModel.Factory factory = new AssignmentViewModel.Factory(
                getApplication(), newAssignment.getId());
        viewModel = new ViewModelProvider(this,factory).get(AssignmentViewModel.class);


        btn.setOnClickListener(view -> createAssignment());

    }

    private void createAssignment(){
        if (name.getText().toString().equals("")){
            name.setError("You need to put a name");
            name.requestFocus();
            return;
        }
        if (course.getText().toString().equals("")){
            course.setError("You need to specify the course");
            course.requestFocus();
            return;
        }
        if (description.getText().toString().equals("")){
            description.setError("You need to put a description");
            description.requestFocus();
            return;
        }
        if (dateL==null){
            date.setError("You need to choose a date");
            date.requestFocus();
            return;
        }

        String typeN= type.getSelectedItem().toString();
        String statusN= status.getSelectedItem().toString();
        String descr= description.getText().toString();
        String cour= course.getText().toString();
        String nameN= name.getText().toString();

        newAssignment = new AssignmentEntity(nameN,typeN,descr,dateL,statusN,username,cour);

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
            Toast toast = Toast.makeText(this,"Assignment created",Toast.LENGTH_LONG);
            toast.show();
            startActivity(new Intent(CreateAssignment.this, Home.class));
        }
        else{
            Toast toast = Toast.makeText(this,"Fail",Toast.LENGTH_LONG);
            toast.show();
        }
    }




}
