package com.example.assigmentemiliecristiana.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.assigmentemiliecristiana.R;
import com.example.assigmentemiliecristiana.database.async.student.CreateStudent;
import com.example.assigmentemiliecristiana.database.entity.StudentEntity;
import com.example.assigmentemiliecristiana.util.OnAsyncEventListener;
import com.example.assigmentemiliecristiana.viewmodel.student.StudentViewModel;

public class ModifiyProfile extends AppCompatActivity {
    private static final String TAG = "ModifyProfileActivity";

    private AppBarConfiguration appBarConfiguration;
    private EditText usernameDisplay;
    private EditText mailDisplay;
    private EditText pwdDisplay;
    private EditText pwdConfDisplay;
    private Button apply;
    private String username;

    private StudentViewModel viewModel;
    private StudentEntity student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_profile);
        username = getIntent().getStringExtra("username");

        usernameDisplay = findViewById(R.id.input_Username);
        mailDisplay = findViewById(R.id.input_Email);
        pwdDisplay = findViewById(R.id.input_Password);
        pwdConfDisplay = findViewById(R.id.input_ConfirmPassword);
        apply = (Button) findViewById(R.id.apply_btn);

        StudentViewModel.Factory factory = new StudentViewModel.Factory(getApplication(),username);
        viewModel = new ViewModelProvider(this,factory).get(StudentViewModel.class);
        viewModel.getStudent().observe(this, studentEntity -> {
            if (studentEntity!= null){
                student = studentEntity;
                setContent();
            }
        });

        apply.setOnClickListener(view -> saveModification(
                usernameDisplay.getText().toString(),
                mailDisplay.getText().toString(),
                pwdDisplay.getText().toString(),
                pwdConfDisplay.getText().toString()
        ));

    }

    private void setContent(){
        usernameDisplay.setText(student.getUsername());
        mailDisplay.setText(student.getEmail());
    }

    private void saveModification(String username, String email, String pwd, String pwdConf){
        if (!pwd.equals(pwdConf) || pwd.length()<4){
            pwdDisplay.setError("Password is invalid");
            pwdDisplay.requestFocus();
            pwdDisplay.setText("");
            pwdConfDisplay.setText("");
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mailDisplay.setError("Invalid email");
            mailDisplay.requestFocus();
            return;
        }

        if (!username.equals(student.getUsername())){

            StudentEntity newStudent = new StudentEntity(username,email,pwd);
            new CreateStudent(getApplication(), new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG,"Change user profile succeed");
                    setResponse(true);
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d(TAG,"Change user profile failed", e);
                    setResponse(false);
                }
            }).execute(newStudent);

        }

        student.setEmail(email);
        student.setPassword(pwd);

        viewModel.updateStudent(student, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG,"Change user profile succeed");
                setResponse(true);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG,"Change user profile failed", e);
                setResponse(false);
            }
        });

    }
    private void setResponse(Boolean response){
        if(response){
            Toast toast = Toast.makeText(this,"Student modified",Toast.LENGTH_LONG);
            toast.show();
            Intent intent = new Intent(ModifiyProfile.this, MyProfile.class);
            intent.putExtra("username", username);
            startActivity(intent);
        }
        else{
            usernameDisplay.setError("Username already used");
            usernameDisplay.requestFocus();
        }
    }
}
