package com.thuvaraganp.sudoku;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class SudokuSolver extends AppCompatActivity {
    TextView tv;
    AlertDialog.Builder builder;

    private class Cell {
        int value; boolean fixed; EditText text;
        public Cell(int initvalue, Context THIS, int i, int j) {
            value = initvalue;
            if (value != 0) fixed = true;
            else fixed = false;
            text = new EditText(THIS);
            ColorStateList colorStateList = ColorStateList.valueOf(Color.WHITE);
            text.setBackgroundTintList(colorStateList);
            if (fixed) text.setText(String.valueOf(value));
            text.setTextColor(Color.WHITE);
            text.setTextSize(28);
            text.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                int before, int count) {
                    if(s.length() > 0) {
                        if(s.toString().charAt(0) >= '1' && s.toString().charAt(0) <= '9' && s.length() == 1) {
                            intTable[i][j] = Integer.parseInt(s.toString());
                        } else {
                            text.setText("");
                            builder.setMessage("Must be a single digit number, from 1-9!");
                            builder.setTitle("Invalid input!");

                            builder.setCancelable(true);

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    }
                }
            });
        }
        public void changeColor() {
            text.setTextColor(Color.BLUE);
        }
    }
    Cell[][] table;
    int[][] intTable;

    boolean solve() {
        int changed[][] = new int[9][9];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (intTable[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(intTable, row, col, num)) {
                            intTable[row][col] = num;
                            changed[row][col] = 1;
                            if (solve()) {
                                return true;
                            }
                            intTable[row][col] = 0;
                            changed[row][col] = 0;
                        }
                    }
                    return false; // No valid number found
                }
            }
        }
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                table[i][j] = new Cell(intTable[i][j], this, i, j);
            }
        }
        return true; // All cells filled
    }

    private static boolean isValid(int[][] board, int row, int col, int num) {
        // Check if the number already exists in the row
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num) {
                return false;
            }
        }

        // Check if the number already exists in the column
        for (int i = 0; i < 9; i++) {
            if (board[i][col] == num) {
                return false;
            }
        }

        // Check if the number already exists in the 3x3 grid
        int gridRow = row - row % 3;
        int gridCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[gridRow + i][gridCol + j] == num) {
                    return false;
                }
            }
        }

        return true; // Number is valid
    }
    boolean correct(int i1, int j1, int i2, int j2) {
        boolean[] seen = new boolean[10];
        for(int i = 0; i <= 9; i++) seen[i] = false; // memcpy
        for(int i = i1; i < i2; i++) {
            for(int j = j1; j < j2; j++) {
                if(table[i][j].value != 0) {
                    if(seen[table[i][j].value]) return false;
                    seen[table[i][j].value] = true;
                }
            }
        }
        return true;
    }

    boolean correct() {
        for(int i = 0; i < 9; i++) {
            if(!correct(i, 0, i+1, 9)) return false;
        }
        for(int j = 0; j < 9; j++) {
            if(!correct(0, j, 9, j+1)) return false;
        }
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                if(!correct(3*i, 3*j, 3*i+3, 3*j+3)) return false;
            }
        }
        return true;
    }

    String input;

    TableLayout tl;
    LinearLayout linlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        input = "? ? ? ? ? ? ? ? ? " +
                "? ? ? ? ? ? ? ? ? " +
                "? ? ? ? ? ? ? ? ? " +
                "? ? ? ? ? ? ? ? ? " +
                "? ? ? ? ? ? ? ? ? " +
                "? ? ? ? ? ? ? ? ? " +
                "? ? ? ? ? ? ? ? ? " +
                "? ? ? ? ? ? ? ? ? " +
                "? ? ? ? ? ? ? ? ? ";


        String[] split = input.split(" ");

       builder = new AlertDialog.Builder(this);

        intTable = new int[9][9];
        table = new Cell[9][9];
        tl = new TableLayout(this);
        for(int i = 0; i < 9; i++) {
            TableRow tr = new TableRow(this);
            for(int j = 0; j < 9; j++) {
                String s = split[i*9+j];
                table[i][j] = new Cell(s.charAt(0) == '?' ? 0 : s.charAt(0) - '0', this, i, j);
                intTable[i][j] = s.charAt(0) == '?' ? 0 : s.charAt(0) - '0';
                tr.addView(table[i][j].text);

            }
            tl.addView(tr);
            tl.setShrinkAllColumns(true);
            tl.setStretchAllColumns(true);
            tv = new TextView(this);
        }

        Button back = new Button(this);
        back.setBackgroundColor(Color.parseColor("#0367FF"));
        back.setText("Back");
        back.setTextColor(Color.WHITE);
        back.setOnClickListener(v -> {
            finish();
        });

        Button instructions = new Button(this);
        instructions.setBackgroundColor(Color.parseColor("#0367FF"));
        instructions.setText("Instructions");
        instructions.setTextColor(Color.WHITE);
        instructions.setOnClickListener(v -> {
            Intent intent = new Intent(this, SolverInstructions.class);
            startActivity(intent);
        });

        Button solveButton = new Button(this);
        solveButton.setText("solve");
        solveButton.setBackgroundColor(Color.parseColor("#0367FF"));
        solveButton.setTextColor(Color.WHITE);
        solveButton.setOnClickListener(v -> {
            solve();
            TableLayout tl = new TableLayout(this);
            tl.removeAllViews();
            for(int i = 0; i < 9; i++) {
                TableRow tr = new TableRow(this);
                tr.removeAllViews();
                for(int j = 0; j < 9; j++) {
                    table[i][j] = new Cell(intTable[i][j], this, i, j);
                    tr.addView(table[i][j].text);
                }
                tl.addView(tr);
                tl.setShrinkAllColumns(true);
                tl.setStretchAllColumns(true);
            }
            linlay.removeAllViews();
            linlay.addView(tl);
            linlay.addView(tv);
            linlay.addView(solveButton);
            linlay.addView(new TextView(this));
            linlay.addView(back);
            linlay.setGravity(Gravity.CENTER);
            linlay.addView(new TextView(this));
            linlay.addView(instructions);

        });
        linlay = new LinearLayout(this);
        linlay.addView(tl);
        linlay.addView(tv);
        linlay.addView(solveButton);
        linlay.addView(new TextView(this));
        linlay.addView(back);
        linlay.addView(new TextView(this));
        linlay.addView(instructions);
        linlay.setGravity(Gravity.CENTER);

        linlay.setBackground(getDrawable(R.drawable.plainbackground));
        linlay.setOrientation(LinearLayout.VERTICAL);
        setContentView(linlay);
    }
}
