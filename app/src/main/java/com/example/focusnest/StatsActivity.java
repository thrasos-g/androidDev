package com.example.focusnest;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class StatsActivity extends AppCompatActivity {

    TextView steak,time,comp,pcc,tbt;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Toolbar toolbar = findViewById(R.id.statistics_toolbar);
        setSupportActionBar(toolbar);

        // Enable the back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String name = getIntent().getStringExtra("user_name");
        String profile = getIntent().getStringExtra("selected_profile");
        User user=new User(10,"test");


        //UI stuff
        steak=findViewById(R.id.streak);
        time=findViewById(R.id.time);
        comp=findViewById(R.id.comp);
        pcc=findViewById(R.id.pcc);
        tbt=findViewById(R.id.tbt);

        steak.setText(String.valueOf(user.getStreak()));
        time.setText(format(user.getTotalStudySeconds()));
        comp.setText(String.valueOf(user.getPomodorosCompleted()));
        pcc.setText(String.valueOf(user.getPomodoroCyclesCompleted()));
        tbt.setText(format(user.getTotalBreakSeconds()));

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();  // Return to the previous activity
        return true;
    }

    public static String format(int totalSeconds){
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}