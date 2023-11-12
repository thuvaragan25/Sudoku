package com.thuvaraganp.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class DifficultyScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty_screen);

        Button easy = (Button) findViewById(R.id.easy);
        easy.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("difficulty", 1);
            intent.putExtra("loadBoard", 0);
            startActivity(intent);
        });

        Button medium = (Button) findViewById(R.id.medium);
        medium.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("difficulty", 2);
            intent.putExtra("loadBoard", 0);
            startActivity(intent);
        });

        Button hard = (Button) findViewById(R.id.hard);
        hard.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("difficulty", 3);
            intent.putExtra("loadBoard", 0);
            startActivity(intent);
        });

        Button exit = (Button) findViewById(R.id.exit);
        exit.setOnClickListener(v -> {
            finish();
        });
    }
}