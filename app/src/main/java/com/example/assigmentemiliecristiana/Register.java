package com.example.assigmentemiliecristiana;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

public class Register extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView username = findViewById(R.id.input_Username);
        TextView email = findViewById(R.id.input_Email);
        TextView password = findViewById(R.id.input_Password);

        Button registerbtn = (Button) findViewById(R.id.register_btn);

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().isEmpty() && password.getText().toString().isEmpty()){
                    Toast.makeText(Register.this,"You must enter a name and password to register", Toast.LENGTH_SHORT).show();

                }else
                    Toast.makeText(Register.this,"fsjdkfjsdkf", Toast.LENGTH_SHORT).show();

            }
        });



    }

}
