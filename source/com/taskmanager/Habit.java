package com.taskmanager;

import java.time.LocalDate;

public class Habit extends AbstractHabit {
    private int missedStreak;

    public Habit(String name, String description, LocalDate startDate, String recurrence) {
        super(name, description, startDate, recurrence);
        this.missedStreak = 0;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getRecurrence() {
        return recurrence;
    }

    public String getName() {
        return name;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    public void incrementMissedStreak() {
        this.missedStreak++;
    }

    public int getMissedStreak() {
        return missedStreak;
    }

    public void setMissedStreak(int missedStreak) {
        this.missedStreak = missedStreak;
    }

    @Override
    public String toString() {
        return name + " - Streak: " + streak + " - Recurrence: " + recurrence + " - Start: " + startDate;
    }
}