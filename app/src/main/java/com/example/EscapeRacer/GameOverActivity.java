package com.example.EscapeRacer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.EscapeRacer.Utilities.SharedPreferencesManager;
import com.google.android.material.textview.MaterialTextView;

public class GameOverActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private Button btn_main_menu;
    private MaterialTextView scoreLBLStatus;
    private MaterialTextView scoreLBLCoin;
    private MaterialTextView score_LBL_duration;
    private int coinsCollected;
    private int duration;
    private LocationManager locationManager;
    private SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        Intent intent = getIntent();
        this.coinsCollected = intent.getIntExtra("COINS_COLLECTED", 0);
        this.duration = intent.getIntExtra("DURATION", 0);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        sharedPreferencesManager = new SharedPreferencesManager(this);
        findViews();
        initViews();
        saveHighScoreWithLocation();
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

    private void saveHighScoreWithLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLastLocationAndSave();
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocationAndSave() {
        Log.d("Location", "Requesting network location updates");
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                sharedPreferencesManager.saveHighScore(duration, latitude, longitude);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocationAndSave();
            }
        }
    }
}
