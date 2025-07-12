package com.taskmanager;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.util.Random;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import java.util.Set;
import java.util.HashSet;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.geometry.Orientation;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;

public class TaskHabitManager extends Application {

private static final String TASK_BG_IMAGE = "file:C:/javaprg/source/com/taskmanager/bgfinal.png";  
private static final String HABIT_BG_IMAGE = "file:C:/javaprg/source/com/taskmanager/bgfinal.png";

    private User currentUser;
    private ListView<Task> personalTaskListView;
    private ListView<Task> professionalTaskListView;
    private ListView<Habit> habitListView ;
    private ListView<Habit> completedHabitListView;
    private ObservableList<Task> personalTaskList = FXCollections.observableArrayList();
    private ObservableList<Task> professionalTaskList = FXCollections.observableArrayList();
    private ObservableList<Habit> habitList = FXCollections.observableArrayList();
    private ObservableList<Habit> completedHabitList = FXCollections.observableArrayList();

private static final String BUTTON_STYLE =
    "-fx-background-color: rgba(255, 209, 220, 0.5); " +  // Pastel pink background with 50% opacity
    "-fx-text-fill: #000000; " +         // Black text
    "-fx-background-radius: 5px; " +     // Rounded corners
    "-fx-padding: 5px 15px; " +          // Padding inside button
    "-fx-cursor: hand; " +                // Hand cursor on hover
    "-fx-border-color: white; " +        // White border color
    "-fx-border-width: 2px; " +          // Border width
    "-fx-border-radius: 5px;";            // Rounded corners for the border


    private final String[] MOTIVATIONAL_QUOTES = {
    "Small daily improvements lead to stunning results.",
    "The secret of getting ahead is getting started.",
    "Success is the sum of small efforts, repeated day in and day out.",
    "Your future is created by what you do today, not tomorrow.",
    "Strive for progress, not perfection.",
    "Consistency is the key to achieving your goals.",
    "Don't watch the clock; do what it does—keep going.",
    "Dream big, start small, but most of all, start.",
    "Motivation gets you going; discipline keeps you growing.",
    "Today's accomplishments were yesterday's impossibilities.",
    "You don't have to be perfect to make progress.",
    "Success doesn't come from what you do occasionally; it comes from what you do consistently.",
    "Start where you are, use what you have, do what you can.",
    "Every accomplishment begins with the decision to try.",
    "Stay focused and never give up on your dreams.",
    "Remember, consistency beats intensity.",
    "Small steps in the right direction are better than big steps in the wrong one.",
    "Make today count; your future self will thank you.",
    "Trust the process; each step brings you closer.",
    "You're one habit away from your best life.",
    "Be proud of every small step forward.",
    "What you do today shapes who you'll be tomorrow.",
    "Great things take time; stay committed.",
    "Growth comes from pushing beyond your comfort zone.",
    "One small task completed is a step closer to a big goal.",
    "Well done! Remember, even small actions have power.",
    "Nice work! Progress is progress, no matter how small.",
    "You're building something amazing. Keep going!"
};

private String getRandomQuote() {
    Random random = new Random();
    return MOTIVATIONAL_QUOTES[random.nextInt(MOTIVATIONAL_QUOTES.length)];
}

    public static void main(String[] args) {
        launch(args);
    }

    @Override
public void start(Stage primaryStage) {
    primaryStage.setTitle("Login");

    // Show login page first
    showLoginPage(primaryStage);
}

    // Method to show the login page
    private void showLoginPage(Stage primaryStage) {
        VBox loginLayout = new VBox(10);
        loginLayout.setPadding(new Insets(20));
        loginLayout.setAlignment(Pos.CENTER);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password ");

     Button loginButton = new Button("Login");
Button registerButton = new Button("Register");
        Label errorLabel = new Label();

        loginLayout.getChildren().addAll(new Label("Login"), usernameField, passwordField, loginButton, registerButton, errorLabel);

        Scene loginScene = new Scene(loginLayout, 300, 200);
        primaryStage.setScene(loginScene);
applyStageStyles(primaryStage);
        primaryStage.show();

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            try {
                if (User .authenticate(username, password)) {
                    currentUser = new User(username, User.hashPassword(password));
                    showTaskOrHabitChoicePage(primaryStage); // Show choice page instead of directly showing task manager
                } else {
                    errorLabel.setText("Invalid username or password.");
                }
            } catch (IOException | NoSuchAlgorithmException ex) {
                ex.printStackTrace();
                errorLabel.setText("Error occurred during login.");
            }
        });

        // Handle registration action
        registerButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (username.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Username and password cannot be empty.");
                return;
            }
            try {
                User.registerUser  (username, password);
                errorLabel.setText("Registration successful. Please login.");
            } catch (IOException | NoSuchAlgorithmException ex) {
                ex.printStackTrace();
                errorLabel.setText("Error occurred during registration.");
            }
        });
    }

    // Method to show the Task or Habit selection page
    private void showTaskOrHabitChoicePage(Stage primaryStage) {
    // Load tasks and habits after successful login
    try {
        loadTasksFromCSV();
        loadHabitsFromCSV();
    } catch (IOException e) {
        e.printStackTrace();
        showAlert("Error loading tasks and habits");
    }

    VBox choiceLayout = new VBox(10);
    choiceLayout.setPadding(new Insets(20));
    choiceLayout.setAlignment(Pos.CENTER);

    Label choiceLabel = new Label("Select Option:");
    Button taskButton = new Button("Task");
    Button habitButton = new Button("Habit");

    choiceLayout.getChildren().addAll(choiceLabel, taskButton, habitButton);

    Scene choiceScene = new Scene(choiceLayout, 300, 200);
    primaryStage.setScene(choiceScene);
applyStageStyles(primaryStage);

    taskButton.setOnAction(e -> showTaskCategorySelectionPage(primaryStage));
    habitButton.setOnAction(e -> showHabitManagerPage(primaryStage));
}

    // Method to show the Task category selection page (Personal or Professional)
    private void showTaskCategorySelectionPage(Stage primaryStage) {
        VBox categoryLayout = new VBox(10);
        categoryLayout.setPadding(new Insets(20));
        categoryLayout.setAlignment(Pos.CENTER);

        Label categoryLabel = new Label("Select Task Category:");
        Button professionalButton = new Button("Professional");
        Button personalButton = new Button("Personal");

        categoryLayout.getChildren().addAll(categoryLabel, professionalButton, personalButton);

        Scene categoryScene = new Scene(categoryLayout, 300, 200);
        primaryStage.setScene(categoryScene);
applyStageStyles(primaryStage);

        professionalButton.setOnAction(e -> showTaskManagerPage(primaryStage, "Professional"));
        personalButton.setOnAction(e -> showTaskManagerPage(primaryStage, "Personal"));
    }

    // Method to show the Task Manager page
   private void showTaskManagerPage(Stage primaryStage, String category) {
    primaryStage.setTitle("Task Manager - " + currentUser .getUsername());
   
    Label quoteLabel = new Label(getRandomQuote());
    quoteLabel.setStyle("-fx-font-style: italic; -fx-font-size: 14px; -fx-text-fill: #666666;");
    quoteLabel.setWrapText(true);
    quoteLabel.setMaxWidth(780);

    // Initialize the ListView based on the category
    if (category.equals("Personal")) {
        personalTaskListView = new ListView<>(personalTaskList);
    } else {
        professionalTaskListView = new ListView<>(professionalTaskList);
    }

    try {
        loadTasksFromCSV();
    } catch (IOException e) {
        e.printStackTrace();
        showAlert("Error loading tasks");
    }

  Button addTaskButton = new Button("Add Task");
addTaskButton.setStyle(BUTTON_STYLE);
Button editTaskButton = new Button("Edit Task");
editTaskButton.setStyle(BUTTON_STYLE);
Button deleteTaskButton = new Button("Delete Task");
deleteTaskButton.setStyle(BUTTON_STYLE);
Button completeTaskButton = new Button("Mark as Completed");
completeTaskButton.setStyle(BUTTON_STYLE);

    // Add new collaboration buttons
    Button newCollabButton = new Button("Start Collaboration");
newCollabButton.setStyle(BUTTON_STYLE);
    Button joinCollabButton = new Button("Join Collaboration");
joinCollabButton.setStyle(BUTTON_STYLE);


    addTaskButton.setOnAction(e -> showTaskCreationWindow(primaryStage, category));
    editTaskButton.setOnAction(e -> {
        Task selectedTask;
        if (category.equals("Personal")) {
            selectedTask = personalTaskListView.getSelectionModel().getSelectedItem();
        } else {
            selectedTask = professionalTaskListView.getSelectionModel().getSelectedItem();
        }
        if (selectedTask != null) {
            editSelectedTask(primaryStage, selectedTask);
        } else {
            showAlert("Please select a task to edit.");
        }
    });

    deleteTaskButton.setOnAction(e -> {
        Task selectedTask;
        if (category.equals("Personal")) {
            selectedTask = personalTaskListView.getSelectionModel().getSelectedItem();
        } else {
            selectedTask = professionalTaskListView.getSelectionModel().getSelectedItem();
        }
        if (selectedTask != null) {
            if (category.equals("Personal")) {
                personalTaskList.remove(selectedTask);
            } else {
                professionalTaskList.remove(selectedTask);
            }
            try {
                saveTasksToCSV();
            } catch (IOException ex) {
                ex.printStackTrace();
                showAlert("Error saving changes");
            }
        } else {
            showAlert("Please select a task to delete.");
        }
    });

    completeTaskButton.setOnAction(e -> {
        Task selectedTask;
        if (category.equals("Personal")) {
            selectedTask = personalTaskListView.getSelectionModel().getSelectedItem();
        } else {
            selectedTask = professionalTaskListView.getSelectionModel().getSelectedItem();
        }
        if (selectedTask != null) {
            selectedTask.markAsCompleted();
            if (category.equals("Personal")) {
                personalTaskListView.refresh();
            } else {
                professionalTaskListView.refresh();
            }
            try {
                saveTasksToCSV();
            } catch (IOException ex) {
                ex.printStackTrace();
                showAlert("Error saving changes");
            }
        } else {
            showAlert("Please select a task to mark as completed.");
        }
    });

    // Add collaboration button actions
    newCollabButton.setOnAction(e -> startNewCollaboration(primaryStage, category));
    joinCollabButton.setOnAction(e -> joinExistingCollaboration(primaryStage, category));

    // Only show collaboration buttons for Professional tasks
    HBox buttonBox;
    if (category.equals("Professional")) {
        buttonBox = new HBox(10, addTaskButton, editTaskButton, deleteTaskButton, completeTaskButton, newCollabButton, joinCollabButton);
    } else {
        buttonBox = new HBox(10, addTaskButton, editTaskButton, deleteTaskButton, completeTaskButton);
    }

    Label todayDateLabel = new Label("Today: " + LocalDate.now());
    todayDateLabel.setStyle("-fx-font-size: 16; -fx-font-weight : bold;");

    HBox topBox = new HBox(10, todayDateLabel);
    topBox.setAlignment(Pos.TOP_RIGHT);

    ListView<Task> taskListView;
    if (category.equals("Personal")) {
        taskListView = personalTaskListView;
    } else {
        taskListView = professionalTaskListView;
    }

   VBox layout = new VBox(10, topBox, quoteLabel, buttonBox, taskListView);
layout.setPadding(new Insets(20));

// Set the background image for the main layout
layout.setStyle(
    "-fx-background-image: url('" + TASK_BG_IMAGE + "'); " +
    "-fx-background-size: cover; " +
    "-fx-background-position: center; " +
    "-fx-background-repeat: no-repeat;"
);

// Optionally, you can still add a semi-transparent overlay to the content
VBox contentPane = new VBox(10);
contentPane.getChildren().addAll(topBox, quoteLabel, buttonBox, taskListView);
contentPane.setPadding(new Insets(20));
contentPane.setStyle(
    "-fx-background-color: rgba(255, 255, 255, 0.85); " + // semi-transparent background
    "-fx-padding: 20;"
);

// Add the content pane to the layout
layout.getChildren().add(contentPane);

// Create the scene with the main layout
Scene taskScene = new Scene(layout, 800, 600);
primaryStage.setScene(taskScene);
primaryStage.show();
}

// Show task creation window
private void showTaskCreationWindow(Stage primaryStage, String category) {
    Stage taskCreationStage = new Stage();
    taskCreationStage.initModality(Modality.WINDOW_MODAL);
    taskCreationStage.initOwner(primaryStage);

    TextField taskNameField = new TextField();
    taskNameField.setPromptText("Task Name");

    TextArea taskDescriptionField = new TextArea();
    taskDescriptionField.setPromptText("Task Description");

    DatePicker startDatePicker = new DatePicker();
    startDatePicker.setPromptText("Start Date");

    DatePicker dueDatePicker = new DatePicker();
    dueDatePicker.setPromptText("Due Date");

    ComboBox<String> recurrenceComboBox = new ComboBox<>();
    recurrenceComboBox.getItems().addAll("None", "Daily", "Weekly", "Monthly");
    recurrenceComboBox.setPromptText("Recurrence");

    Button saveButton = new Button("Add Task");

    saveButton.setOnAction(e -> {
        String taskName = taskNameField.getText();
        String taskDescription = taskDescriptionField.getText();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate dueDate = dueDatePicker.getValue();
        String recurrence = recurrenceComboBox.getValue();

        try {
            if (taskName.isEmpty() || recurrence == null || startDate == null || dueDate == null) {
                showAlert("Please fill in all fields.");
            } else {
                validateDates(startDate, dueDate); // Validate dates
                Task task = new Task(taskName, taskDescription, startDate, dueDate, recurrence, category);
                if (category.equals("Personal")) {
                    personalTaskList.add(task);
                } else {
                    professionalTaskList.add(task);
                }
                try {
                    saveTasksToCSV();
                    if (category.equals("Personal")) {
                        personalTaskListView.refresh(); // Refresh the list view
                    } else {
                        professionalTaskListView.refresh(); // Refresh the list view
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    showAlert("Error saving task");
                }
                taskCreationStage.close();
            }
        } catch (PastDateException ex) {
            showAlert(ex.getMessage());
        }
    });

    VBox layout = new VBox(10, taskNameField, taskDescriptionField, startDatePicker, dueDatePicker, recurrenceComboBox, saveButton);
    layout.setPadding(new Insets(20));

    Scene scene = new Scene(layout, 400, 300);
    taskCreationStage.setScene(scene);
    applyDialogStyles(taskCreationStage);
    taskCreationStage.show();
}

private void validateDates(LocalDate startDate, LocalDate dueDate) throws PastDateException {
    LocalDate today = LocalDate.now();
    if (startDate.isBefore(today)) {
        throw new PastDateException("Start date cannot be in the past.");
    }
    if (dueDate.isBefore(today)) {
        throw new PastDateException("Due date cannot be in the past.");
    }
    if (startDate.isAfter(dueDate)) {
        throw new PastDateException("Start date must be before due date.");
    }
}

    // Method to show the Habit Manager page
private void showHabitManagerPage(Stage primaryStage) {
    primaryStage.setTitle("Habit Manager - " + currentUser.getUsername());
   
    Label quoteLabel = new Label(getRandomQuote());
    quoteLabel.setStyle("-fx-font-style: italic; -fx-font-size: 14px; -fx-text-fill: #666666;");
    quoteLabel.setWrapText(true);
    quoteLabel.setMaxWidth(780);

Button addHabitButton = new Button("Add Habit");
addHabitButton.setStyle(BUTTON_STYLE);
Button editHabitButton = new Button("Edit Habit");
editHabitButton.setStyle(BUTTON_STYLE);
Button deleteHabitButton = new Button("Delete Habit");
deleteHabitButton.setStyle(BUTTON_STYLE);
Button completeHabitButton = new Button("Mark as Completed");
completeHabitButton.setStyle(BUTTON_STYLE);
   Button fullyCompleteHabitButton = new Button("Completed the Habit");
fullyCompleteHabitButton.setStyle(BUTTON_STYLE);

Button missedHabitButton = new Button("Mark as Missed");
missedHabitButton.setStyle(BUTTON_STYLE);

Button showGraphButton = new Button("Show Progress Graph");
showGraphButton.setStyle(BUTTON_STYLE);

Button communityFeedbackButton = new Button("Community Feedback");
communityFeedbackButton.setStyle(BUTTON_STYLE);

    habitList = currentUser.getHabits();
    habitListView = new ListView<>(habitList);

    completedHabitList = currentUser.getCompletedHabits();
    completedHabitListView = new ListView<>(completedHabitList);

    addHabitButton.setOnAction(e -> showHabitCreationWindow(primaryStage, "", ""));
    editHabitButton.setOnAction(e -> editSelectedHabit(primaryStage));
    deleteHabitButton.setOnAction(e -> deleteSelectedHabit());
    fullyCompleteHabitButton.setOnAction(e -> markHabitAsFullyCompleted());
   
    completeHabitButton.setOnAction(e -> {
        Habit selectedHabit = habitListView.getSelectionModel().getSelectedItem();
        if (selectedHabit != null) {
            selectedHabit.markAsCompleted();
            habitListView.refresh();
            try {
                saveHabitsToCSV();
            } catch (IOException ex) {
                ex.printStackTrace();
                showAlert("Error saving habit changes");
            }
        } else {
            showAlert("Please select a habit");
        }
    });

    missedHabitButton.setOnAction(e -> {
        Habit selectedHabit = habitListView.getSelectionModel().getSelectedItem();
        if (selectedHabit != null) {
            selectedHabit.incrementMissedStreak();
            habitListView.refresh();
            try {
                saveHabitsToCSV();
            } catch (IOException ex) {
                ex.printStackTrace();
                showAlert("Error saving habit changes");
            }
        } else {
            showAlert("Please select a habit");
        }
    });

    showGraphButton.setOnAction(e -> {
        Habit selectedHabit = habitListView.getSelectionModel().getSelectedItem();
        if (selectedHabit != null) {
            showHabitProgressGraph(selectedHabit);
        } else {
            showAlert("Please select a habit");
        }
    });

    communityFeedbackButton.setOnAction(e -> showCommunityFeedbackWindow());

    HBox buttonBox = new HBox(10, addHabitButton, editHabitButton, deleteHabitButton,
                            completeHabitButton, missedHabitButton, showGraphButton,
                            fullyCompleteHabitButton, communityFeedbackButton);

    try {
        loadHabitsFromCSV();
    } catch (IOException e) {
        e.printStackTrace();
        showAlert("Error loading habits");
    }
    // Create recommended habits list
    ListView<String> recommendedHabitsListView = new ListView<>();
    ObservableList<String> recommendedHabits = FXCollections.observableArrayList(
        "Daily Stretch Routine - Start the day with 10 minutes of stretching exercises",
        "Meal Prep Sundays - Spend an hour on Sundays planning and preparing meals",
        "Weekly Yoga Session - Join a guided yoga class once a week",
        "Green Smoothie Habit - Incorporate one green smoothie into your daily routine",
        "Read One Book a Month - Commit to reading one book a month",
        "Master Touch Typing - Practice touch typing 10 minutes a day",
        "Daily Gratitude Journal - Write three specific things you're grateful for",
        "Track Every Expense - Use a budgeting app daily to record every purchase",
        "Practice Skin-Care Routine - Follow a basic skincare routine every morning and night",
        "Mindful Bedtime Ritual - Start a calming 15-minute bedtime routine"
    );
    recommendedHabitsListView.setItems(recommendedHabits);
    recommendedHabitsListView.setPrefHeight(200);

    // Add a button to add a recommended habit
    Button addRecommendedHabitButton = new Button("Add Selected Recommended Habit");
addRecommendedHabitButton.setStyle(BUTTON_STYLE);

    addRecommendedHabitButton.setOnAction(e -> {
        String selectedRecommendedHabit = recommendedHabitsListView.getSelectionModel().getSelectedItem();
        if (selectedRecommendedHabit != null) {
            String[] parts = selectedRecommendedHabit.split(" - ");
            showHabitCreationWindow(primaryStage, parts[0], parts[1]);
        } else {
            showAlert("Please select a recommended habit to add.");
        }
    });

    VBox recommendedHabitsBox = new VBox(10, new Label("Recommended Habits:"), recommendedHabitsListView, addRecommendedHabitButton);

    // Create a split pane for current habits and completed habits
    SplitPane habitsSplitPane = new SplitPane();
    habitsSplitPane.setOrientation(Orientation.VERTICAL);
    habitsSplitPane.getItems().addAll(
        new VBox(10, new Label("Current Habits"), habitListView),
        new VBox(10, new Label("Completed Habits"), completedHabitListView)
    );
    habitsSplitPane.setDividerPositions(0.7);

    // Create a split pane for habits and recommended habits
    SplitPane mainSplitPane = new SplitPane();
    mainSplitPane.getItems().addAll(habitsSplitPane, recommendedHabitsBox);
    mainSplitPane.setDividerPositions(0.7);

    VBox layout = new VBox(10, quoteLabel, buttonBox, mainSplitPane);
    layout.setPadding(new Insets(20));

layout.setStyle(
    "-fx-background-image: url('" + HABIT_BG_IMAGE + "'); " +
    "-fx-background-size: cover; " +
    "-fx-background-position: center; " +
    "-fx-background-repeat: no-repeat;"
);
    // Make the content more readable with a semi-transparent overlay
     VBox contentPane = new VBox(10);
    contentPane.getChildren().addAll(quoteLabel, buttonBox, mainSplitPane);
    contentPane.setPadding(new Insets(20));
    contentPane.setStyle(
        "-fx-background-color: rgba(255, 255, 255, 0.85); " +
        "-fx-padding: 20;"
    );

    // Add the content pane to the layout
    layout.getChildren().add(contentPane);

    Scene habitScene = new Scene(layout, 800, 600);
    primaryStage.setScene(habitScene);

    primaryStage.show();
}

private void showHabitProgressGraph(Habit habit) {
    Stage graphStage = new Stage();
    graphStage.setTitle("Habit Progress - " + habit.getName());

    CategoryAxis xAxis = new CategoryAxis();
    NumberAxis yAxis = new NumberAxis();
   
    BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
    barChart.setTitle("Habit Progress");
    xAxis.setLabel("Streak Type");
    yAxis.setLabel("Count");
barChart.setLegendVisible(false);

    XYChart.Series<String, Number> series = new XYChart.Series<>();
   
    // Add data points
    series.getData().add(new XYChart.Data<>("Completed Streaks", habit.getStreak()));
    series.getData().add(new XYChart.Data<>("Missed Streaks", habit.getMissedStreak()));

    barChart.getData().add(series);

    // Style the bars with pastel colors and reduced width
    series.getData().forEach(data -> {
        Node node = data.getNode();
        if (data.getXValue().equals("Completed Streaks")) {
            node.setStyle("-fx-bar-fill: rgba(144, 238, 144, 0.7); -fx-bar-width: 30px;"); // Pastel green
        } else {
            node.setStyle("-fx-bar-fill: rgba(255, 182, 193, 0.7); -fx-bar-width: 30px;"); // Pastel red
        }
    });

    // Add some space between bars
    barChart.setCategoryGap(50);

    // Additional styling for the chart
    barChart.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8);"); // Semi-transparent white background

    Scene scene = new Scene(barChart, 400, 300);
    graphStage.setScene(scene);
    applyDialogStyles(graphStage);
    graphStage.show();
}

    // Show habit creation window
    private void showHabitCreationWindow(Stage primaryStage, String habitName, String habitDescription) {
    Stage habitCreationStage = new Stage();
    habitCreationStage.initModality(Modality.WINDOW_MODAL);
    habitCreationStage.initOwner(primaryStage);

    TextField habitNameField = new TextField(habitName);
    habitNameField.setPromptText("Habit Name");

    TextArea habitDescriptionField = new TextArea(habitDescription);
    habitDescriptionField.setPromptText("Habit Description");

    DatePicker startDatePicker = new DatePicker();
    startDatePicker.setPromptText("Start Date");

    ComboBox<String> recurrenceComboBox = new ComboBox<>();
    recurrenceComboBox.getItems().addAll("Daily", "Weekly", "Monthly");
    recurrenceComboBox.setPromptText("Recurrence");

    Button saveButton = new Button("Add Habit");

    saveButton.setOnAction(e -> {
        String name = habitNameField.getText();
        String description = habitDescriptionField.getText();
        LocalDate startDate = startDatePicker.getValue();
        String recurrence = recurrenceComboBox.getValue();

        try {
            if (name.isEmpty() || recurrence == null || startDate == null) {
                showAlert("Please fill in all fields.");
            } else {
                validateHabitStartDate(startDate); // Validate start date
                Habit habit = new Habit(name, description, startDate, recurrence);
                habitList.add(habit);
                try {
                    saveHabitsToCSV();
                    habitListView.refresh();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    showAlert("Error saving new habit");
                }
                habitCreationStage.close();
            }
        } catch (PastDateException ex) {
            showAlert(ex.getMessage());
        }
    });

    VBox layout = new VBox(10, habitNameField, habitDescriptionField, startDatePicker, recurrenceComboBox, saveButton);
    layout.setPadding(new Insets(20));

    Scene scene = new Scene(layout, 400, 300);
    habitCreationStage.setScene(scene);
    applyDialogStyles(habitCreationStage);
    habitCreationStage.show();
}
private void validateHabitStartDate(LocalDate startDate) throws PastDateException {
    LocalDate today = LocalDate.now();
    if (startDate.isBefore(today)) {
        throw new PastDateException("Start date cannot be in the past.");
    }
}


    // Edit selected task
    private void editSelectedTask(Stage primaryStage, Task selectedTask) {
    if (selectedTask == null) {
        showAlert("Please select a task to edit.");
        return;
    }

    Stage taskEditStage = new Stage();
    taskEditStage.initModality(Modality.WINDOW_MODAL);
    taskEditStage.initOwner(primaryStage);

    TextField taskNameField = new TextField(selectedTask.getName());
    taskNameField.setPromptText("Task Name");

    TextArea taskDescriptionField = new TextArea(selectedTask.getDescription());
    taskDescriptionField.setPromptText("Task Description");

    DatePicker startDatePicker = new DatePicker(selectedTask.getStartDate());
    startDatePicker.setPromptText("Start Date");

    DatePicker dueDatePicker = new DatePicker(selectedTask.getDueDate());

    ComboBox<String> recurrenceComboBox = new ComboBox<>();
    recurrenceComboBox.getItems().addAll("None", "Daily", "Weekly", "Monthly");
    recurrenceComboBox.setValue(selectedTask.getRecurrence());

    Button saveButton = new Button("Save Changes");

    saveButton.setOnAction(e -> {
        String taskName = taskNameField.getText();
        String taskDescription = taskDescriptionField.getText();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate dueDate = dueDatePicker.getValue();
        String recurrence = recurrenceComboBox.getValue();

        if (taskName.isEmpty() || recurrence == null || startDate == null || dueDate == null) {
            showAlert("Please fill in all fields.");
        } else if (dueDate.isBefore(LocalDate.now()) || startDate.isAfter(dueDate)) {
            showAlert("Due date cannot be in the past and start date must be before due date.");
        } else {
            // Create new task with updated values
            Task updatedTask = new Task(taskName, taskDescription, startDate, dueDate, recurrence, selectedTask.getCategory());
           
            // Copy over collaboration information if any
            if (selectedTask.getCollaborationPin() != null) {
                updatedTask.setCollaborationPin(selectedTask.getCollaborationPin());
                for (String collaborator : selectedTask.getCollaborators()) {
                    updatedTask.addCollaborator(collaborator);
                }
            }

            // Remove old task and add updated task to appropriate list
            if (selectedTask.getCategory().equals("Personal")) {
                int index = personalTaskList.indexOf(selectedTask);
                if (index != -1) {
                    personalTaskList.remove(index);
                    personalTaskList.add(index, updatedTask);
                }
                personalTaskListView.refresh();
            } else {
                int index = professionalTaskList.indexOf(selectedTask);
                if (index != -1) {
                    professionalTaskList.remove(index);
                    professionalTaskList.add(index, updatedTask);
                }
                professionalTaskListView.refresh();
            }

            try {
                saveTasksToCSV(); // Save tasks to CSV after editing
                showAlert("Task updated successfully!");
            } catch (IOException ex) {
                ex.printStackTrace();
                showAlert("Error saving changes");
            }
            taskEditStage.close();
        }
    });

    VBox layout = new VBox(10, taskNameField, taskDescriptionField, startDatePicker, dueDatePicker, recurrenceComboBox, saveButton);
    layout.setPadding(new Insets(20));

    Scene scene = new Scene(layout, 400, 300);
    taskEditStage.setScene(scene);
applyDialogStyles(taskEditStage);
    taskEditStage.show();
}
    // Edit selected habit
    private void editSelectedHabit(Stage primaryStage) {
    Habit selectedHabit = habitListView.getSelectionModel().getSelectedItem();
    if (selectedHabit == null) {
        showAlert("Please select a habit to edit.");
        return;
    }

    Stage habitEditStage = new Stage();
    habitEditStage.initModality(Modality.WINDOW_MODAL);
    habitEditStage.initOwner(primaryStage);

    TextField habitNameField = new TextField(selectedHabit.getName());
    habitNameField.setPromptText("Habit Name");

    TextArea habitDescriptionField = new TextArea(selectedHabit.getDescription());
    habitDescriptionField.setPromptText("Habit Description");

    DatePicker startDatePicker = new DatePicker(selectedHabit.getStartDate());
    startDatePicker.setPromptText("Start Date");

    ComboBox<String> recurrenceComboBox = new ComboBox<>();
    recurrenceComboBox.getItems().addAll("Daily", "Weekly", "Monthly");
    recurrenceComboBox.setValue(selectedHabit.getRecurrence());

    Button saveButton = new Button("Save Changes");

    saveButton.setOnAction(e -> {
        String habitName = habitNameField.getText();
        String habitDescription = habitDescriptionField.getText();
        LocalDate startDate = startDatePicker.getValue();
        String recurrence = recurrenceComboBox.getValue();

        if (habitName.isEmpty() || recurrence == null || startDate == null) {
            showAlert("Please fill in all fields.");
        } else if (startDate.isAfter(LocalDate.now())) {
            showAlert("Start date cannot be in the future.");
        } else {
            // Create new habit with updated values
            Habit updatedHabit = new Habit(habitName, habitDescription, startDate, recurrence);
            updatedHabit.setStreak(selectedHabit.getStreak()); // Preserve the streak

            // Remove old habit and add updated habit
            int index = habitList.indexOf(selectedHabit);
            if (index != -1) {
                habitList.remove(index);
                habitList.add(index, updatedHabit);
            }

            try {
                saveHabitsToCSV();
                habitListView.refresh();
                showAlert("Habit updated successfully!");
            } catch (IOException ex) {
                ex.printStackTrace();
                showAlert("Error saving changes");
            }
            habitEditStage.close();
        }
    });

    VBox layout = new VBox(10, habitNameField, habitDescriptionField, startDatePicker, recurrenceComboBox, saveButton);
    layout.setPadding(new Insets(20));

    Scene scene = new Scene(layout, 400, 300);
    habitEditStage.setScene(scene);
 applyDialogStyles(habitEditStage);
    habitEditStage.show();
}

    // Delete selected task
    private void deleteSelectedTask(Task selectedTask) {
        if (selectedTask == null) {
            showAlert("Please select a task to delete.");
            return;
        }
        if (selectedTask.getCategory().equals("Personal")) {
            personalTaskList.remove(selectedTask);
        } else {
            professionalTaskList.remove(selectedTask);
        }
        try {
            saveTasksToCSV();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert("Error saving changes");
        }
    }

    // Delete selected habit
    private void deleteSelectedHabit() {
        Habit selectedHabit = habitListView.getSelectionModel().getSelectedItem();
        if (selectedHabit == null) {
            showAlert("Please select a habit to delete.");
            return;
        }
        habitList.remove(selectedHabit);
    }

    // Mark task as completed
    private void markTaskAsCompleted(Task selectedTask) {
        if (selectedTask == null) {
            showAlert("Please select a task to mark as completed.");
            return;
        }
        selectedTask.markAsCompleted();
        if (selectedTask.getCategory().equals("Personal")) {
            personalTaskListView.refresh(); // Add this to refresh the view
        } else {
            professionalTaskListView.refresh(); // Add this to refresh the view
        }
        try {
            saveTasksToCSV();
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert("Error saving changes");
        }
    }

    // Mark habit as completed and increase the streak
    private void markHabitAsCompleted() {
    Habit selectedHabit = habitListView.getSelectionModel().getSelectedItem();
    if (selectedHabit == null) {
        showAlert("Please select a habit to mark as completed.");
        return;
    }
    selectedHabit.markAsCompleted();
    habitListView.refresh();
    try {
        saveHabitsToCSV();
    } catch (IOException ex) {
        ex.printStackTrace();
        showAlert("Error saving habit changes");
    }
}

private void markHabitAsFullyCompleted() {
    Habit selectedHabit = habitListView.getSelectionModel().getSelectedItem();
    if (selectedHabit == null) {
        showAlert("Please select a habit to fully complete.");
        return;
    }
    habitList.remove(selectedHabit);
    completedHabitList.add(selectedHabit);
    habitListView.refresh();
    completedHabitListView.refresh();
    try {
        saveHabitsToCSV();
    } catch (IOException ex) {
        ex.printStackTrace();
        showAlert("Error saving habit changes");
    }
}

    // Save collaboration messages to a file
    private void saveCollaborationMessages(String pin, String message) {
        try {
            File file = new File("collaboration_" + pin + ".txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(message);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error saving collaboration message");
        }
    }

    // Load collaboration messages from file
    private List<String> loadCollaborationMessages(String pin) {
        List<String> messages = new ArrayList<>();
        File file = new File("collaboration_" + pin + ".txt");
        if (!file.exists()) {
            return messages;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                messages.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error loading collaboration messages");
        }
        return messages;
    }

    // Show collaboration options (new collaboration or join existing)
    private void showCollaborationPage(Stage primaryStage, Task task, String pin) {
        Stage collaborationPageStage = new Stage();
        collaborationPageStage.initModality(Modality.WINDOW_MODAL);
        collaborationPageStage.initOwner(primaryStage);
        collaborationPageStage.setTitle("Collaboration on Task: " + task.getName() + " (Pin: " + pin + ")");

        // Create ObservableList from Set to prevent duplicates
        Set<String> uniqueParticipants = new HashSet<>(task.getCollaborators());
        ObservableList<String> participantsList = FXCollections.observableArrayList(uniqueParticipants);
        ListView<String> participantsListView = new ListView<>(participantsList);

        TextArea chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        chatArea.setPromptText("Group chat...");

        // Load existing messages
        List<String> existingMessages = loadCollaborationMessages(pin);
        for (String message : existingMessages) {
            chatArea.appendText(message + "\n");
        }

        TextField messageField = new TextField();
        messageField.setPromptText("Enter your message...");
        Button sendButton = new Button("Send");

        sendButton.setOnAction(e -> {
            String message = messageField.getText();
            if (!message.isEmpty()) {
                String fullMessage = currentUser.getUsername() + ": " + message;
                chatArea.appendText(fullMessage + "\n");
                saveCollaborationMessages(pin, fullMessage);
                messageField.clear();
            }
        });

        // Add enter key handler for sending messages
        messageField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                sendButton.fire();
            }
        });

        HBox chatBox = new HBox(10, messageField, sendButton);
        VBox layout = new VBox(10,
            new Label("Participants:"),
            participantsListView,
            new Separator(),
            chatArea,
            chatBox
        );
        layout.setPadding(new Insets(20));

        // Set preferred sizes
        chatArea.setPrefHeight(300);
        participantsListView.setPrefHeight(150);
        messageField.setPrefWidth(300);

        Scene scene = new Scene(layout, 500, 600);
        collaborationPageStage.setScene(scene);
applyDialogStyles(collaborationPageStage);
        collaborationPageStage.show();

        // Focus on message field
        messageField.requestFocus();
    }

    private List<Task> findSharedTasks(String pin) {
    List<Task> sharedTasks = new ArrayList<>();
   
    // Debug print
    System.out.println("Searching for pin: " + pin);
   
    // Search in current user's tasks
    for (Task task : personalTaskList) {
        System.out.println("Checking task: " + task.getName() + " with pin: " + task.getCollaborationPin());
        if (pin.equals(task.getCollaborationPin())) {
            sharedTasks.add(task);
        }
    }
    for (Task task : professionalTaskList) {
        System.out.println("Checking task: " + task.getName() + " with pin: " + task.getCollaborationPin());
        if (pin.equals(task.getCollaborationPin())) {
            sharedTasks.add(task);
        }
    }
   
    // Search in other users' task files
    File directory = new File(".");
    File[] files = directory.listFiles((dir, name) -> name.endsWith("_tasks.csv"));
   
    if (files != null) {
        for (File file : files) {
            System.out.println("Checking file: " + file.getName());
            if (!file.getName().equals(currentUser.getTaskFileName())) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length > 7 && pin.equals(parts[7])) { // Check index 7 for collaboration pin
                            Task task = new Task(parts[0], parts[1],
                                LocalDate.parse(parts[2]), LocalDate.parse(parts[3]),
                                parts[4], parts[5]);
                            task.setCollaborationPin(pin);
                            sharedTasks.add(task);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
   
    // Debug print
    System.out.println("Found " + sharedTasks.size() + " shared tasks");
   
    return sharedTasks;
}

    // Start a new collaboration by generating a 7-digit pin and opening the collaboration page
   // In startNewCollaboration method:
private void startNewCollaboration(Stage primaryStage, String category) {
    Task selectedTask;
    if (category.equals("Personal")) {
        selectedTask = personalTaskListView.getSelectionModel().getSelectedItem();
    } else {
        selectedTask = professionalTaskListView.getSelectionModel().getSelectedItem();
    }
    if (selectedTask == null) {
        showAlert("Please select a task to collaborate on.");
        return;
    }

    String collaborationPin = generateCollaborationPin();
    selectedTask.setCollaborationPin(collaborationPin);
    selectedTask.addCollaborator(currentUser.getUsername());
   
    // Add this line to save the changes immediately
    try {
        saveTasksToCSV();
    } catch (IOException ex) {
        ex.printStackTrace();
        showAlert("Error saving collaboration information");
    }
   
    showCollaborationPage(primaryStage, selectedTask, collaborationPin);
}
    // Join an existing collaboration by entering a pin
    private void joinExistingCollaboration(Stage primaryStage, String category) {
    TextInputDialog pinDialog = new TextInputDialog();
    pinDialog.setTitle("Join Collaboration");
    pinDialog.setHeaderText("Enter the 7-digit collaboration pin:");
    pinDialog.setContentText("Pin:");

    pinDialog.showAndWait().ifPresent(pin -> {
        System.out.println("Attempting to join with pin: " + pin);
        List<Task> sharedTasks = findSharedTasks(pin);
        System.out.println("Found shared tasks: " + sharedTasks.size());
       
        if (!sharedTasks.isEmpty()) {
            Task sharedTask = sharedTasks.get(0);
            sharedTask.addCollaborator(currentUser.getUsername());
           
            if (category.equals("Personal")) {
                if (!personalTaskList.contains(sharedTask)) {
                    personalTaskList.add(sharedTask);
                }
            } else {
                if (!professionalTaskList.contains(sharedTask)) {
                    professionalTaskList.add(sharedTask);
                }
            }
           
            try {
                saveTasksToCSV();
            } catch (IOException ex) {
                ex.printStackTrace();
                showAlert("Error saving collaboration information");
            }
           
            showCollaborationPage(primaryStage, sharedTask, pin);
        } else {
            showAlert("Collaboration with this pin not found.");
        }
    });
}
    // Utility to generate a random 7-digit collaboration pin
    private String generateCollaborationPin() {
        Random random = new Random();
        int pin = 1000000 + random.nextInt(9000000);  // Generate a 7-digit number
        return String.valueOf(pin);
    }

    // Show alert
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadTasksFromCSV() throws IOException {
    if (currentUser == null) return;

    personalTaskList.clear();
    professionalTaskList.clear();
    loadTasksFromFile(currentUser.getPersonalTaskFileName(), personalTaskList);
    loadTasksFromFile(currentUser.getProfessionalTaskFileName(), professionalTaskList);
}

    private void loadTasksFromFile(String fileName, ObservableList<Task> taskList) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            return; // No tasks to load for new user
        }

        taskList.clear(); // Clear existing tasks before loading

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 7) continue;

                String name = parts[0];
                String description = parts[1];
                LocalDate startDate;
                LocalDate dueDate;

                try {
                    startDate = LocalDate.parse(parts[2]);
                } catch (Exception e) {
                    startDate = LocalDate.now(); // Default to today if parsing fails
                }

                try {
                    dueDate = LocalDate.parse(parts[3]);
                } catch (Exception e) {
                    dueDate = LocalDate.now(); // Default to today if parsing fails
                }

                String recurrence = parts[4];
                String category = parts[5];
                boolean isCompleted = Boolean.parseBoolean(parts[6]);

                Task task = new Task(name, description, startDate, dueDate, recurrence, category);
                if (isCompleted) {
                    task.markAsCompleted();
                }

                // Load collaboration information if available
                if (parts.length > 7) {
                    String collaborationPin = parts[7];
                    if (!collaborationPin.equals("null")) {
                        task.setCollaborationPin(collaborationPin);
                    }
                    if (parts.length > 8) {
                        String[] collaborators = parts[8].split(";");
                        for (String collaborator : collaborators) {
                            if (!collaborator.isEmpty()) {
                                task.addCollaborator(collaborator);
                            }
                        }
                    }
                }

                taskList.add(task);
            }
        }
    }

    private void saveTasksToCSV() throws IOException {
    if (currentUser == null) return;
   
    saveTasksToFile(currentUser.getPersonalTaskFileName(), personalTaskList);
    saveTasksToFile(currentUser.getProfessionalTaskFileName(), professionalTaskList);
}

   private void saveTasksToFile(String fileName, ObservableList<Task> taskList) throws IOException {
    File file = new File(fileName);
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
        for (Task task : taskList) {
            String collaborationPin = task.getCollaborationPin();
            if (collaborationPin == null) collaborationPin = "null";
           
            String collaborators = String.join(";", task.getCollaborators());
            if (collaborators.isEmpty()) collaborators = "none";
           
            writer.write(String.format("%s,%s,%s,%s,%s,%s,%b,%s,%s",
                task.getName(),
                task.getDescription(),
                task.getStartDate(),
                task.getDueDate(),
                task.getRecurrence(),
                task.getCategory(),
                task.isCompleted(),
                collaborationPin,
                collaborators));
            writer.newLine();
        }
    }
}

   private void loadHabitsFromCSV() throws IOException {
    if (currentUser == null) return;

    habitList.clear();
    completedHabitList.clear();
    loadHabitsFromFile(currentUser.getHabitFileName(), habitList);
    loadHabitsFromFile(currentUser.getCompletedHabitFileName(), completedHabitList);
   
    if (habitListView != null) {
        habitListView.refresh();
    }
    if (completedHabitListView != null) {
        completedHabitListView.refresh();
    }
}

private void showCommunityFeedbackWindow() {
    Stage feedbackStage = new Stage();
    feedbackStage.initModality(Modality.APPLICATION_MODAL);
    feedbackStage.setTitle("Community Feedback");

    ListView<String> feedbackListView = new ListView<>();
    loadFeedbackFromFile(feedbackListView);

    TextArea feedbackInput = new TextArea();
    feedbackInput.setPromptText("Enter your feedback here...");
    feedbackInput.setWrapText(true);
    feedbackInput.setPrefRowCount(3);

    Button submitButton = new Button("Submit Feedback");
    submitButton.setOnAction(e -> {
        String feedbackText = feedbackInput.getText().trim();
        if (!feedbackText.isEmpty()) {
            String newFeedback = currentUser.getUsername() + ": " + feedbackText;
            feedbackListView.getItems().add(newFeedback);
            saveFeedbackToFile(feedbackListView.getItems());
            feedbackInput.clear();
        }
    });

    VBox layout = new VBox(10);
    layout.setPadding(new Insets(10));
    layout.getChildren().addAll(
        new Label("Community Feedback:"),
        feedbackListView,
        new Label("Add Your Feedback:"),
        feedbackInput,
        submitButton
    );

    Scene scene = new Scene(layout, 500, 600);
scene.setFill(Color.rgb(255, 182, 193, 0.3));
    feedbackStage.setScene(scene);
applyDialogStyles(feedbackStage);
    feedbackStage.show();
}

private void loadFeedbackFromFile(ListView<String> feedbackListView) {
    File feedbackFile = new File("community_feedback.txt");
    if (feedbackFile.exists()) {
        try (BufferedReader reader = new BufferedReader(new FileReader(feedbackFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                feedbackListView.getItems().add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error loading feedback");
        }
    }
}

private void saveFeedbackToFile(ObservableList<String> feedbackList) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter("community_feedback.txt"))) {
        for (String feedback : feedbackList) {
            writer.write(feedback);
            writer.newLine();
        }
    } catch (IOException e) {
        e.printStackTrace();
        showAlert("Error saving feedback");
    }
}

private void applyDialogStyles(Stage stage) {
    Scene scene = stage.getScene();
    if (scene != null) {
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
       
        // Apply background color directly
        scene.setFill(Color.rgb(255, 209, 220, 0.15));
       
        // Apply to the root node
        scene.getRoot().setStyle("-fx-background-color: rgba(255, 209, 220, 0.15);");
    }
}

private void applyStageStyles(Stage stage) {
    Scene scene = stage.getScene();
    if (scene != null) {
        // Apply CSS stylesheet
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
       
        // Apply background color directly to the scene
        scene.setFill(Color.rgb(255, 209, 220, 0.15));
       
        // Apply to the root node of the scene
        scene.getRoot().setStyle("-fx-background-color: rgba(255, 209, 220, 0.15);");
    }
   
    // Apply style to the stage itself (affects the window decoration)
    stage.getScene().getRoot().setStyle("-fx-background-color: rgba(255, 209, 220, 0.2);");
}

private void loadHabitsFromFile(String fileName, ObservableList<Habit> habitList) throws IOException {
    File file = new File(fileName);
    System.out.println("Attempting to load habits from: " + file.getAbsolutePath());
   
    if (!file.exists()) {
        System.out.println("Habit file does not exist: " + file.getAbsolutePath());
        return;
    }

    // Print file size and check if it's empty
    System.out.println("File size: " + file.length() + " bytes");
   
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        String line;
        int lineNumber = 0;
       
        while ((line = reader.readLine()) != null) {
            lineNumber++;
            System.out.println("Reading line " + lineNumber + ": " + line);
           
            if (line.trim().isEmpty()) {
                System.out.println("Skipping empty line " + lineNumber);
                continue;
            }

            try {
                String[] parts = line.split(",");
                System.out.println("Number of parts: " + parts.length);
               
                // Check for minimum required parts (including missed streak)
                if (parts.length < 6) {
                    System.out.println("Invalid line format at line " + lineNumber + ": insufficient parts");
                    continue;
                }

                String name = parts[0].trim();
                String description = parts[1].trim();
                LocalDate startDate = LocalDate.parse(parts[2].trim());
                String recurrence = parts[3].trim();
                int streak = Integer.parseInt(parts[4].trim());
                int missedStreak = Integer.parseInt(parts[5].trim());

                Habit habit = new Habit(name, description, startDate, recurrence);
                habit.setStreak(streak);
                habit.setMissedStreak(missedStreak);
                habitList.add(habit);
               
                System.out.println("Successfully added habit: " + habit.getName());
            } catch (Exception e) {
                System.out.println("Error processing line " + lineNumber + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    System.out.println("Loaded " + habitList.size() + " habits from " + fileName);
}

private void saveHabitsToCSV() throws IOException {
    if (currentUser == null) return;
   
    saveHabitsToFile(currentUser.getHabitFileName(), habitList);
    saveHabitsToFile(currentUser.getCompletedHabitFileName(), completedHabitList);
}

private void saveHabitsToFile(String fileName, ObservableList<Habit> habitList) throws IOException {
    File file = new File(fileName);
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
        for (Habit habit : habitList) {
            String habitString = String.format("%s,%s,%s,%s,%d,%d",
                habit.getName(),
                habit.getDescription(),
                habit.getStartDate(),
                habit.getRecurrence(),
                habit.getStreak(),
                habit.getMissedStreak());
            writer.write(habitString);
            writer.newLine();
        }
    }
}

   @Override
public void stop() {
    try {
        if (currentUser != null) {
            saveTasksToCSV();
            saveHabitsToCSV();
        }
    } catch (IOException ex) {
        ex.printStackTrace();
    }
}
}