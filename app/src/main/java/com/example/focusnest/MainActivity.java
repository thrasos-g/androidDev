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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        MyDBHandler dbHandler = new MyDBHandler(this,null,null,1);

        //Put saved users in spinner
        Spinner profileSpinner = findViewById(R.id.profile_spinner);
        List<User> users = dbHandler.getAllUsers();

        ArrayAdapter<User> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profileSpinner.setAdapter(adapter);


        // Enable toolbar
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        // Handle window insets (optional, from your code)
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

        // Start HomeActivity with data
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.putExtra("selected_user",(Serializable) selectedUser);
        startActivity(intent);
        finish();
    }

}
