package com.example.focusnest;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;


public class SettingsActivity extends AppCompatActivity
{



    private Button Set;

    private Switch switch1;
    private EditText minutes_study,seconds_study,minutes_big,seconds_big,minutes_small,seconds_small;

    private TextView setStudy , setBig , setSmall,alarmText;

    private ImageView focusnest;





    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);

       // MyDBHandler dbHandler = new MyDBHandler(this,null,null,1);
        Button setButton = findViewById(R.id.Set);

        User user1 = (User) getIntent().getSerializableExtra("selected_user");

        //UI

       // Set =findViewById(R.id.Set);
        switch1 =findViewById(R.id.switch1);
        minutes_big =findViewById(R.id.minutes_big);
        minutes_small =findViewById(R.id.minutes_small);
        seconds_big =findViewById(R.id.seconds_big);
        seconds_small =findViewById(R.id.seconds_small);
        minutes_study =findViewById(R.id.minutes_study);
        seconds_study =findViewById(R.id.seconds_study);
        setSmall = findViewById(R.id.setSmall);
        setStudy = findViewById(R.id.setStudy);
        setBig = findViewById(R.id.setBig);
        alarmText= findViewById(R.id.alarm_text);
        focusnest = findViewById(R.id.focusnest);

        // Get current values from DB and project last put values to user

        int currentStudy = user1.getStudyTime();
        int currentSmall = user1.getBreakSmall();
        int currentBig = user1.getBreakLarge();
        minutes_study.setHint(String.valueOf(currentStudy/60)+" minutes");
        seconds_study.setHint(String.valueOf(currentStudy%60)+" seconds");
        minutes_small.setHint(String.valueOf(currentSmall/60)+" minutes");
        seconds_small.setHint(String.valueOf(currentSmall%60)+" seconds");
        minutes_big.setHint(String.valueOf(currentBig/60)+" minutes");
        seconds_big.setHint(String.valueOf(currentBig%60)+" seconds");






        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //minutes_study = currentStudy;


                // Parse inputs or fallback to current DB values

              /*  int studyMin = getInputOrDefault(minutesStudy, currentStudy[0]);
                int studySec = getInputOrDefault(secondsStudy, currentStudy[1]);

                int smallMin = getInputOrDefault(minutesSmall, currentSmall[0]);
                int smallSec = getInputOrDefault(secondsSmall, currentSmall[1]);

                int bigMin = getInputOrDefault(minutesBig, currentBig[0]);
                int bigSec = getInputOrDefault(secondsBig, currentBig[1]); */

                // Save new values
               // user1.setStudyTime();

                Toast.makeText(SettingsActivity.this, "Times updated successfully111", Toast.LENGTH_SHORT).show();

            }
        });
    }
}