# taskmanager
## Overview

The Task Manager and Habit Tracker Application is a JavaFX desktop app with enhanced CSS styling designed to help users manage tasks and habits efficiently. It features user authentication, task and habit management, collaboration, and community feedback.

## Key Features

- **User  Authentication**: Secure registration and login with password hashing using SHA-256.
- **Task Management**: Create, edit, delete, and track personal and professional tasks.
- **Habit Tracking**: Manage habits and visualize progress with graphs.
- **Collaboration**: Share tasks with others using a unique collaboration PIN.
- **Community Feedback**: Provide and view feedback and suggestions.

## OOP Concepts Used

- **Encapsulation**: Data is kept private and accessed via public methods.
- **Inheritance**: Abstract classes define common behaviors for concrete classes.
- **Polymorphism**: Interfaces allow different implementations for tasks and habits.
- **Composition**: Users contain lists of tasks and habits.

## Security

Passwords are hashed for security using SHA-256 to protect user credentials.

## Exception Handling

The application handles exceptions gracefully, especially during file operations, to prevent crashes and provide user feedback.

## General Flow

1. Launch the application and log in or register.
2. Choose to manage tasks or habits; if managing tasks, choose between professional or personal.
3. Create, edit, delete, and track tasks or habits.
4. Use collaboration features for shared tasks. Either join an existing collaboration window or create a new one.
5. Provide and view community feedback in habits.
6. Data is saved to CSV files for persistence.

## Installation

1. **Download and Unzip**:
   - Download the provided ZIP file containing the application.
   - Unzip the file to a directory of your choice (e.g., `C:\javaprg\source\com`).

2. **JavaFX Setup**:
   - Ensure you have JDK 8 or higher installed.
   - Download the JavaFX SDK from the official [JavaFX website](https://openjfx.io/).
   - Extract the JavaFX SDK to a directory (e.g., `C:\Program Files\Java\javafx-sdk-22.0.2`).

3. **Compile the Application**:
   - Open a command prompt or terminal.
   - Navigate to the directory where the Java files are located (e.g., `C:\javaprg\source\com\taskmanager`).
   - Run the following command to compile the Java files:
     ```bash
     javac --module-path "C:\Program Files\Java\javafx-sdk-22.0.2\lib" --add-modules javafx.controls,javafx.fxml *.java
     ```

4. **Run the Application**:
   - After successful compilation, run the application using the following command:
     ```bash
     java --module-path "C:\Program Files\Java\javafx-sdk-22.0.2\lib" --add-modules javafx.controls,javafx.fxml com.taskmanager.TaskHabitManager
     ```
     
**Note**: You can change the path to the JavaFX SDK and application directory if you have it stored in a different location.
