package com.example.assigmentemiliecristiana.UI;
//fjskdfgfdgfdfsddsa
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.assigmentemiliecristiana.R;
import com.example.assigmentemiliecristiana.database.async.student.CreateStudent;
import com.example.assigmentemiliecristiana.database.entity.StudentEntity;
import com.example.assigmentemiliecristiana.util.OnAsyncEventListener;

public class Register extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private AppBarConfiguration appBarConfiguration;
    TextView password;
    TextView email_input;
    TextView username;
    TextView confirmP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

         username = findViewById(R.id.input_Username);
         email_input = findViewById(R.id.input_Email);
        password = findViewById(R.id.input_Password);
        confirmP = findViewById(R.id.input_ConfirmPassword);

        Button registerbtn = (Button) findViewById(R.id.register_btn);

        registerbtn.setOnClickListener(view -> saveChange(
            username.getText().toString(),
            email_input.getText().toString(),
            password.getText().toString(),
            confirmP.getText().toString()
        ));

    }

    private void saveChange(String username, String email, String password1, String password2){
        if(!password1.equals(password2)||password1.length()<4){
            password.setError("Password is invalid");
            password.requestFocus();
            password.setText("");
            confirmP.setText("");
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email_input.setError("Invalid email");
            email_input.requestFocus();
            return;
        }

        StudentEntity newStudent = new StudentEntity(username,email,password1);
        new CreateStudent(getApplication(), new OnAsyncEventListener() {
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
        }).execute(newStudent);
    }

    private void setResponse(Boolean response){
        if(response){
            Toast toast = Toast.makeText(this,"Client created",Toast.LENGTH_LONG);
            toast.show();
            Intent intent = new Intent(Register.this, LoginPage.class);
            startActivity(intent);
        }
        else{
            username.setError("Username already used");
            username.requestFocus();
        }
    }

}
