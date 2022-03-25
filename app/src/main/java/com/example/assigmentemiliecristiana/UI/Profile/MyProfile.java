package com.example.assigmentemiliecristiana.UI.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.assigmentemiliecristiana.R;
import com.example.assigmentemiliecristiana.UI.Login.LoginPage;
import com.example.assigmentemiliecristiana.database.entity.StudentEntity;
import com.example.assigmentemiliecristiana.util.OnAsyncEventListener;
import com.example.assigmentemiliecristiana.viewmodel.student.StudentViewModel;

/**
 * this activity show the profile of the user when you click on the person button
 */
public class MyProfile extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private AppBarConfiguration appBarConfiguration;
    private String username;

    private TextView usernameDisplay;
    private TextView emailDisplay;
    private TextView passwordDisplay;

    private Button deleteProfile;
    private Button modify;
    private Button logout;

    private StudentViewModel viewModel;
    private StudentEntity student;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //link this activity with the layout
        setContentView(R.layout.profile);

        //use ActionBar utility methods
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("AssignmentApp");

        // methods to display the icon in the ActionBar
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //go research the username of the user
        username = getIntent().getStringExtra("username");

        //link the variables in this activity with that in the layout
        usernameDisplay = findViewById(R.id.input_Username);
        emailDisplay = findViewById(R.id.input_Email);
        passwordDisplay = findViewById(R.id.input_Password);
        deleteProfile = findViewById(R.id.delete_account_btn);
        logout = findViewById(R.id.disconnect);
        modify = findViewById(R.id.modify_account_btn);

        //set what to do when you click on the modify button
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to ModifyProfile with the username of the user
                Intent intent = new Intent(MyProfile.this, ModifiyProfile.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        //go research the student information from the database
        StudentViewModel.Factory factory = new StudentViewModel.Factory(getApplication(),username);
        viewModel = new ViewModelProvider(this,factory).get(StudentViewModel.class);
        viewModel.getStudent().observe(this, studentEntity -> {
            if (studentEntity!=null){
                student = studentEntity;
                //It's a external method which is below
                updateContent();
            }
        });

        //set what to do when you click on the delete button
        //the delete method is a external method which is below
        deleteProfile.setOnClickListener(view -> delete());

        //set what to do when you click on the log out button
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to LoginPage
                startActivity(new Intent(MyProfile.this, LoginPage.class));
            }
        });

    }

    /**
     * set the text of the TextViews
     */
    private void updateContent(){
        if (student!=null){
            usernameDisplay.setText(student.getUsername());
            emailDisplay.setText(student.getEmail());
            passwordDisplay.setText("**********");

            Log.i(TAG,"Activity populated.");
        }
    }

    /**
     * It will remove the student from the database
     */
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
                setResponse(false);
            }
        });
    }

    private void setResponse(Boolean response){
        if(response){
            //display a little message to say that the profile is deleted
            Toast toast = Toast.makeText(this,"Profile deleted",Toast.LENGTH_LONG);
            toast.show();
            //go to LoginPage
            startActivity(new Intent(MyProfile.this, LoginPage.class));
        }
        else{
            //display a little message to say that the profile isn't deleted
            Toast toast = Toast.makeText(this,"Fail delete profile",Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /**
     * method to inflate the options menu when
     * the user opens the menu for the first time
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    /**
     * methods to control the operations that will
     * happen when user clicks on the action buttons
     */
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.profile_taskbar:
                Intent intent = new Intent(MyProfile.this, MyProfile.class);
                intent.putExtra("username", username);
                startActivity(intent);
                break;

            case R.id.about_taskbar:
                final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("About");
                alertDialog.setCancelable(false);
                alertDialog.setMessage("This project has been done to understand how Android Studio work and to implement the different architectures patterns that we have learned.");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"Ok",(dialog, which)->alertDialog.dismiss());
                alertDialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
