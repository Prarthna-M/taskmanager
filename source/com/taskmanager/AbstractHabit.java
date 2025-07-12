package com.taskmanager;

import java.time.LocalDate;

public abstract class AbstractHabit implements Habitable {
    protected String name;
    protected String description;
    protected LocalDate startDate;
    protected String recurrence;
    protected int streak;

    public AbstractHabit(String name, String description, LocalDate startDate, String recurrence) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.recurrence = recurrence;
        this.streak = 0;
    }

    @Override
    public void markAsCompleted() {
        streak++;
    }

    @Override
    public int getStreak() {
        return streak;
    }
}