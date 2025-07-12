package com.taskmanager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Task extends AbstractTask {
    private List<String> collaborators;
    private String collaborationPin;

    public Task(String name, String description, LocalDate startDate, LocalDate dueDate, String recurrence, String category) {
        super(name, description, startDate, dueDate, recurrence, category);
        this.collaborators = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRecurrence(String recurrence) {
        this.recurrence = recurrence;
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

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getRecurrence() {
        return recurrence;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getCollaborators() {
        return collaborators;
    }

    public String getCollaborationPin() {
        return collaborationPin;
    }

    public void addCollaborator(String collaborator) {
        if (!collaborators.contains(collaborator)) {
            collaborators.add(collaborator);
        }
    }

    public void setCollaborationPin(String pin) {
        this.collaborationPin = pin;
    }

    @Override
    public void markAsCompleted() {
        if (startDate.isBefore(dueDate) || startDate.isEqual(dueDate)) {
            if (recurrence.equals("Daily")) {
                startDate = startDate.plusDays(1);
            } else if (recurrence.equals("Weekly")) {
                startDate = startDate.plusWeeks(1);
            } else if (recurrence.equals("Monthly")) {
                startDate = startDate.plusMonths(1);
            }

            if (startDate.isAfter(dueDate)) {
                startDate = dueDate;
            }

            isCompleted = true;
        }
    }

    public void updateRecurrence() {
        if (recurrence.equals("Daily")) {
            dueDate = dueDate.plusDays(1);
        } else if (recurrence.equals("Weekly")) {
            dueDate = dueDate.plusWeeks(1);
        } else if (recurrence.equals("Monthly")) {
            dueDate = dueDate.plusMonths(1);
        }
    }

    public int calculateStreak() {
        int streak = 0;
        LocalDate currentDate = LocalDate.now();
        LocalDate date = startDate;

        while (!date.isAfter(currentDate)) {
            if (isCompleted) {
                streak++;
            }
            if (recurrence.equals("Daily")) {
                date = date.plusDays(1);
            } else if (recurrence.equals("Weekly")) {
                date = date.plusWeeks(1);
            } else if (recurrence.equals("Monthly")) {
                date = date.plusMonths(1);
            } else {
                break;
            }
        }
        return streak;
    }

    @Override
    public String toString() {
        return name + " - " + category + " - Start: " + startDate + " - Due: " + dueDate + " - " + (isCompleted ? "Completed" : "Pending");
    }
}