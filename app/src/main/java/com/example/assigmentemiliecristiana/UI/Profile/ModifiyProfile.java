package com.example.assigmentemiliecristiana.UI.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.assigmentemiliecristiana.R;
import com.example.assigmentemiliecristiana.database.entity.StudentEntity;
import com.example.assigmentemiliecristiana.util.OnAsyncEventListener;
import com.example.assigmentemiliecristiana.viewmodel.student.StudentViewModel;

/**
 * this activity is for modify the profile of the user
 */
public class ModifiyProfile extends AppCompatActivity {
    private static final String TAG = "ModifyProfileActivity";

    private AppBarConfiguration appBarConfiguration;
    private EditText usernameDisplay;
    private EditText mailDisplay;
    private EditText pwdDisplay;
    private EditText pwdConfDisplay;
    private Button apply;
    private String email;

    private StudentViewModel viewModel;
    private StudentEntity student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //link this activity with the layout
        setContentView(R.layout.modify_profile);
        //go research the username of the user
        email = getIntent().getStringExtra("email");

        //use ActionBar utility methods
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("AssignmentApp");

        // methods to display the icon in the ActionBar
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //link the variables in this activity with that in the layout
        usernameDisplay = findViewById(R.id.input_Username);
        mailDisplay = findViewById(R.id.input_Email);
        pwdDisplay = findViewById(R.id.input_Password);
        pwdConfDisplay = findViewById(R.id.input_ConfirmPassword);
        apply = findViewById(R.id.apply_btn);

        //go research the student information from the database
        StudentViewModel.Factory factory = new StudentViewModel.Factory(getApplication(), email);
        viewModel = new ViewModelProvider(this,factory).get(StudentViewModel.class);
        viewModel.getStudent().observe(this, studentEntity -> {
            if (studentEntity!= null){
                student = studentEntity;
                //It's a external method which is below
                setContent();
            }
        });

        //set what to do when you click on the apply button
        //the saveModification method is a external method which is below
        apply.setOnClickListener(view -> saveModification(
                usernameDisplay.getText().toString(),
                mailDisplay.getText().toString(),
                pwdDisplay.getText().toString(),
                pwdConfDisplay.getText().toString()
        ));

    }

    /**
     * this method is use to display the content of the page
     */
    private void setContent(){
        usernameDisplay.setText(student.getUsername());
        mailDisplay.setText(student.getEmail());
    }

    /**
     * this method is use to see if it is a little modification or create a new user + delete the current one
     * @param username
     * @param email
     * @param pwd password
     * @param pwdConf confirm password
     */
    private void saveModification(String username, String email, String pwd, String pwdConf){
        //see if the password is not equals to the confirm password and if it is not more that 4 characters
        if (!pwd.equals(pwdConf) || pwd.length()<4){
            pwdDisplay.setError("Password is invalid");
            pwdDisplay.requestFocus();
            pwdDisplay.setText("");
            pwdConfDisplay.setText("");
            return;
        }
        //see if the email is not put correctly
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mailDisplay.setError("Invalid email");
            mailDisplay.requestFocus();
            return;
        }

        //set the new data
        student.setEmail(email);
        student.setPassword(pwd);
        student.setUsername(username);

        //update the student
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

    /**
     * what to do after update or create data
     * @param response
     */
    private void setResponse(Boolean response){
        if(response){
            //display a little message to say that the profile is modified
            Toast toast = Toast.makeText(this,"Student modified",Toast.LENGTH_LONG);
            toast.show();

            //go to MyProfile with the username of the user
            Intent intent = new Intent(ModifiyProfile.this, MyProfile.class);
            intent.putExtra("email", email);
            startActivity(intent);
        }
        else{
            mailDisplay.setError("Email already used");
            mailDisplay.requestFocus();
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
                Intent intent = new Intent(ModifiyProfile.this, MyProfile.class);
                intent.putExtra("email", email);
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
