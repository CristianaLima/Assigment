package com.example.assigmentemiliecristiana;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.assigmentemiliecristiana.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView username = findViewById(R.id.username);
        TextView password = findViewById(R.id.password);

        Button loginbtn = (Button) findViewById(R.id.loginbtn);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().equals("admin") && password.getText().toString().equals("admin")){
                    Toast.makeText(MainActivity.this,"LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(MainActivity.this,"LOGIN FAILED", Toast.LENGTH_SHORT).show();

            }
        });

     /*   setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        listView=(ListView)findViewById(R.id.listview);

      ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Android begin project");
        arrayList.add("JavaScript begin project");
        arrayList.add("Patterns observer pattern");
        arrayList.add("Cisco read theory");
        arrayList.add("Sleep");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.listview_layout, arrayList);
        listView.setAdapter(arrayAdapter);*/

    }

}