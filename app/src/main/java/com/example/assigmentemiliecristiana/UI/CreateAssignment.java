package com.example.assigmentemiliecristiana.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assigmentemiliecristiana.R;

import java.util.Calendar;

public class CreateAssignment extends AppCompatActivity {

    Button btn;
    TextView date;
    EditText name;
    EditText course;
    EditText description;
    DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_assigment);

        btn = (Button) findViewById(R.id.create_button);
        date = (TextView) findViewById(R.id.due_date);
        name = (EditText) findViewById(R.id.name);
        course = (EditText) findViewById(R.id.course);
        description = (EditText) findViewById(R.id.description);

        Spinner spinner = (Spinner) findViewById(R.id.status);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_list, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        Spinner spinner_type = (Spinner) findViewById(R.id.type);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.type_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_type.setAdapter(adapter2);

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

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateAssignment.this, Home.class));

            }
        });

    }




}
