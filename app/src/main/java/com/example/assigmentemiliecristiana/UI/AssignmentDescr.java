package com.example.assigmentemiliecristiana.UI;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.assigmentemiliecristiana.R;
import com.example.assigmentemiliecristiana.database.entity.AssignmentEntity;
import com.example.assigmentemiliecristiana.util.OnAsyncEventListener;
import com.example.assigmentemiliecristiana.viewmodel.assignment.AssignmentViewModel;

public class AssignmentDescr extends AppCompatActivity {
    private static final String TAG = "AccountDetailActivity";

    private static final int EDIT_ACCOUNT = 1;

    private AssignmentEntity assigment;
    private TextView descr;
    private TextView course;
    private TextView note;
    private Spinner spinner;
    private Spinner spinner_type;
    private String statut;
    private String type;
    private AlertDialog alertDialogStatus;

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


//        setButtonAlertDialogStatus(alertDialogStatus);
//        spinner.onClick(alertDialogStatus,0);
//        spinner.setOnItemClickListener((adapterView, view, position, id) -> modifyStatus());
//        spinner_type.setOnItemClickListener((adapterView, view, i, l) -> modifyType() );
    }
    private void updateContent(){
        if (assigment!=null){
            descr.setText(assigment.getName());
            course.setText(assigment.getCourse());
            note.setText(assigment.getDescription());

            ArrayAdapter arrayAdapter = (ArrayAdapter) spinner.getAdapter();
            int idStatut = arrayAdapter.getPosition(assigment.getStatus());
            spinner.setSelection(idStatut);
            statut = spinner.getSelectedItem().toString();

            ArrayAdapter arrayAdapter2 = (ArrayAdapter) spinner_type.getAdapter();
            int idType = arrayAdapter2.getPosition(assigment.getType());
            spinner_type.setSelection(idType);
            type = spinner_type.getSelectedItem().toString();

            Log.i(TAG,"Activity populated.");

        }
    }

    private void setButtonAlertDialogStatus(AlertDialog alertDialog){
        alertDialog.setTitle("Change the statut");
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Execute",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                modifyStatus();
            }
        });
    }


    private void modifyStatus(){
         if (!statut.equals(spinner.getSelectedItem())){
             statut = spinner.getSelectedItem().toString();
             assigment.setStatus(statut);
             viewModel.updateAssignment(assigment, new OnAsyncEventListener() {
                 @Override
                 public void onSuccess() {
                     Log.d(TAG,"updateAssigment: success");
                 }

                 @Override
                 public void onFailure(Exception e) {
                     Log.d(TAG,"updateAssigment: fail",e);
                 }
             });
         }


    }
    private void modifyType(){
        if (!type.equals(spinner_type.getSelectedItem())){
            type = spinner_type.getSelectedItem().toString();
            assigment.setType(type);
            viewModel.updateAssignment(assigment, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG,"updateAssigment: success");
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d(TAG,"updateAssigment: fail",e);
                }
            });
        }


    }

}
