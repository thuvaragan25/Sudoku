package com.thuvaraganp.sudoku;

import android.os.Handler;

public class SudokuTimer {
    private long startTime;
    private Handler handler;
    private Runnable runnable;
    private SudokuTimerListener listener;
    private boolean isRunning;

    public SudokuTimer(SudokuTimerListener listener) {
        this.listener = listener;
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - startTime;
                listener.onTimeChanged(elapsedTime);
                handler.postDelayed(this, 1000);
            }
        };
    }

    public void start() {
        startTime = System.currentTimeMillis();
        handler.postDelayed(runnable, 1000);
        isRunning = true;
    }

    public void stop() {
        handler.removeCallbacks(runnable);
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public interface SudokuTimerListener {
        void onTimeChanged(long elapsedTime);
    }
}
