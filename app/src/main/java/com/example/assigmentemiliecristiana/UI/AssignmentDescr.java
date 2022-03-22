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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.assigmentemiliecristiana.R;
import com.example.assigmentemiliecristiana.database.entity.AssignmentEntity;
import com.example.assigmentemiliecristiana.viewmodel.assignment.AssignmentViewModel;

import java.util.Calendar;

public class AssignmentDescr extends AppCompatActivity {
    private static final String TAG = "AccountDetailActivity";

    private static final int EDIT_ACCOUNT = 1;

    private AssignmentEntity assigment;
    private TextView descr;
    private TextView course;
    private TextView note;
    private Spinner spinner;
    private Spinner spinner_type;
    private Button delete;
    private Button save;
    private ImageButton date;

    private AssignmentViewModel viewModel;
    DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assignment_details);

        Long assignmentId = getIntent().getLongExtra("assignmentId",0L);

        descr = findViewById(R.id.description);
        course = findViewById(R.id.course);
        note = findViewById(R.id.note);
        delete = findViewById(R.id.delete_assignment);
        save = findViewById(R.id.save_assigment);
        date = findViewById(R.id.date_assignment);

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
                assigment= assignmentEntity;
                updateContent();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AssignmentDescr.this, Home.class));
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AssignmentDescr.this, Home.class));
            }
        });

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
    }
    private void updateContent(){
        if (assigment!=null){
            descr.setText(assigment.getName());
            course.setText(assigment.getCourse());
            note.setText(assigment.getDescription());

            ArrayAdapter arrayAdapter = (ArrayAdapter) spinner.getAdapter();
            int idStatut = arrayAdapter.getPosition(assigment.getStatus());
            spinner.setSelection(idStatut);

            ArrayAdapter arrayAdapter2 = (ArrayAdapter) spinner_type.getAdapter();
            int idType = arrayAdapter2.getPosition(assigment.getType());
            spinner_type.setSelection(idType);

            Log.i(TAG,"Activity populated.");

        }
    }

}
