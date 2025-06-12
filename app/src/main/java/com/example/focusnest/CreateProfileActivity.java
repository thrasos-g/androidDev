package com.example.focusnest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateProfileActivity extends AppCompatActivity {

    private EditText profileNameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        profileNameInput = findViewById(R.id.profile_name_input);
        Button saveButton = findViewById(R.id.save_profile_button);


        MyDBHandler dbHandler=new MyDBHandler(this,null,null,1);

        saveButton.setOnClickListener(v -> {
            String name = profileNameInput.getText().toString().trim();
            if (!name.isEmpty()) {
                User user= new User(name);
                dbHandler.addUser(user);
                Toast.makeText(this, "User saved!", Toast.LENGTH_SHORT).show();
                finish(); // Go back to main screen
            } else {
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
