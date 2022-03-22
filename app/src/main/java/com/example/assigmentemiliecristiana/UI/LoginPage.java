package com.example.assigmentemiliecristiana.UI;

import static com.example.assigmentemiliecristiana.database.AppDatabase.initializeDemoData;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;


import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assigmentemiliecristiana.BaseApp;
import com.example.assigmentemiliecristiana.R;
import com.example.assigmentemiliecristiana.database.AppDatabase;
import com.example.assigmentemiliecristiana.database.repository.StudentRepository;

public class LoginPage extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private StudentRepository studentRepository;
    TextView username;
    TextView password;
    Button dataBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

          username = findViewById(R.id.username_login);
          password = findViewById(R.id.password);

         studentRepository= ((BaseApp) getApplication()).getStudentRepository();

        Button loginbtn = (Button) findViewById(R.id.loginbtn);
        Button registerbtn = (Button) findViewById(R.id.registerbtn);
        dataBtn = (Button)findViewById(R.id.reset_button);

             loginbtn.setOnClickListener(view -> attemptLogin());

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(LoginPage.this, Register.class));
            }
        });


        dataBtn.setOnClickListener(view -> reinitializeDB());
    }

    private void reinitializeDB(){
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

   private void attemptLogin(){
        username.setError(null);
        password.setError(null);

        String username_data = username.getText().toString();
        String password_data = password.getText().toString();

        Boolean cancel = false;
        View focusView = null; // why ????

        if (!TextUtils.isEmpty(password_data)&&!isPasswordValid(password_data)){
            password.setError("Password is too short or empty");
            password.setText("");
            focusView = password;
            cancel=true;
        }

        if(TextUtils.isEmpty(username_data)){
            username.setError("Username is empty");
            focusView=username;
            cancel=true;
        }

        if(cancel){
            focusView.requestFocus();
        }
        else{
            studentRepository.getStudent(username_data, getApplication()).observe(LoginPage.this, studentEntity -> {
                if(studentEntity!=null){
                    if(studentEntity.getPassword().equals(password_data)){
                        SharedPreferences.Editor editor = getSharedPreferences(Home.PREFS_NAME, 0).edit();
                        editor.putString(Home.PREFS_USER, studentEntity.getUsername());
                        editor.apply();

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
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
}