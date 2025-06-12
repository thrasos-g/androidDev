package com.example.focusnest;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
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

import java.io.Serializable;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private TextView timerText, pomodoroCountText;
    private Button startPauseButton, resetButton, breakButton, skipButton, skipBreakButton;
    private CountDownTimer timer;
    private boolean isTimerRunning = false;
    private boolean isBreak = false;
    private boolean isPaused = false;
    private int pomodoroCount = 0;

    private long pomodoroStartTime = 0; // in milliseconds
    private long breakStartTime = 0; // set when a break begins


    private User selectedUser;

    private long timeLeftInMillis = 25 * 60 * 1000; // 25 minutes

    // Declare the break buttons container
    private View breakButtonsContainer;

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
        skipBreakButton = findViewById(R.id.skip_break_button);
        // Initialize the break buttons container
        breakButtonsContainer = findViewById(R.id.break_buttons_container);


        // Get the extras
        selectedUser = (User) getIntent().getSerializableExtra("selected_user");

        // Build welcome message
        String welcomeMessage = "You have chosen the profile \"" + selectedUser.getName() + "\".";

        // Set to TextView
        TextView welcomeText = findViewById(R.id.welcome_text); // Make sure this ID exists in XML
        welcomeText.setText(welcomeMessage);

        // Initial button visibility
        startPauseButton.setVisibility(View.VISIBLE);
        resetButton.setVisibility(View.GONE);
        skipButton.setVisibility(View.GONE);
        // Use the container to hide break buttons initially
        breakButtonsContainer.setVisibility(View.GONE);


        startPauseButton.setOnClickListener(v -> {
            if (isBreak) {
                // If currently in a break, and start/pause is clicked, it means we want to end the break and start a pomodoro
                if (timer != null) timer.cancel();
                isBreak = false; // No longer in break
                timeLeftInMillis = 25 * 60 * 1000; // Reset to 25 minutes pomodoro
                updateTimerText();
                startTimer(); // Start the pomodoro timer

                startPauseButton.setText("Pause"); // Change button text
                breakButton.setText("Break"); // Reset break button text

                // Adjust button visibility for pomodoro session
                startPauseButton.setVisibility(View.VISIBLE);
                resetButton.setVisibility(View.VISIBLE);
                skipButton.setVisibility(View.VISIBLE);
                // Use the container to hide break specific buttons
                breakButtonsContainer.setVisibility(View.GONE);
            } else {
                // If not in break, standard start/pause behavior for pomodoro timer
                if (isTimerRunning) {
                    pauseTimer();
                    startPauseButton.setText("Start");
                } else {
                    startTimer();
                    startPauseButton.setText("Pause");

                    // Show skip and reset buttons now that timer is running
                    resetButton.setVisibility(View.VISIBLE);
                    skipButton.setVisibility(View.VISIBLE);
                }
            }
        });

        resetButton.setOnClickListener(v -> {
            // Cancel any active timer
            if (timer != null) timer.cancel();
            // Reset all timer states
            isTimerRunning = false;
            isPaused = false;
            isBreak = false;

            // Reset timer duration to default pomodoro
            timeLeftInMillis = 25 * 60 * 1000;
            updateTimerText(); // Update UI

            startPauseButton.setText("Start"); // Reset start/pause button text

            // Adjust button visibility after reset
            startPauseButton.setVisibility(View.VISIBLE);
            resetButton.setVisibility(View.GONE); // Hide reset button until start is clicked again
            skipButton.setVisibility(View.GONE);  // Hide skip button until start is clicked again
            // Use the container to hide break specific buttons
            breakButtonsContainer.setVisibility(View.GONE);
        });


        breakButton.setOnClickListener(v -> {
            if (!isBreak) {
                // If not currently in break, initiate a break
                if (timer != null) timer.cancel(); // Cancel current timer
                isBreak = true; // Set state to break
                isPaused = false; // Ensure it's not paused when starting break

                //start counting break time
                breakStartTime = System.currentTimeMillis(); // record break start

                // Determine break duration (long break every 4 pomodoros)
                timeLeftInMillis = (pomodoroCount != 0 && pomodoroCount % 4 == 0) ?
                        15 * 60 * 1000 : 5 * 60 * 1000;

                updateTimerText(); // Update UI with break time
                startTimer(); // Start the break timer

                breakButton.setText("Pause Break"); // Change break button text
                startPauseButton.setText("Pause"); // Not used when in break, but set for consistency if it were visible

                // Adjust button visibility for break session
                startPauseButton.setVisibility(View.GONE); // Hide pomodoro start/pause
                resetButton.setVisibility(View.GONE); // Hide pomodoro reset
                skipButton.setVisibility(View.GONE); // Hide pomodoro skip
                // Use the container to show break specific buttons
                breakButtonsContainer.setVisibility(View.VISIBLE);
            } else {
                // If already in break, toggle pause/resume for the break timer
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
            // This button skips the current pomodoro session and moves to a break.
            if (isBreak) return; // Cannot skip break if already in one

            if (timer != null) timer.cancel(); // Stop current pomodoro timer

            //start counting break seconds
            breakStartTime = System.currentTimeMillis(); // record break start


            //increments total study time
            if (pomodoroStartTime > 0) {
                int elapsed = (int)((System.currentTimeMillis() - pomodoroStartTime) / 1000);
                selectedUser.setTotalStudySeconds(selectedUser.getTotalStudySeconds()+elapsed);
            }

            pomodoroCount++; // Increment pomodoro count as it's being skipped/completed
            selectedUser.setPomodorosCompleted(selectedUser.getPomodorosCompleted()+1);//++pomodoros complete for stats

            //check it see if pomodoro cycles need to be incremented as well
            if (pomodoroCount != 0 && pomodoroCount % 4 == 0){
                selectedUser.setPomodoroCyclesCompleted(selectedUser.getPomodoroCyclesCompleted()+1);
            }

            pomodoroCountText.setText("Pomodoros: " + pomodoroCount); // Update UI

            isBreak = true; // Set state to break
            isPaused = false; // Ensure not paused when starting break
            // Determine break duration
            timeLeftInMillis = (pomodoroCount % 4 == 0) ? 15 * 60 * 1000 : 5 * 60 * 1000;

            updateTimerText(); // Update UI with break time
            startTimer(); // Start the break timer

            breakButton.setText("Pause Break"); // Change break button text

            // Adjust button visibility for break session
            resetButton.setVisibility(View.GONE); // Hide pomodoro reset
            skipButton.setVisibility(View.GONE); // Hide pomodoro skip
            startPauseButton.setVisibility(View.GONE); // Hide pomodoro start/pause
            // Use the container to show break specific buttons
            breakButtonsContainer.setVisibility(View.VISIBLE);
        });

        // OnClickListener for the new skipBreakButton
        skipBreakButton.setOnClickListener(v -> {
            // This button allows skipping the current break session.
            if (timer != null) timer.cancel(); // Stop current break timer


            if (breakStartTime > 0) {
                int elapsed = (int)((System.currentTimeMillis() - breakStartTime) / 1000);
                selectedUser.setTotalBreakSeconds(selectedUser.getTotalBreakSeconds()+elapsed);
                breakStartTime = 0;
            }

            isBreak = false; // No longer in break
            isPaused = false; // Not paused
            isTimerRunning = false; // Not running

            // Reset timer to 25 minutes (Pomodoro) for the next session
            timeLeftInMillis = 25 * 60 * 1000;
            updateTimerText(); // Update UI

            // Adjust button visibility to show only Start button, preparing for a new pomodoro
            startPauseButton.setText("Start");
            startPauseButton.setVisibility(View.VISIBLE);
            resetButton.setVisibility(View.GONE);    // Hide until Start is clicked
            skipButton.setVisibility(View.GONE);     // Hide until Start is clicked

            // Use the container to hide break specific buttons
            breakButtonsContainer.setVisibility(View.GONE);
        });

        FloatingActionButton settingsButton = findViewById(R.id.settings_button);

        settingsButton.setOnClickListener(v -> {
            //temp
            Toast.makeText(HomeActivity.this, "Settings clicked", Toast.LENGTH_SHORT).show();

            // Start the SettingsActivity
            // open settings screen
             Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
             intent.putExtra("selected_user",(Serializable) selectedUser);
             startActivity(intent);
        });

        FloatingActionButton statsButton = findViewById(R.id.stats_button);

        statsButton.setOnClickListener(v -> {

            // Start statsActivity
             Intent intent = new Intent(HomeActivity.this, StatsActivity.class);
             intent.putExtra("selected_user",(Serializable) selectedUser);
             startActivity(intent);
            //finish();
        });
    }

    private void startTimer() {

        if (!isBreak) {
            pomodoroStartTime = System.currentTimeMillis(); // track actual start
        }

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
                    //updates total study time
                    long endTime = System.currentTimeMillis();
                    int elapsedSeconds = (int)((endTime - pomodoroStartTime) / 1000);
                    selectedUser.setTotalStudySeconds(selectedUser.getStudyTime()+elapsedSeconds);

                    // Only increment pomodoro count if the completed session was a pomodoro, not a break.
                    pomodoroCount++;
                    pomodoroCountText.setText("Pomodoros: " + pomodoroCount);
                }


                if (isBreak && breakStartTime > 0) {
                    long endTime = System.currentTimeMillis();
                    int breakElapsed = (int)((endTime - breakStartTime) / 1000);
                    selectedUser.setTotalBreakSeconds(selectedUser.getTotalBreakSeconds()+breakElapsed);
                    breakStartTime = 0;
                }



                // After timer finishes (either pomodoro or break), reset to pomodoro state.
                isBreak = false; // Not in break anymore
                timeLeftInMillis = 25 * 60 * 1000; // Reset to 25 minutes pomodoro
                updateTimerText(); // Update UI

                startPauseButton.setText("Start"); // Reset button text
                breakButton.setText("Break"); // Reset break button text

                // Adjust button visibility for start of a new pomodoro session
                startPauseButton.setVisibility(View.VISIBLE);
                resetButton.setVisibility(View.GONE); // Hide reset button until start is clicked
                skipButton.setVisibility(View.GONE); // Hide skip button until start is clicked
                // Use the container to hide break specific buttons
                breakButtonsContainer.setVisibility(View.GONE);
            }
        }.start();

        isTimerRunning = true; // Set timer running state
    }

    private void pauseTimer() {
        if (timer != null) {

            if (isBreak && breakStartTime > 0) {
                long now = System.currentTimeMillis();
                int breakElapsed = (int)((now - breakStartTime) / 1000);
                selectedUser.setTotalBreakSeconds(selectedUser.getTotalBreakSeconds()+breakElapsed);
                breakStartTime = 0;

            }

            timer.cancel(); // Cancel the timer
            isTimerRunning = false; // Set running state to false
            isPaused = true; // Indicate that it's paused

            if (!isBreak && pomodoroStartTime > 0) {
                long now = System.currentTimeMillis();
                int elapsed = (int) ((now - pomodoroStartTime) / 1000); // seconds
                selectedUser.setTotalStudySeconds(selectedUser.getTotalStudySeconds()+elapsed);
                pomodoroStartTime = 0; // reset for next session

            }
        }
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timerText.setText(timeFormatted);
    }

    //save the changes done to the DB
    protected void onPause() {
        super.onPause();
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        dbHandler.updateUser(selectedUser);
    }

    protected void onStop() {
        super.onStop();
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        dbHandler.updateUser(selectedUser);
    }
}

