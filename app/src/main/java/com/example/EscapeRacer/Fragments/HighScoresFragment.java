package com.example.EscapeRacer.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.EscapeRacer.R;
import com.example.EscapeRacer.Utilities.SharedPreferencesManager;
import com.example.EscapeRacer.Interfaces.OnHighScoreClickListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class HighScoresFragment extends Fragment {

    private LinearLayout scoresContainer;
    private SharedPreferencesManager sharedPreferencesManager;
    private OnHighScoreClickListener highScoreClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_high_scores, container, false);
        scoresContainer = view.findViewById(R.id.scores_container);
        sharedPreferencesManager = new SharedPreferencesManager(requireContext());
        if (getActivity() instanceof OnHighScoreClickListener) {
            highScoreClickListener = (OnHighScoreClickListener) getActivity();
        }
        displayHighScores();
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void displayHighScores() {
        List<String> highScores = sharedPreferencesManager.getHighScores();
        for (int i = 0; i < highScores.size(); i++) {
            String highScore = highScores.get(i);
            String[] parts = highScore.split(":");
            int duration = Integer.parseInt(parts[0]);
            String[] locationParts = parts[1].split(",");
            double latitude = Double.parseDouble(locationParts[0]);
            double longitude = Double.parseDouble(locationParts[1]);

            MaterialCardView cardView = new MaterialCardView(requireContext());
            cardView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            cardView.setCardElevation(8);
            cardView.setPadding(16, 16, 16, 16);
            cardView.setRadius(16);
            cardView.setCardBackgroundColor(getResources().getColor(android.R.color.white, null));

            MaterialTextView scoreView = new MaterialTextView(requireContext());
            scoreView.setText((i + 1) + ". Distance: " + duration);
            scoreView.setTextSize(18);
            scoreView.setPadding(8, 8, 8, 8);
            scoreView.setTextColor(getResources().getColor(android.R.color.black, null));
            scoreView.setOnClickListener(v -> {
                if (highScoreClickListener != null) {
                    highScoreClickListener.onHighScoreClick(latitude, longitude);
                }
            });

            cardView.addView(scoreView);
            scoresContainer.addView(cardView);
        }
    }
}
