package com.example.EscapeRacer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.example.EscapeRacer.Logic.GameManager;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private final int numRows = 8;
    private final int numCols = 3;
    private ImageView[][] rocksMatrix;
    private AppCompatImageView[] main_IMG_hearts;
    private ExtendedFloatingActionButton leftArrow;
    private ExtendedFloatingActionButton rightArrow;
    private ImageView carLeft;
    private ImageView carMiddle;
    private ImageView carRight;
    private GameManager gameManager;

    private static long delay = 1000L;

    final Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, delay);
            updateGame();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initViews();

        if (getIntent().getBooleanExtra("RESET_DATA", false)) {
            resetData();
        }

        gameManager = new GameManager();

        mediaPlayer = MediaPlayer.create(this, R.raw.fade);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        startGame();
        scheduleDelayChanges();
    }

    private void resetData() {
        delay = 1000L;
    }

    private void scheduleDelayChanges() {
        handler.postDelayed(() -> delay = 800L, 21000);
        handler.postDelayed(() -> delay = 700L, 31000);
        handler.postDelayed(() -> delay = 600L, 41000);
        handler.postDelayed(() -> delay = 500L, 56000);
        handler.postDelayed(() -> delay = 400L, 81000);
    }

    private void findViews() {
        leftArrow = findViewById(R.id.leftArrow);
        rightArrow = findViewById(R.id.rightArrow);
        carLeft = findViewById(R.id.carLeft);
        carMiddle = findViewById(R.id.carMiddle);
        carRight = findViewById(R.id.carRight);

        main_IMG_hearts = new AppCompatImageView[]{
                findViewById(R.id.main_IMG_heart1),
                findViewById(R.id.main_IMG_heart2),
                findViewById(R.id.main_IMG_heart3),
        };

        rocksMatrix = new ImageView[numRows][numCols];

        for (int i = 0; i < numRows - 1; i++) {
            for (int j = 0; j < numCols; j++) {
                @SuppressLint("DiscouragedApi") int resID = getResources().getIdentifier("rock" + i + j, "id", getPackageName());
                rocksMatrix[i][j] = findViewById(resID);
            }
        }
        rocksMatrix[7][0] = carLeft;
        rocksMatrix[7][1] = carMiddle;
        rocksMatrix[7][2] = carRight;
    }

    private void initViews() {
        leftArrow.setOnClickListener(v -> moveCar("left"));
        rightArrow.setOnClickListener(v -> moveCar("right"));
        carLeft.setImageResource(R.drawable.empty);
        carRight.setImageResource(R.drawable.empty);

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                rocksMatrix[i][j].setImageResource(R.drawable.empty);
            }
        }
        carMiddle.setImageResource(R.drawable.car2);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void moveCar(String direction) {
        boolean collision = false;

        if (Objects.equals(direction, "left")) {
            if (Objects.equals(gameManager.getVisibleCar(), "middle")) {
                if (rocksMatrix[numRows - 1][0].getDrawable() != null &&
                        Objects.equals(rocksMatrix[numRows - 1][0].getDrawable().getConstantState(), Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.rock3)).getConstantState())) {
                    collision = true;
                }
                carMiddle.setImageResource(R.drawable.empty);
                carLeft.setImageResource(R.drawable.car2);
                gameManager.setVisibleCar("left");
            } else if (Objects.equals(gameManager.getVisibleCar(), "right")) {
                if (rocksMatrix[numRows - 1][1].getDrawable() != null &&
                        Objects.equals(rocksMatrix[numRows - 1][1].getDrawable().getConstantState(), Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.rock3)).getConstantState())) {
                    collision = true;
                }
                carRight.setImageResource(R.drawable.empty);
                carMiddle.setImageResource(R.drawable.car2);
                gameManager.setVisibleCar("middle");
            }
        } else if (Objects.equals(direction, "right")) {
            if (Objects.equals(gameManager.getVisibleCar(), "middle")) {
                if (rocksMatrix[numRows - 1][2].getDrawable() != null &&
                        Objects.equals(rocksMatrix[numRows - 1][2].getDrawable().getConstantState(), Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.rock3)).getConstantState())) {
                    collision = true;
                }
                carMiddle.setImageResource(R.drawable.empty);
                carRight.setImageResource(R.drawable.car2);
                gameManager.setVisibleCar("right");

            } else if (Objects.equals(gameManager.getVisibleCar(), "left")) {
                if (rocksMatrix[numRows - 1][1].getDrawable() != null &&
                        Objects.equals(rocksMatrix[numRows - 1][1].getDrawable().getConstantState(), Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.rock3)).getConstantState())) {
                    collision = true;
                }
                carLeft.setImageResource(R.drawable.empty);
                carMiddle.setImageResource(R.drawable.car2);
                gameManager.setVisibleCar("middle");
            }
        }

        if (collision) {
            handleCollision();
        }
    }

    private void handleCollision() {
        toastAndVibrate();
        gameManager.setNumOfCollisions(gameManager.getNumOfCollisions() + 1);
        gameManager.setLife(gameManager.getLife() - 1);
        refreshUI();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void updateGame() {
        Random random = new Random();
        for (int i = numRows - 1; i >= 0; i--) {
            for (int j = 0; j < numCols; j++) {
                if (rocksMatrix[i][j].getDrawable() != null && Objects.equals(rocksMatrix[i][j].getDrawable().getConstantState(), Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.rock3)).getConstantState())) {
                    if (i != numRows - 1) {
                        if (rocksMatrix[i + 1][j].getDrawable() == null || !Objects.equals(rocksMatrix[i + 1][j].getDrawable().getConstantState(), Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.rock3)).getConstantState())) {
                            rocksMatrix[i][j].setImageResource(R.drawable.empty);
                            rocksMatrix[i + 1][j].setImageResource(R.drawable.rock3);
                        }
                    } else {
                        if (!Objects.equals(gameManager.getVisibleCar(), "left") && j == 0 ||
                                !Objects.equals(gameManager.getVisibleCar(), "middle") && j == 1 ||
                                !Objects.equals(gameManager.getVisibleCar(), "right") && j == 2) {
                            rocksMatrix[i][j].setImageResource(R.drawable.empty);
                            gameManager.setCurrentRocksCount(gameManager.getCurrentRocksCount() - 1);
                        }
                    }
                }
            }
        }

        checkCollision();

        int randomNumber;
        do {
            randomNumber = random.nextInt(numCols);
        } while (randomNumber == gameManager.getLastRockColumn() || gameManager.getBeforeLastRockColumn() == 0 && gameManager.getLastRockColumn() == 1 && randomNumber == 2 || gameManager.getBeforeLastRockColumn() == 2 && gameManager.getLastRockColumn() == 1 && randomNumber == 0);
        rocksMatrix[0][randomNumber].setImageResource(R.drawable.rock3);
        gameManager.setBeforeLastRockColumn(gameManager.getLastRockColumn());
        gameManager.setLastRockColumn(randomNumber);
        gameManager.setCurrentRocksCount(gameManager.getCurrentRocksCount() + 1);
    }

    private void startGame() {
        Random random = new Random();
        int randomNumber = random.nextInt(3);
        rocksMatrix[0][randomNumber].setImageResource(R.drawable.rock3);
        handler.postDelayed(runnable, delay);
        gameManager.setGameRunning(true);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void checkCollision() {
        boolean collision = false;
        if (Objects.equals(gameManager.getVisibleCar(), "left")) {
            if (rocksMatrix[numRows - 1][0].getDrawable() != null &&
                    Objects.equals(rocksMatrix[numRows - 1][0].getDrawable().getConstantState(), Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.rock3)).getConstantState())) {
                collision = true;
                rocksMatrix[numRows - 1][0].setImageResource(R.drawable.car2);
            }
        } else if (Objects.equals(gameManager.getVisibleCar(), "middle")) {
            if (rocksMatrix[numRows - 1][1].getDrawable() != null &&
                    Objects.equals(rocksMatrix[numRows - 1][1].getDrawable().getConstantState(), Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.rock3)).getConstantState())) {
                collision = true;
                rocksMatrix[numRows - 1][1].setImageResource(R.drawable.car2);
            }
        } else if (Objects.equals(gameManager.getVisibleCar(), "right")) {
            if (rocksMatrix[numRows - 1][2].getDrawable() != null &&
                    Objects.equals(rocksMatrix[numRows - 1][2].getDrawable().getConstantState(), Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.rock3)).getConstantState())) {
                collision = true;
                rocksMatrix[numRows - 1][2].setImageResource(R.drawable.car2);
            }
        }

        if (collision) {
            handleCollision();
        }
    }

    private void refreshUI() {
        if (gameManager.isGameLost()) {
            changeActivity();
        } else if (gameManager.getLife() != 0) {
            int index = main_IMG_hearts.length - gameManager.getNumOfCollisions();
            main_IMG_hearts[index].setImageResource(R.drawable.empty);
        }
    }

    private void changeActivity() {
        Intent gameOverIntent = new Intent(this, GameOverActivity.class);
        startActivity(gameOverIntent);
        finish();
    }

    private void toastAndVibrate() {
        vibrate();
        toast();
    }

    private void toast() {
        String toastText = "oops";
        Toast.makeText(this, toastText, Toast.LENGTH_LONG).show();
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
        if (!gameManager.isGameRunning()) {
            handler.postDelayed(runnable, delay);
            gameManager.setGameRunning(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        if (gameManager.isGameRunning()) {
            handler.removeCallbacks(runnable);
            gameManager.setGameRunning(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (gameManager.isGameRunning()) {
            handler.removeCallbacks(runnable);
            gameManager.setGameRunning(false);
        }
    }
}
