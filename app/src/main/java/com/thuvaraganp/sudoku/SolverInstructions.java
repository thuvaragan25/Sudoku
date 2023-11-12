package com.thuvaraganp.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class SolverInstructions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solver_instructions);

        Button b = findViewById(R.id.back);
        b.setOnClickListener(v -> {
            finish();
        });
    }
}