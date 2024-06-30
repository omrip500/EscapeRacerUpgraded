package com.example.EscapeRacer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    private Button buttonModeSlow;
    private Button buttonModeFast;
    private Button sensorMode;

    private Button high_scores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        findViews();
        initViews();
    }

    private void findViews() {
        buttonModeSlow = findViewById(R.id.button_mode_slow);
        buttonModeFast = findViewById(R.id.button_mode_fast);
        sensorMode = findViewById(R.id.sensor_mode);
        high_scores = findViewById(R.id.high_scores);
    }

    private void initViews() {
        buttonModeSlow.setOnClickListener(v -> {
            Intent gameIntent = new Intent(MenuActivity.this, MainActivity.class);
            gameIntent.putExtra("GAME_TYPE", 0);
            startActivity(gameIntent);
            finish();
        });

        buttonModeFast.setOnClickListener(v -> {
            Intent gameIntent = new Intent(MenuActivity.this, MainActivity.class);
            gameIntent.putExtra("GAME_TYPE", 1);
            startActivity(gameIntent);
            finish();
        });

        sensorMode.setOnClickListener(v -> {
            Intent gameIntent = new Intent(MenuActivity.this, MainActivity.class);
            gameIntent.putExtra("GAME_TYPE", 2);
            startActivity(gameIntent);
            finish();
        });

        high_scores.setOnClickListener(v -> {
            Intent highScoresIntent = new Intent(MenuActivity.this, HighScoresActivity.class);
            startActivity(highScoresIntent);
            finish();
        });
    }
}
