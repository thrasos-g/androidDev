package com.example.focusnest;


import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;



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


        User user1 = (User) getIntent().getSerializableExtra("selected_user");

        //UI

        Set =findViewById(R.id.Set);
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

        // Get current values from DB and project last put values to user as hints

        int currentStudyMins = (user1.getStudyTime()/60);
        int currentSmallMins =( user1.getBreakSmall()/60);
        int currentBigMins = (user1.getBreakLarge()/60);
        int currentStudySecs = (user1.getStudyTime()%60);
        int currentSmallSecs =( user1.getBreakSmall()%60);
        int currentBigSecs = (user1.getBreakLarge()%60);
        minutes_study.setHint(String.valueOf(currentStudyMins)+" minutes");
        seconds_study.setHint(String.valueOf(currentStudySecs)+" seconds");
        minutes_small.setHint(String.valueOf(currentSmallMins)+" minutes");
        seconds_small.setHint(String.valueOf(currentSmallSecs)+" seconds");
        minutes_big.setHint(String.valueOf(currentBigMins)+" minutes");
        seconds_big.setHint(String.valueOf(currentBigSecs)+" seconds");

        Log.d("USER_OBJECT", " study time: " + user1.getStudyTime());
        Log.d("USER_OBJECT", " small break: " + user1.getBreakSmall());
        Log.d("USER_OBJECT", " big break: " + user1.getBreakLarge());



        Set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //try {
                    // Get study time
                    int studyMin = getValueOrZero(minutes_study,currentStudyMins);
                    int studySec = getValueOrZero(seconds_study,currentStudySecs);
                    // Get small break
                    int smallMin = getValueOrZero(minutes_small,currentSmallMins);
                    int smallSec = getValueOrZero(seconds_small,currentSmallSecs);
                    // Get big brake
                    int bigMin = getValueOrZero(minutes_big,currentBigMins);
                    int bigSec = getValueOrZero(seconds_big,currentBigSecs);

                    Log.d("INPUT_VALUES", "Study: " + studyMin + "m " + studySec + "s");
                    Log.d("INPUT_VALUES", "Small: " + smallMin + "m " + smallSec + "s");
                    Log.d("INPUT_VALUES", "Big: " + bigMin + "m " + bigSec + "s");
                    //set new values
                    user1.setBreakLarge(bigMin * 60 + bigSec);
                    user1.setStudyTime(studyMin * 60 + studySec);
                    user1.setBreakSmall(smallMin * 60 + smallSec);


                    // Save to DB
                    MyDBHandler dbHandler = new MyDBHandler(SettingsActivity.this, null, null, 1);
                    dbHandler.updateUser(user1);

                    Toast.makeText(SettingsActivity.this, "Times updated successfully!", Toast.LENGTH_SHORT).show();
               // } catch (Exception e) {
                 //   Toast.makeText(SettingsActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
               // }
                Log.d("USER_OBJECT", "Updated study time: " + user1.getStudyTime());
                Log.d("USER_OBJECT", "Updated small break: " + user1.getBreakSmall());
                Log.d("USER_OBJECT", "Updated big break: " + user1.getBreakLarge());
                // set new values as hints again.
                int currentStudy = user1.getStudyTime();
                int currentSmall = user1.getBreakSmall();
                int currentBig = user1.getBreakLarge();
                minutes_study.setHint(String.valueOf(currentStudy/60)+" minutes");
                seconds_study.setHint(String.valueOf(currentStudy%60)+" seconds");
                minutes_small.setHint(String.valueOf(currentSmall/60)+" minutes");
                seconds_small.setHint(String.valueOf(currentSmall%60)+" seconds");
                minutes_big.setHint(String.valueOf(currentBig/60)+" minutes");
                seconds_big.setHint(String.valueOf(currentBig%60)+" seconds");

            }
        });


        switch1.setChecked(DoNotDisturbHelper.isDoNotDisturbOn(this));
        switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            DoNotDisturbHelper.setDoNotDisturb(SettingsActivity.this, isChecked);

        });

    }
    //request permission to listen
    private void requestNotificationListenerPermission() {
        if (!isNotificationServiceEnabled()) {
            Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            startActivity(intent);
        }
    }

    private boolean isNotificationServiceEnabled() {
        String packageName = getPackageName();
        String flat = Settings.Secure.getString(getContentResolver(),
                "enabled_notification_listeners");
        return flat != null && flat.contains(packageName);
    }


    private int getValueOrZero(EditText editText, int lastvalue) { // parses the new value as integer to user or returns the same value if user leaves blank answer

        String input = editText.getText().toString().trim();
        if (input.isEmpty()) {
            return lastvalue; // treat empty input as fallback
        }
        return Integer.parseInt(input);
    }

}