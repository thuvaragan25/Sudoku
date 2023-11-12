package com.thuvaraganp.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class GameInstructions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameinstructions);

        Button b = findViewById(R.id.back);
        b.setOnClickListener(v -> {
            finish();
        });
    }
}