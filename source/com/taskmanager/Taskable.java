package com.taskmanager;

public interface Taskable {
    void markAsCompleted();
    boolean isCompleted();
    String getTaskFileName();
}