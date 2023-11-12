package com.thuvaraganp.sudoku;

import static java.security.AccessController.getContext;

import android.content.Context;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class BestTimeManager {
    private static final String FILE_PATH = "best_times.txt";
    private static final Map<String, Long> bestTimes = new HashMap<>();

    public static void saveBestTime(String difficulty, long bestTime, Context c) {
        bestTimes.put(difficulty, bestTime);
        saveToFile(c);
    }

    public static long loadBestTime(String difficulty) {
        if (bestTimes.containsKey(difficulty)) {
            return bestTimes.get(difficulty);
        }
        return 0;
    }

    private static void saveToFile(Context c) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(c.getFilesDir(), FILE_PATH)))) {
            for (Map.Entry<String, Long> entry : bestTimes.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void loadFromFile(Context c) {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(c.getFilesDir(), FILE_PATH)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                System.out.println(parts);
                if (parts.length == 2) {
                    String difficulty = parts[0];
                    long bestTime = Long.parseLong(parts[1]);
                    bestTimes.put(difficulty, bestTime);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}