package com.example.focusnest;

import android.content.Intent;
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

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Spinner profileSpinner = findViewById(R.id.profile_spinner);
        List<Profile> profiles = ProfileManager.getInstance(this).getAllProfiles();

        ArrayAdapter<Profile> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,profiles);
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
        EditText nameInput = findViewById(R.id.editTextText);
        Spinner profileSpinner = findViewById(R.id.profile_spinner);

        String userName = nameInput.getText().toString().trim();
        String selectedProfile = profileSpinner.getSelectedItem().toString();

        if (!userName.isEmpty()) {
            // Save name (optional)
            getSharedPreferences("FocusNestPrefs", MODE_PRIVATE)
                    .edit()
                    .putString("user_name", userName)
                    .apply();

            // Start HomeActivity with data
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.putExtra("user_name", userName);
            intent.putExtra("selected_profile", selectedProfile);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        }
    }

}
