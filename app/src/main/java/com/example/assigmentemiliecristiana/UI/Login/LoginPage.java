package com.example.assigmentemiliecristiana.UI.Login;

import static com.example.assigmentemiliecristiana.database.AppDatabase.initializeDemoData;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;


import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assigmentemiliecristiana.BaseApp;
import com.example.assigmentemiliecristiana.R;
import com.example.assigmentemiliecristiana.UI.Home;
import com.example.assigmentemiliecristiana.database.repository.StudentRepository;

/**
 * this activity is for login to a account
 */
public class LoginPage extends AppCompatActivity {

    private StudentRepository studentRepository;
    private TextView username;
    private TextView password;
    private Button dataBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //link this activity with the layout
        setContentView(R.layout.login);

        //link the variables in this activity with that in the layout
        username = findViewById(R.id.username_login);
        password = findViewById(R.id.password);
        Button loginbtn = findViewById(R.id.loginbtn);
        Button registerbtn = findViewById(R.id.registerbtn);
        dataBtn = findViewById(R.id.reset_button);

        //to get the repository
        studentRepository= ((BaseApp) getApplication()).getStudentRepository();

        //set what to do when you click on the login button
        //the attemptLogin method is a external method which is below
        loginbtn.setOnClickListener(view -> attemptLogin());

        //set what to do when you click on the sigh in button
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to Register
               startActivity(new Intent(LoginPage.this, Register.class));
            }
        });

        //set what to do when you click on the reset data button
        //the reinitializeDB method is a external method which is below
        dataBtn.setOnClickListener(view -> reinitializeDB());
    }

    /**
     * It will create the database and go research the initial data
     */
    private void reinitializeDB(){
        //create a AlertDialog to be sure the user want to do that
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Reset database");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Do you really want to reset the database?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Reset",(dialog, which) ->{
            initializeDemoData(AppDatabase.getInstance(this));
            Toast.makeText(this, "Demo data initialized",Toast.LENGTH_LONG).show();
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"cancel",(dialog, which)->alertDialog.dismiss());
        alertDialog.show();
    }

    /**
     * it will see if the data is put correctly
     */
    private void attemptLogin(){
        username.setError(null);
        password.setError(null);

        //put the data to Strings
        String username_data = username.getText().toString();
        String password_data = password.getText().toString();

        //see if the password is empty or less than 4 characters
        if (TextUtils.isEmpty(password_data)&&!isPasswordValid(password_data)){
            password.setError("Password is too short or empty");
            password.setText("");
            password.requestFocus();
            return;
        }

        //see if the username is empty
        if(TextUtils.isEmpty(username_data)){
            username.setError("Username is empty");
            username.requestFocus();
            return;
        }

        //it will get the student
        studentRepository.getStudent(username_data, getApplication()).observe(LoginPage.this, studentEntity -> {
            //see if the account exist
            if(studentEntity!=null){
                //see if the password is correct
                if(studentEntity.getPassword().equals(password_data)){
                    SharedPreferences.Editor editor = getSharedPreferences(Home.PREFS_NAME, 0).edit();
                    editor.putString(Home.PREFS_USER, studentEntity.getUsername());
                    editor.apply();

                    //go to Home
                    Intent intent = new Intent(LoginPage.this, Home.class);
                    startActivity(intent);
                    username.setText("");
                    password.setText("");
                }

                else {
                    password.setError("This password is incorrect");
                    password.requestFocus();
                    password.setText("");
                }

            }
            else {
                username.setError("Invalid login credentials");
                username.requestFocus();
                username.setText("");
                password.setText("");
            }
        });

    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
}