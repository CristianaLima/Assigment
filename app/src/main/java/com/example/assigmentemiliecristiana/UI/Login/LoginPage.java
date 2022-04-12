package com.example.assigmentemiliecristiana.UI.Login;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;


import android.widget.Button;
import android.widget.TextView;

import com.example.assigmentemiliecristiana.BaseApp;
import com.example.assigmentemiliecristiana.R;
import com.example.assigmentemiliecristiana.UI.Home;
import com.example.assigmentemiliecristiana.database.repository.StudentRepository;

/**
 * this activity is for login to a account
 */
public class LoginPage extends AppCompatActivity {

    private StudentRepository studentRepository;
    private TextView email;
    private TextView password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //link this activity with the layout
        setContentView(R.layout.login);

        //link the variables in this activity with that in the layout
        email = findViewById(R.id.username_login);
        password = findViewById(R.id.password);
        Button loginbtn = findViewById(R.id.loginbtn);
        Button registerbtn = findViewById(R.id.registerbtn);

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
    }



    /**
     * it will see if the data is put correctly
     */
    private void attemptLogin(){
        email.setError(null);
        password.setError(null);

        //put the data to Strings
        String email_data = email.getText().toString();
        String password_data = password.getText().toString();

        //see if the password is empty or less than 4 characters
        if (TextUtils.isEmpty(password_data)&&!isPasswordValid(password_data)){
            password.setError("Password is too short or empty");
            password.setText("");
            password.requestFocus();
            return;
        }

        //see if the username is empty
        if(TextUtils.isEmpty(email_data)){
            email.setError("Email is empty");
            email.requestFocus();
            return;
        }
        if (!isEmailValid(email_data)) {
            email.setError("Invalid email");
            email.requestFocus();
            return;
        }

        //it will get the student
        studentRepository.signIn(email_data, password_data, task -> {
            //see if the password exist
            if(task.isSuccessful()){
                    //go to Home
                    Intent intent = new Intent(LoginPage.this, Home.class);
                    intent.putExtra("email",email_data);
                    startActivity(intent);
                    email.setText("");
                    password.setText("");
                }

                else {
                    password.setError("Invalid login");
                    password.requestFocus();
                    password.setText("");
                }


        });


    }

    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }
}