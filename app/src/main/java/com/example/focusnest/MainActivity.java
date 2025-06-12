package com.example.focusnest;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // <- Add this!
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        MyDBHandler dbHandler = new MyDBHandler(this,null,null,1);

        // TEMP: Delete DB
        //deleteDatabase("userDB.db");

        //Put saved users in spinner
        Spinner profileSpinner = findViewById(R.id.profile_spinner);
        List<User> users = dbHandler.getAllUsers();

        ArrayAdapter<User> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profileSpinner.setAdapter(adapter);


        // Enable toolbar
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.create_profile_button).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateProfileActivity.class);
            startActivity(intent);
        });

    }


    public void nextActivity(View view) {



        Spinner profileSpinner = findViewById(R.id.profile_spinner);


        User selectedUser = (User)profileSpinner.getSelectedItem();

        //Streak logic
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = sdf.format(new Date());
        String lastLogin = selectedUser.getLastLoginDate();

        if (lastLogin == null || lastLogin.isEmpty()) {
            selectedUser.setStreak(1); // first login ever
        } else if (lastLogin.equals(today)) {
            // already logged in today → do nothing
        } else {

            try {
                Date lastDate = sdf.parse(lastLogin);
                Date todayDate = sdf.parse(today);

                long diff = todayDate.getTime() - lastDate.getTime();
                long daysDiff = diff / (1000 * 60 * 60 * 24);

                if (daysDiff == 1) {
                    selectedUser.setStreak(selectedUser.getStreak() + 1); // continued streak
                } else {
                    selectedUser.setStreak(1); // missed day → reset
                }
            } catch (ParseException e) {
                // Handle case where stored date is invalid (or corrupted)
                selectedUser.setStreak(1);
            }
        }

// Update last login
        selectedUser.setLastLoginDate(today);
        new MyDBHandler(this, null, null, 1).updateUser(selectedUser);


        // Start HomeActivity with data
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.putExtra("selected_user",(Serializable) selectedUser);
        startActivity(intent);
        finish();
    }

    //updates the spinner after a new user is added in the CreateProfileActivity
    protected void onResume() {
        super.onResume();

        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        Spinner profileSpinner = findViewById(R.id.profile_spinner);
        List<User> users = dbHandler.getAllUsers();

        ArrayAdapter<User> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profileSpinner.setAdapter(adapter);
    }


}
