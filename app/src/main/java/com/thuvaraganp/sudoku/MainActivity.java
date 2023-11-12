package com.thuvaraganp.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements SudokuTimer.SudokuTimerListener {
    TextView tv;

    private int[][] intTable;
    private Random random;

    private SudokuTimer timer;
    private TextView timerTextView;
    private long elapsedTime;

    Context c;

    private class Cell {
        int value; boolean fixed; Button bt;
        public Cell(int initvalue, Context THIS) {
            // set the value
            value = initvalue;

            if (value != 0) fixed = true;
            else fixed = false;

            // create a new button
            bt = new Button(THIS);
            if (fixed) bt.setText(String.valueOf(value));
            else bt.setTextColor(Color.BLUE);

            // if button is clicked, increment the value
            bt.setOnClickListener(v -> {
                if(fixed) return;
                value++;
                if(value > 9) value = 1;
                bt.setText(String.valueOf(value));
                // if the sudoku puzzle is completed, save the best time
                if(completed()) {
                    BestTimeManager.saveBestTime(Integer.toString(difficulty), elapsedTime, c);
                    tv.setText("Solved!");
                } else {
                    if(correct()) {
                        tv.setText("");
                    } else {
                        tv.setText("There is a repeated digit!");
                    }
                }
            });
        }
    }
    Cell[][] table;
    boolean completed() {
        // check if the board is complete
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                if(table[i][j].value == 0) return false;
            }
        }
        if(correct())
            return true;
        else
            return false;
    }
    boolean correct(int i1, int j1, int i2, int j2) {
        // check if a move on the board is correct
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
        // check if correct
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

    int difficulty;

    public void generate(int difficulty) {
        // generate the board
        fillDiagonal();
        fillRemaining(0, 3);
        removeCells(difficulty);
    }

    private void fillDiagonal() {
        // fill the diagonals
        for (int i = 0; i < 9; i += 3) {
            fillBox(i, i);
        }
    }

    private void fillBox(int row, int col) {
        // fill a box with a number that is not found
        int num;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                do {
                    num = random.nextInt(9) + 1;
                } while (!isValid(row, col, num));

                intTable[row + i][col + j] = num;
            }
        }
    }

    private boolean isValid(int row, int col, int num) {
        // check if a position is valid
        for (int i = 0; i < 9; i++) {
            if (intTable[row][i] == num || intTable[i][col] == num) {
                return false;
            }
        }

        int boxRow = row - row % 3;
        int boxCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (intTable[boxRow + i][boxCol + j] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean fillRemaining(int row, int col) {
        // using backtracking, fill remaining boxes
        if (col >= 9 && row < 8) {
            row++;
            col = 0;
        }

        if (row >= 9 && col >= 9) {
            return true;
        }

        if (row < 3) {
            if (col < 3) {
                col = 3;
            }
        } else if (row < 6) {
            if (col == (row / 3) * 3) {
                col += 3;
            }
        } else {
            if (col == 6) {
                row++;
                col = 0;
                if (row >= 9) {
                    return true;
                }
            }
        }

        for (int num = 1; num <= 9; num++) {
            if (isValid(row, col, num)) {
                // fill int table
                intTable[row][col] = num;
                if (fillRemaining(row, col + 1)) {
                    return true;
                }
                intTable[row][col] = 0;
            }
        }

        return false;
    }

    private void removeCells(int difficulty) {
        // remove cells for the puzzle depending on the difficulty level
        int cellsToRemove = 0;
        switch (difficulty) {
            case 1:
                cellsToRemove = 40;
                break;
            case 2:
                cellsToRemove = 50;
                break;
            case 3:
                cellsToRemove = 60;
                break;
            default:
                cellsToRemove = 40;
        }

        while (cellsToRemove > 0) {
            int row = random.nextInt(9);
            int col = random.nextInt(9);
            if (intTable[row][col] != 0) {
                intTable[row][col] = 0;
                cellsToRemove--;
            }
        }
    }

    private void saveBoard(int[][] board) {
        // save the board into a text file
        try {
            File file = new File(getFilesDir(), "board.txt");
            FileWriter writer = new FileWriter(file);

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    writer.write(board[i][j] + " ");
                }
                writer.write("\n");
            }

            writer.close();

            File timeFile = new File(getFilesDir(), "time.txt");
            FileWriter timeWriter = new FileWriter(timeFile);
            timeWriter.write((int) elapsedTime);
            timeWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private int[][] readBoard() {
        // read in the board
        int[][] board = new int[9][9];

        try {
            File file = new File(getFilesDir(), "board.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));

            String line;
            int row = 0;
            while ((line = reader.readLine()) != null && row < 9) {
                String[] values = line.trim().split(" ");
                for (int col = 0; col < 9; col++) {
                    board[row][col] = Integer.parseInt(values[col]);
                }
                row++;
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return board;
    }

    void load() {
        intTable = readBoard();
    }

    int loadBoard;

    private void startTimer() {
        timer.start();
    }

    private void stopTimer() {
        timer.stop();
    }

    @Override
    public void onTimeChanged(long elapsedTime) {
        this.elapsedTime = elapsedTime;
        updateTimerText();
    }

    private void updateTimerText() {
        long seconds = elapsedTime / 1000;
        long minutes = seconds / 60;
        seconds %= 60;
        timerTextView.setText(String.format("%02d:%02d", minutes, seconds));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!timer.isRunning()) {
            startTimer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        // set up the variables
        Bundle b = getIntent().getExtras();
        difficulty = b.getInt("difficulty");
        loadBoard = b.getInt("loadBoard");

        intTable = new int[9][9];
        random = new Random();

        timer = new SudokuTimer(this);

        table = new Cell[9][9];
        tl = new TableLayout(this);

        timerTextView = new TextView(this);

        c = this;

        if(loadBoard == 1) {
            load();
        } else {
            generate(difficulty);
        }

        // create a save button
        Button save = new Button(this);
        save.setText("Save");

        save.setOnClickListener(v -> {
            for(int i = 0; i < 9; i++) {
                for(int j = 0; j < 9; j++) {
                    intTable[i][j] = table[i][j].value;
                }
            }
            saveBoard(intTable);

        });

        // create the board
        for(int i = 0; i < 9; i++) {
            TableRow tr = new TableRow(this);
            for(int j = 0; j < 9; j++) {
                table[i][j] = new Cell(intTable[i][j], this);
                tr.addView(table[i][j].bt);
            }
            tl.addView(tr);
        }
        tl.setShrinkAllColumns(true);
        tl.setStretchAllColumns(true);
        tv = new TextView(this);
        tv.setPadding(16, 16, 16, 16);
        tv.setTextSize(20);
        tv.setTextColor(Color.WHITE);
        linlay = new LinearLayout(this);
        linlay.addView(tl);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.gravity = Gravity.CENTER;
        linlay.addView(tv);
        tv.setLayoutParams(layoutParams);


        save.setLayoutParams(layoutParams);
        save.setTextColor(Color.WHITE);

        save.setBackgroundColor(Color.parseColor("#0367FF"));
        save.setPadding(16, 16, 16, 16);
        linlay.addView(save);
        timerTextView.setPadding(16, 16, 16, 16 );
        linlay.addView(timerTextView);
        timerTextView.setLayoutParams(layoutParams);
        timerTextView.setTextSize(30);
        timerTextView.setTextColor(Color.WHITE);

        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        buttonLayoutParams.gravity = Gravity.START | Gravity.BOTTOM;

        Button back = new Button(this);
        back.setText("Back");
        back.setLayoutParams(buttonLayoutParams);
        back.setOnClickListener(v -> {
            finish();
        });
        linlay.addView(back);
        back.setTextColor(Color.WHITE);
        back.setBackgroundColor(Color.parseColor("#0367FF"));

        LinearLayout.LayoutParams instructionLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        instructionLayoutParams.gravity = Gravity.START | Gravity.BOTTOM;

        Button instructions = new Button(this);
        instructions.setText("Instructions");
        instructions.setLayoutParams(instructionLayoutParams);
        instructions.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameInstructions.class);
            startActivity(intent);
        });
        instructions.setTextColor(Color.WHITE);
        instructions.setPadding(16, 0, 16, 0);
        instructions.setBackgroundColor(Color.parseColor("#0367FF"));
        linlay.addView(new TextView(this));

        linlay.addView(instructions);

        linlay.setBackground(getDrawable(R.drawable.plainbackground));
        linlay.setOrientation(LinearLayout.VERTICAL);
        int padding = 50;
        linlay.setPadding(padding, padding, padding, padding);

        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                intTable[i][j] = table[i][j].value;
            }
        }

        setContentView(linlay);
    }
}