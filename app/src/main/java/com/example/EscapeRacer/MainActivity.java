package com.example.EscapeRacer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.example.EscapeRacer.Interfaces.MoveCallback;
import com.example.EscapeRacer.Logic.GameManager;
import com.example.EscapeRacer.Utilities.MoveDetector;
import com.example.EscapeRacer.Utilities.SoundPlayer;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private final int numRows = 9;
    private final int numCols = 5;
    private ImageView[][] rocksMatrix;
    private AppCompatImageView[] main_IMG_hearts;
    private ExtendedFloatingActionButton leftArrow;
    private ExtendedFloatingActionButton rightArrow;
    private ImageView carLeft1;
    private ImageView carLeft2;
    private ImageView carMiddle;
    private ImageView carRight1;
    private ImageView carRight2;
    private GameManager gameManager;
    private MoveDetector moveDetector;

    private MaterialTextView coinCount;


    private MaterialTextView duration_text;

    private static long delay;

    final Handler handler = new Handler();

    private SoundPlayer musicSoundPlayer;

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

        int gameType = getIntent().getIntExtra("GAME_TYPE", 0);

        if(gameType == 0)
            delay = 1000L;

        else if(gameType == 1) {
            delay = 500L;
        }

        else {
            delay = 1000L;
            leftArrow.setVisibility(View.INVISIBLE);
            rightArrow.setVisibility(View.INVISIBLE);
            initMoveDetector();
            moveDetector.start();
        }

        gameManager = new GameManager();
        startGame();
    }

    private void initMoveDetector() {
        moveDetector = new MoveDetector(this,
                new MoveCallback() {
                    @Override
                    public void moveRight() {
                        moveCar("right");
                    }

                    @Override
                    public void moveLeft() {
                        moveCar("left");
                    }
                });
    }


    private void findViews() {
        duration_text = findViewById(R.id.duration_text);
        coinCount = findViewById(R.id.coin_count);
        leftArrow = findViewById(R.id.leftArrow);
        rightArrow = findViewById(R.id.rightArrow);
        carLeft1 = findViewById(R.id.carLeft1);
        carLeft2 = findViewById(R.id.carLeft2);
        carMiddle = findViewById(R.id.carMiddle);
        carRight1 = findViewById(R.id.carRight1);
        carRight2 = findViewById(R.id.carRight2);

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
        rocksMatrix[numRows - 1][0] = carLeft1;
        rocksMatrix[numRows - 1][1] = carLeft2;
        rocksMatrix[numRows - 1][2] = carMiddle;
        rocksMatrix[numRows - 1][3] = carRight1;
        rocksMatrix[numRows - 1][4] = carRight2;
    }

    private void initViews() {
        leftArrow.setOnClickListener(v -> moveCar("left"));
        rightArrow.setOnClickListener(v -> moveCar("right"));
        carLeft1.setImageResource(R.drawable.empty);
        carLeft2.setImageResource(R.drawable.empty);
        carRight1.setImageResource(R.drawable.empty);
        carRight2.setImageResource(R.drawable.empty);

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
        String collisionType = "";
        int currentColumn = gameManager.getVisibleCarColumn();
        int newColumn = currentColumn;

        if (Objects.equals(direction, "left")) {
            if (currentColumn > 0) {
                newColumn = currentColumn - 1;
            }
        } else if (Objects.equals(direction, "right")) {
            if (currentColumn < numCols - 1) {
                newColumn = currentColumn + 1;
            }
        }

        if (isRock(rocksMatrix[numRows - 1][newColumn])) {
            collisionType = "rock";
            collision = true;
        } else {
            if(isCoin(rocksMatrix[numRows - 1][newColumn])) {
                collisionType = "coin";
                collision = true;
            }
        }

        for (int i = 0; i < numCols; i++) {
            rocksMatrix[numRows - 1][i].setImageResource(R.drawable.empty);
        }

        rocksMatrix[numRows - 1][newColumn].setImageResource(R.drawable.car2);
        gameManager.setVisibleCarColumn(newColumn);

        if (collision) {
            handleCollision(collisionType);
            refreshUI();
        }
    }




    private void handleCollision(String type) {
        if(Objects.equals(type, "rock")) {
            SoundPlayer crashSoundPlayer = new SoundPlayer(this);
            crashSoundPlayer.playSound(R.raw.crash);
            toastAndVibrate();
            gameManager.setNumOfCollisions(gameManager.getNumOfCollisions() + 1);
            gameManager.setLife(gameManager.getLife() - 1);
        } else if(Objects.equals(type, "coin")) {
            SoundPlayer coinSoundPlayer = new SoundPlayer(this);
            coinSoundPlayer.playSound(R.raw.coin_sound);
            gameManager.increaseProfit();
        }
    }

    private void updateGame() {
        gameManager.increaseDistance();
        Random random = new Random();
        for (int i = numRows - 1; i >= 0; i--) {
            for (int j = 0; j < numCols; j++) {
                if (isRock(rocksMatrix[i][j])) {
                    if (i != numRows - 1) {
                        rocksMatrix[i][j].setImageResource(R.drawable.empty);
                        rocksMatrix[i + 1][j].setImageResource(R.drawable.rock3);
                    } else {
                        if (!(gameManager.getVisibleCarColumn() == 0 && j == 0 ||
                                gameManager.getVisibleCarColumn() == 1 && j == 1 ||
                                gameManager.getVisibleCarColumn() == 2 && j == 2 ||
                                gameManager.getVisibleCarColumn() == 3 && j == 3 ||
                                gameManager.getVisibleCarColumn() == 4 && j == 4)) {
                            rocksMatrix[i][j].setImageResource(R.drawable.empty);
                            gameManager.setCurrentRocksCount(gameManager.getCurrentRocksCount() - 1);
                        }
                    }
                }
            }
        }

        for (int i = numRows - 1; i >= 0; i--) {
            for (int j = 0; j < numCols; j++) {
                if (isCoin(rocksMatrix[i][j])) {
                    if (i != numRows - 1) {
                        rocksMatrix[i][j].setImageResource(R.drawable.empty);
                        rocksMatrix[i + 1][j].setImageResource(R.drawable.coin);
                    } else {
                        if (!(gameManager.getVisibleCarColumn() == 0 && j == 0 ||
                                gameManager.getVisibleCarColumn() == 1 && j == 1 ||
                                gameManager.getVisibleCarColumn() == 2 && j == 2 ||
                                gameManager.getVisibleCarColumn() == 3 && j == 3 ||
                                gameManager.getVisibleCarColumn() == 4 && j == 4)) {
                            rocksMatrix[i][j].setImageResource(R.drawable.empty);
                            gameManager.setCurrentRocksCount(gameManager.getCurrentRocksCount() - 1);
                        }
                    }
                }
            }
        }

        checkCollision();

        int randomNumberForCol;
        int randomNumberForCoin = random.nextInt(4) + 1;

        if(gameManager.getUpdatingNumber() == 1){
            randomNumberForCol = random.nextInt(numCols);
            gameManager.setFirst(randomNumberForCol);
            gameManager.setUpdatingNumber(gameManager.getUpdatingNumber() + 1);
        }

        else if(gameManager.getUpdatingNumber() == 2) {
            do {
                randomNumberForCol = random.nextInt(numCols);
            } while(randomNumberForCol == gameManager.getFirst());
            gameManager.setSecond(randomNumberForCol);
            gameManager.setUpdatingNumber(gameManager.getUpdatingNumber() + 1);
        }

        else {
            do {
                randomNumberForCol = random.nextInt(numCols);
            } while (randomNumberForCol == gameManager.getSecond()
                    || gameManager.getFirst() == 0 && gameManager.getSecond() == 1 && randomNumberForCol == 2
                    || gameManager.getFirst() == 2 && gameManager.getSecond() == 1 && randomNumberForCol == 0);

            gameManager.setFirst(gameManager.getSecond());
            gameManager.setSecond(randomNumberForCol);
        }

        if(randomNumberForCoin == 2 )
            rocksMatrix[0][randomNumberForCol].setImageResource(R.drawable.coin);
        else {
            rocksMatrix[0][randomNumberForCol].setImageResource(R.drawable.rock3);
            gameManager.setCurrentRocksCount(gameManager.getCurrentRocksCount() + 1);
        }

        refreshUI();

    }



    private boolean isCoin(ImageView imageView) {
        return imageView.getDrawable() != null &&
                Objects.equals(imageView.getDrawable().getConstantState(), Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.coin)).getConstantState());
    }

    private boolean isRock(ImageView imageView) {
        return imageView.getDrawable() != null &&
                Objects.equals(imageView.getDrawable().getConstantState(), Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.rock3)).getConstantState());
    }


    private void startGame() {
        Random random = new Random();
        int randomNumber = random.nextInt(3);
        rocksMatrix[0][randomNumber].setImageResource(R.drawable.rock3);
        handler.postDelayed(runnable, delay);
        gameManager.setGameRunning(true);
    }

    private void checkCollision() {
        boolean collision = false;
        String type = "";

        int currentColumn = gameManager.getVisibleCarColumn();

        if (isRock(rocksMatrix[numRows - 1][gameManager.getVisibleCarColumn()])) {
            collision = true;
            type = "rock";
            Log.d("Collision", "Rock Collision: ");
            rocksMatrix[numRows - 1][currentColumn].setImageResource(R.drawable.car2);
        }
        else if(isCoin(rocksMatrix[numRows - 1][gameManager.getVisibleCarColumn()])) {
            collision = true;
            type = "coin";
            rocksMatrix[numRows - 1][currentColumn].setImageResource(R.drawable.car2);
        }

        if (collision) {
            handleCollision(type);
        }
    }


    @SuppressLint("SetTextI18n")
    private void refreshUI() {
        if (gameManager.isGameLost()) {
            changeActivity();
        } else if (gameManager.getLife() != 3) {
            int index = main_IMG_hearts.length - gameManager.getNumOfCollisions();
            main_IMG_hearts[index].setImageResource(R.drawable.empty);
        }
        duration_text.setText("Distance: " + gameManager.getDistance());
        coinCount.setText(": " + gameManager.getProfit());
    }

    private void changeActivity() {
        Intent gameOverIntent = new Intent(this, GameOverActivity.class);
        gameOverIntent.putExtra("COINS_COLLECTED", gameManager.getProfit());
        gameOverIntent.putExtra("DISTANCE", gameManager.getDistance());
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
        musicSoundPlayer = new SoundPlayer((this));
        musicSoundPlayer.playSound(R.raw.fade);
        super.onResume();
        if(moveDetector != null)
            moveDetector.start();
        if (!gameManager.isGameRunning()) {
            handler.postDelayed(runnable, delay);
            gameManager.setGameRunning(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        musicSoundPlayer.stopSound();
        if(moveDetector !=null)
            moveDetector.stop();
        if (gameManager.isGameRunning()) {
            handler.removeCallbacks(runnable);
            gameManager.setGameRunning(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gameManager.isGameRunning()) {
            handler.removeCallbacks(runnable);
            gameManager.setGameRunning(false);
        }
    }
}
