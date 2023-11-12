package com.thuvaraganp.sudoku;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import java.io.File;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Button solve = (Button) findViewById(R.id.solve);
        solve.setOnClickListener(v -> {
            Intent intent = new Intent(this, SudokuSolver.class);
            startActivity(intent);
        });
        Button play = (Button) findViewById(R.id.play);
        play.setOnClickListener(v -> {
            Intent intent = new Intent(this, DifficultyScreen.class);
            startActivity(intent);
        });

        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(v -> {
            File file = new File(getFilesDir(),"board.txt");
            if (file.exists()) {
                System.out.println("Exists");
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("difficulty", -1);
                intent.putExtra("loadBoard", 1);
                startActivity(intent);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage("No board is saved!");
                builder.setTitle("Not saved!");

                builder.setCancelable(true);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });
        Button statistics = (Button) findViewById(R.id.statistics);
        statistics.setOnClickListener(v -> {
            Intent intent = new Intent(this, StatisticsScreen.class);
            startActivity(intent);
        });
    }
}