package com.example.EscapeRacer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button buttonModeSlow;
    private Button buttonModeFast;
    private Button sensorMode;

    private Intent gameIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.gameIntent = new Intent(this, GameActivity.class);

        findViews();
        initViews();
    }

    private void findViews() {
        buttonModeSlow = findViewById(R.id.button_mode_slow);
        buttonModeFast = findViewById(R.id.button_mode_fast);
        sensorMode = findViewById(R.id.sensor_mode);
    }

    private void initViews() {
        buttonModeSlow.setOnClickListener( v-> {
            gameIntent.putExtra("GAME_TYPE", 0);
            startActivity(gameIntent);
            finish();
        });

        buttonModeFast.setOnClickListener( v-> {
            gameIntent.putExtra("GAME_TYPE", 1);
            startActivity(gameIntent);
            finish();
        });

        sensorMode.setOnClickListener( v-> {
            gameIntent.putExtra("GAME_TYPE", 2);
            startActivity(gameIntent);
            finish();
        });
    }
}
