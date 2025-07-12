package com.taskmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

public class User extends AbstractUser {
    private ObservableList<Task> tasks;
    private ObservableList<Habit> habits;
    private ObservableList<Habit> completedHabits;

    public User(String username, String password) {
        super(username, password);
        this.tasks = FXCollections.observableArrayList();
        this.habits = FXCollections.observableArrayList();
        this.completedHabits = FXCollections.observableArrayList();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    public static boolean authenticate(String username, String password) throws IOException, NoSuchAlgorithmException {
        File file = new File("user_credentials.txt");
        if (!file.exists()) {
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[1].equals(hashPassword(password))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void registerUser(String username, String password) throws IOException, NoSuchAlgorithmException {
        String hashedPassword = hashPassword(password);
        File file = new File("user_credentials.txt");

        if (!file.exists()) {
            file.createNewFile();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(username + "," + hashedPassword);
            writer.newLine();
        }
    }

    public ObservableList<Task> getTasks() {
        return tasks;
    }

    public ObservableList<Habit> getHabits() {
        return habits;
    }

    public ObservableList<Habit> getCompletedHabits() {
        return completedHabits;
    }

    public boolean checkCredentials(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    public String getTaskFileName() {
        return username + "_tasks.csv";
    }

    public String getPersonalTaskFileName() {
        return username + "_personal_tasks.csv";
    }

    public String getProfessionalTaskFileName() {
        return username + "_professional_tasks.csv";
    }

    public String getHabitFileName() {
        return username + "_habits.csv";
    }

    public String getCompletedHabitFileName() {
        return username + "_completed_habits.csv";
    }
}