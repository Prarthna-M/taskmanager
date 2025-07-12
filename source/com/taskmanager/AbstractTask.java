package com.taskmanager;

import java.time.LocalDate;

public abstract class AbstractTask implements Taskable {
    protected String name;
    protected String description;
    protected LocalDate startDate;
    protected LocalDate dueDate;
    protected boolean isCompleted;
    protected String recurrence;
    protected String category;

    public AbstractTask(String name, String description, LocalDate startDate, LocalDate dueDate, String recurrence, String category) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.recurrence = recurrence;
        this.category = category;
        this.isCompleted = false;
    }

    @Override
    public boolean isCompleted() {
        return isCompleted;
    }

    @Override
    public String getTaskFileName() {
        return name + "_task.csv";
    }

    @Override
    public void markAsCompleted() {
        this.isCompleted = true;
    }
}