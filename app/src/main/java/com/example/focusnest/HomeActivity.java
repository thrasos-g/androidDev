package com.example.focusnest;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private TextView timerText, pomodoroCountText;
    private Button startPauseButton, resetButton, breakButton, skipButton;
    private CountDownTimer timer;
    private boolean isTimerRunning = false;
    private boolean isBreak = false;
    private boolean isPaused = false;
    private int pomodoroCount = 0;

    private long timeLeftInMillis = 25 * 60 * 1000; // 25 minutes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        // UI setup
        timerText = findViewById(R.id.timer_text);
        pomodoroCountText = findViewById(R.id.pomodoro_count_text);
        startPauseButton = findViewById(R.id.start_pause_button);
        resetButton = findViewById(R.id.reset_button);
        breakButton = findViewById(R.id.break_button);
        skipButton = findViewById(R.id.skip_button);
        // Get the extras
        String name = getIntent().getStringExtra("user_name");
        String profile = getIntent().getStringExtra("selected_profile");

        // Build welcome message
        String welcomeMessage = "Hello " + name + "!\nYou have chosen the profile \"" + profile + "\".";

        // Set to TextView
        TextView welcomeText = findViewById(R.id.welcome_text); // Make sure this ID exists in XML
        welcomeText.setText(welcomeMessage);

        // Initial button visibility
        startPauseButton.setVisibility(View.VISIBLE);
        resetButton.setVisibility(View.GONE);
        skipButton.setVisibility(View.GONE);
        breakButton.setVisibility(View.GONE);

        startPauseButton.setOnClickListener(v -> {
            if (isBreak) {
                if (timer != null) timer.cancel();
                isBreak = false;
                timeLeftInMillis = 25 * 60 * 1000;
                updateTimerText();
                startTimer();

                startPauseButton.setText("Pause");
                breakButton.setText("Break");

                startPauseButton.setVisibility(View.VISIBLE);
                resetButton.setVisibility(View.VISIBLE);
                skipButton.setVisibility(View.VISIBLE);
                breakButton.setVisibility(View.GONE);
            } else {
                if (isTimerRunning) {
                    pauseTimer();
                    startPauseButton.setText("Start");
                } else {
                    startTimer();
                    startPauseButton.setText("Pause");

                    // Show skip and reset buttons now
                    resetButton.setVisibility(View.VISIBLE);
                    skipButton.setVisibility(View.VISIBLE);
                }
            }
        });

        resetButton.setOnClickListener(v -> {
            if (timer != null) timer.cancel();
            isTimerRunning = false;
            isPaused = false;
            isBreak = false;

            timeLeftInMillis = 25 * 60 * 1000;
            updateTimerText();

            startPauseButton.setText("Start");

            // Keep start, reset, skip visible
            startPauseButton.setVisibility(View.VISIBLE);
            resetButton.setVisibility(View.VISIBLE);
            skipButton.setVisibility(View.VISIBLE);
            breakButton.setVisibility(View.GONE);
        });


        breakButton.setOnClickListener(v -> {
            if (!isBreak) {
                if (timer != null) timer.cancel();
                isBreak = true;
                isPaused = false;
                timeLeftInMillis = (pomodoroCount != 0 && pomodoroCount % 4 == 0) ?
                        15 * 60 * 1000 : 5 * 60 * 1000;

                updateTimerText();
                startTimer();

                breakButton.setText("Pause Break");
                startPauseButton.setText("Pause");

                startPauseButton.setVisibility(View.GONE);
                resetButton.setVisibility(View.GONE);
                skipButton.setVisibility(View.GONE);
                breakButton.setVisibility(View.VISIBLE);
            } else {
                if (isTimerRunning) {
                    pauseTimer();
                    breakButton.setText("Resume Break");
                    isPaused = true;
                } else {
                    startTimer();
                    breakButton.setText("Pause Break");
                    isPaused = false;
                }
            }
        });

        skipButton.setOnClickListener(v -> {
            if (isBreak) return;

            if (timer != null) timer.cancel();

            pomodoroCount++;
            pomodoroCountText.setText("Pomodoros: " + pomodoroCount);

            isBreak = true;
            isPaused = false;
            timeLeftInMillis = (pomodoroCount % 4 == 0) ? 15 * 60 * 1000 : 5 * 60 * 1000;

            updateTimerText();
            startTimer();

            breakButton.setText("Pause Break");

            resetButton.setVisibility(View.GONE);
            skipButton.setVisibility(View.GONE);
            startPauseButton.setVisibility(View.GONE);
            breakButton.setVisibility(View.VISIBLE);
        });

        FloatingActionButton settingsButton = findViewById(R.id.settings_button);

        settingsButton.setOnClickListener(v -> {
            //temp
            Toast.makeText(HomeActivity.this, "Settings clicked", Toast.LENGTH_SHORT).show();

            // Start the SettingsActivity
            // open settings screen
            // Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            // startActivity(intent);
        });

    }

    private void startTimer() {
        timer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;

                if (!isBreak) {
                    pomodoroCount++;
                    pomodoroCountText.setText("Pomodoros: " + pomodoroCount);
                }

                isBreak = false;
                timeLeftInMillis = 25 * 60 * 1000;
                updateTimerText();

                startPauseButton.setText("Start");
                breakButton.setText("Break");

                startPauseButton.setVisibility(View.VISIBLE);
                resetButton.setVisibility(View.GONE);
                skipButton.setVisibility(View.GONE);
                breakButton.setVisibility(View.GONE);
            }
        }.start();

        isTimerRunning = true;
    }

    private void pauseTimer() {
        if (timer != null) {
            timer.cancel();
            isTimerRunning = false;
            isPaused = true;
        }
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timerText.setText(timeFormatted);
    }
}
