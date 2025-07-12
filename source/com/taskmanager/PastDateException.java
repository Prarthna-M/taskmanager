package com.taskmanager;

public class PastDateException extends Exception {
    public PastDateException(String message) {
        super(message);
    }
}