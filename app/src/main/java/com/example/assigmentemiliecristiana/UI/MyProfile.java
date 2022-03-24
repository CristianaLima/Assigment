package com.example.assigmentemiliecristiana.UI;

import static com.example.assigmentemiliecristiana.database.AppDatabase.initializeDemoData;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.assigmentemiliecristiana.R;
import com.example.assigmentemiliecristiana.database.AppDatabase;
import com.example.assigmentemiliecristiana.database.entity.StudentEntity;
import com.example.assigmentemiliecristiana.util.OnAsyncEventListener;
import com.example.assigmentemiliecristiana.viewmodel.assignment.AssignmentViewModel;
import com.example.assigmentemiliecristiana.viewmodel.student.StudentViewModel;

public class MyProfile extends AppCompatActivity {
    private static final String TAG = "Profile";
    private AppBarConfiguration appBarConfiguration;
    private String username;

    private TextView usernameDisplay;
    private TextView emailDisplay;
    private TextView passwordDisplay;

    private Button deleteProfile;
    private Button modify;
    private Button logout;
    private Button help;

    private StudentViewModel viewModel;
    private StudentEntity student;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        username = getIntent().getStringExtra("username");

        usernameDisplay = findViewById(R.id.input_Username);
        emailDisplay = findViewById(R.id.input_Email);
        passwordDisplay = findViewById(R.id.input_Password);
        deleteProfile = findViewById(R.id.delete_account_btn);
        logout = findViewById(R.id.disconnect);
        modify = (Button)findViewById(R.id.modify_account_btn);
        help = (Button)findViewById(R.id.help);

        help.setOnClickListener(view -> help());

        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProfile.this, ModifiyProfile.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        StudentViewModel.Factory factory = new StudentViewModel.Factory(getApplication(),username);
        viewModel = new ViewModelProvider(this,factory).get(StudentViewModel.class);
        viewModel.getStudent().observe(this, studentEntity -> {
            if (studentEntity!=null){
                student = studentEntity;
                updateContent();
            }
        });

        deleteProfile.setOnClickListener(view -> delete());
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyProfile.this, LoginPage.class));
            }
        });

    }
    private void updateContent(){
        if (student!=null){
            usernameDisplay.setText(student.getUsername());
            emailDisplay.setText(student.getEmail());
            passwordDisplay.setText("**********");

            Log.i(TAG,"Activity populated.");
        }
    }

    private void delete(){
        viewModel.deleteStudent(student, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG,"Delete assignment success");
                setResponse(true);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG,"Delete assignment fail");
                setResponse(true);
            }
        });
    }

    private void setResponse(Boolean response){
        if(response){
            Toast toast = Toast.makeText(this,"Profile deleted",Toast.LENGTH_LONG);
            toast.show();
            startActivity(new Intent(MyProfile.this, LoginPage.class));
        }
        else{
            Toast toast = Toast.makeText(this,"Fail delete profile",Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void help(){
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("About");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("This project has been done to understand how Android Studio work and to implement the different architectures patterns that we have learned.");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Ok",(dialog, which)->alertDialog.dismiss());
        alertDialog.show();
    }

}
