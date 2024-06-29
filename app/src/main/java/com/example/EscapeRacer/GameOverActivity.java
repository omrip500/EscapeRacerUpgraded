package com.example.EscapeRacer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textview.MaterialTextView;

public class GameOverActivity extends AppCompatActivity {
    private Button btn_main_menu;
    private MaterialTextView scoreLBLStatus;
    private MaterialTextView scoreLBLCoin;
    private MaterialTextView score_LBL_duration;
    private int coinsCollected;
    private int duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        Intent intent = getIntent();
        this.coinsCollected = intent.getIntExtra("COINS_COLLECTED", 0);
        this.duration = intent.getIntExtra("DURATION", 0);
        findViews();
        initViews();
    }

    private void findViews() {
        this.btn_main_menu = findViewById(R.id.btn_main_menu);
        this.scoreLBLStatus = findViewById(R.id.score_LBL_status);
        this.scoreLBLCoin = findViewById(R.id.score_LBL_coins);
        this.score_LBL_duration = findViewById(R.id.score_LBL_duration);
    }

    @SuppressLint("SetTextI18n")
    private void initViews() {
        btn_main_menu.setOnClickListener(v -> {
            Intent intent = new Intent(GameOverActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        });

        scoreLBLStatus.setText("Game Over");
        scoreLBLCoin.setText("Coins collected: " + this.coinsCollected);
        score_LBL_duration.setText("Duration: " + this.duration);
    }
}
