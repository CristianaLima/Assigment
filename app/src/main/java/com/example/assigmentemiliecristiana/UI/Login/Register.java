package com.example.assigmentemiliecristiana.UI.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assigmentemiliecristiana.BaseApp;
import com.example.assigmentemiliecristiana.R;
import com.example.assigmentemiliecristiana.database.entity.StudentEntity;
import com.example.assigmentemiliecristiana.database.repository.StudentRepository;
import com.example.assigmentemiliecristiana.util.OnAsyncEventListener;

/**
 * this activity is for create a account
 */
public class Register extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private StudentRepository repository;

    private TextView password;
    private TextView email_input;
    private TextView username;
    private TextView confirmP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //link this activity with the layout
        setContentView(R.layout.register);
        repository = ((BaseApp) getApplication()).getStudentRepository();

        //link the variables in this activity with that in the layout
        username = findViewById(R.id.input_Username);
        email_input = findViewById(R.id.input_Email);
        password = findViewById(R.id.input_Password);
        confirmP = findViewById(R.id.input_ConfirmPassword);
        Button registerbtn = findViewById(R.id.register_btn);

        //set what to do when you click on the register button
        //the saveChange method is a external method which is below
        registerbtn.setOnClickListener(view -> saveChange(
            username.getText().toString(),
            email_input.getText().toString(),
            password.getText().toString(),
            confirmP.getText().toString()
        ));

    }

    /**
     * This will create a new student
     * @param username
     * @param email
     * @param pwd password
     * @param pwdConf confirm password
     */
    private void saveChange(String username, String email, String pwd, String pwdConf){
        //see if the password is not equals to the confirm password and if it is not more that 4 characters
        if(!pwd.equals(pwdConf)||pwd.length()<6){
            password.setError("Password is invalid");
            password.requestFocus();
            password.setText("");
            confirmP.setText("");
            return;
        }
        //see if the email is not put correctly
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_input.setError("Invalid email");
            email_input.requestFocus();
            return;
        }

        //it will create a new student
        StudentEntity newStudent = new StudentEntity(username,email,pwd);
        repository.register(newStudent, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG,"Create user with email succeed");
                setResponse(true);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG,"Create user with email failed", e);
                setResponse(false);
            }
        });
    }

    /**
     * what to do after create a student
     * @param response
     */
    private void setResponse(Boolean response){
        if(response){
            //display a little message to say that the client is created
            Toast toast = Toast.makeText(this,"Client created",Toast.LENGTH_LONG);
            toast.show();
            //go to LoginPage
            Intent intent = new Intent(Register.this, LoginPage.class);
            startActivity(intent);
        }
        else{
            //display a little error message to say that the username is already use
            email_input.setError("email already used");
            email_input.requestFocus();
        }
    }

}
