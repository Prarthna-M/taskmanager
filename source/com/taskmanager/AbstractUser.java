package com.taskmanager;

public abstract class AbstractUser {
    protected String username;
    protected String password;

    public AbstractUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public abstract String getUsername();
    public abstract String getPassword();
}