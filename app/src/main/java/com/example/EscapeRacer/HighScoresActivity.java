package com.example.EscapeRacer;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.example.EscapeRacer.Fragments.HighScoresFragment;
import com.example.EscapeRacer.Fragments.MapFragment;
import com.example.EscapeRacer.Interfaces.OnHighScoreClickListener;

import java.util.Objects;

public class HighScoresActivity extends AppCompatActivity implements OnHighScoreClickListener {

    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Fragment highScoresFragment = new HighScoresFragment();
        mapFragment = new MapFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.high_scores_fragment, highScoresFragment)
                .replace(R.id.map_fragment, mapFragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MenuActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onHighScoreClick(double latitude, double longitude) {
        if (mapFragment != null) {
            mapFragment.focusOnLocation(latitude, longitude);
        }
    }
}
