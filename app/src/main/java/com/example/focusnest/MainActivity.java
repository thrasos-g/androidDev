package com.example.focusnest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // <- Add this!
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Enable toolbar
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        // Handle window insets (optional, from your code)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void nextActivity(View view) {
        EditText nameInput = findViewById(R.id.editTextText);
        String userName = nameInput.getText().toString().trim();

        if (!userName.isEmpty()) {
            // Save the name
            getSharedPreferences("FocusNestPrefs", MODE_PRIVATE)
                    .edit()
                    .putString("user_name", userName)
                    .apply();

            // Launch new activity
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish(); // Optional: close login screen
        } else {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        }
    }
}
