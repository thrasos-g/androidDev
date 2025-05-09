package com.example.focusnest;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private TextView timerText, pomodoroCountText;
    private Button startPauseButton, resetButton, breakButton, skipButton;
    private CountDownTimer timer;
    private boolean isTimerRunning = false;
    private boolean isBreak = false;
    private int pomodoroCount = 0;
    private boolean isPaused = false; // track pause state


    private long timeLeftInMillis = 25 * 60 * 1000; // default to 25 mins

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        timerText = findViewById(R.id.timer_text);
        pomodoroCountText = findViewById(R.id.pomodoro_count_text);
        startPauseButton = findViewById(R.id.start_pause_button);
        resetButton = findViewById(R.id.reset_button);
        breakButton = findViewById(R.id.break_button);
        skipButton = findViewById(R.id.skip_button);

        startPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBreak) {
                    if (timer != null)
                        timer.cancel();
                    isBreak = false;
                    timeLeftInMillis = 25 * 60 * 1000;
                    updateTimerText();
                    startTimer();
                    startPauseButton.setText("Pause");
                    breakButton.setText("Break");
                } else {
                    if (isTimerRunning) {
                        pauseTimer();
                        startPauseButton.setText("Start");
                    } else {
                        startTimer();
                        startPauseButton.setText("Pause");
                    }
                }
            }
        });


        //resetButton.setOnClickListener(v -> resetTimer());
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timer != null) timer.cancel();
                isBreak = false;
                isTimerRunning = false;
                timeLeftInMillis = 25 * 60 * 1000;
                updateTimerText();
                startPauseButton.setText("Start");
                breakButton.setText("Break");
            }
        });

        breakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isBreak) {
                    // Start a new break
                    if (timer != null) timer.cancel();

                    isBreak = true;
                    isPaused = false;

                    timeLeftInMillis = (pomodoroCount != 0 && pomodoroCount % 4 == 0)
                            ? 15 * 60 * 1000
                            : 5 * 60 * 1000;

                    updateTimerText();
                    startTimer();
                    breakButton.setText("Pause Break");
                    startPauseButton.setText("Pause");
                } else {
                    // Pause or Resume break
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
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBreak) {
                    // Don't skip during a break
                    return;
                }

                if (timer != null) timer.cancel();

                pomodoroCount++;
                pomodoroCountText.setText("Pomodoros: " + pomodoroCount);

                isBreak = true;
                isPaused = false;

                timeLeftInMillis = (pomodoroCount % 4 == 0)
                        ? 15 * 60 * 1000
                        : 5 * 60 * 1000;

                updateTimerText();
                startTimer();
                breakButton.setText("Pause Break");
                startPauseButton.setText("Pause");
            }
        });


        String name = getSharedPreferences("FocusNestPrefs", MODE_PRIVATE)
                .getString("user_name", "Guest");

        //TextView greeting = findViewById(R.id.greetingText);
        //greeting.setText("Welcome, " + name + "!");
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

                // Reset to default 25 minutes after any session
                isBreak = false;
                timeLeftInMillis = 25 * 60 * 1000;
                updateTimerText();

                startPauseButton.setText("Start");
                breakButton.setText("Break");
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


    private void resetTimer() {
        if (timer != null) {
            timer.cancel();
        }
        isBreak = false;
        isTimerRunning = false;
        timeLeftInMillis = 25 * 60 * 1000;
        updateTimerText();
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timerText.setText(timeFormatted);
    }



}
