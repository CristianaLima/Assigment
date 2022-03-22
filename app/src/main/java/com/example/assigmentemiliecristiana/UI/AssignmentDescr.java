package com.example.assigmentemiliecristiana.UI;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.assigmentemiliecristiana.R;
import com.example.assigmentemiliecristiana.database.entity.AssignmentEntity;
import com.example.assigmentemiliecristiana.viewmodel.assignment.AssignmentListViewModel;
import com.example.assigmentemiliecristiana.viewmodel.assignment.AssignmentViewModel;

import org.w3c.dom.Text;

public class AssignmentDescr extends AppCompatActivity {
    private static final String TAG = "AccountDetailActivity";

    private static final int EDIT_ACCOUNT = 1;

    private AssignmentEntity assigment;
    private TextView descr;
    private TextView course;
    private TextView note;
    private Spinner spinner;
    private Spinner spinner_type;

    private AssignmentViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignmentdescr);

        Long assignmentId = getIntent().getLongExtra("assignmentId",0L);

        descr = findViewById(R.id.description);
        course = findViewById(R.id.course);
        note = findViewById(R.id.note);

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
            spinner.setSelection(idType);

            Log.i(TAG,"Activity populated.");

        }
    }

}
