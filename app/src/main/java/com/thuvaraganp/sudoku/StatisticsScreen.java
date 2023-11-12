package com.thuvaraganp.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class StatisticsScreen extends AppCompatActivity {
    String convertToText(long elapsedTime) {
        long seconds = elapsedTime / 1000;
        long minutes = seconds / 60;
        seconds %= 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_screen);
        BestTimeManager.loadFromFile(this);
        long easyTime = BestTimeManager.loadBestTime("1");
        long mediumTime = BestTimeManager.loadBestTime("2");
        long hardTime = BestTimeManager.loadBestTime("3");

        String easy = convertToText(easyTime);
        String medium = convertToText(mediumTime);
        String hard = convertToText(hardTime);

        TextView easyText = findViewById(R.id.easyScore);
        easyText.setText(easy);

        TextView mediumText = findViewById(R.id.mediumScore);
        mediumText.setText(medium);

        TextView hardText = findViewById(R.id.hardScore);
        hardText.setText(hard);

        Button back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            finish();
        });

    }
}