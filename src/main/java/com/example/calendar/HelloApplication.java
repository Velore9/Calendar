package com.example.calendar;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.stage.Stage;
import java.time.LocalDate;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;
import java.time.format.DateTimeFormatter;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class HelloApplication extends Application {
    private LocalDate currentDate;
    private Label monthLabel;
    @Override
    public void start(Stage primaryStage) throws IOException {
        currentDate = LocalDate.now();

        GridPane calendarPane = new GridPane();
        calendarPane.setAlignment(Pos.CENTER);

        // Labels for days of week
        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < daysOfWeek.length; i++) {
            Label dayLabel = new Label(daysOfWeek[i]);
            calendarPane.add(dayLabel, i, 0);
        }

        // Take over the calendar with dates
        populateCalendar(calendarPane, currentDate);

        // Create buttons for navigation
        Button prevButton = new Button("Prev");
        prevButton.setOnAction(event -> {
            currentDate = currentDate.minusMonths(1);
            populateCalendar(calendarPane, currentDate);
            updateMonthLabel();
        });

        Button nextButton = new Button("Next");
        nextButton.setOnAction(event -> {
            currentDate = currentDate.plusMonths(1);
            populateCalendar(calendarPane, currentDate);
            updateMonthLabel();
        });

        GridPane navigationPane = new GridPane();
        navigationPane.setAlignment(Pos.CENTER);
        navigationPane.add(prevButton, 0, 0);
        navigationPane.add(nextButton, 1, 0);

        // Create main layout
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(navigationPane, calendarPane);

        monthLabel = new Label();
        monthLabel.setAlignment(Pos.CENTER);
        updateMonthLabel();
        root.getChildren().add(monthLabel);

        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Calendar App");
        primaryStage.show();
    }
    private void populateCalendar(GridPane calendarPane, LocalDate date) {
        calendarPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) > 0);
        LocalDate firstDayOfMonth = date.withDayOfMonth(1);
        int startColumn = firstDayOfMonth.getDayOfWeek().getValue() % 7;

        int dayOfMonth = 1;
        int row = 1;
        for (int col = startColumn; col < 7; col++) {
            VBox dateBox = createDateBox(String.valueOf(dayOfMonth));
            calendarPane.add(dateBox, col, row);
            dayOfMonth++;
        }

        while (dayOfMonth <= date.lengthOfMonth()) {
            row++;
            for (int col = 0; col < 7 && dayOfMonth <= date.lengthOfMonth(); col++) {
                calendarPane.add(new Label(String.valueOf(dayOfMonth)), col, row);
                dayOfMonth++;
            }
        }
    }
    private void updateMonthLabel() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        monthLabel.setText(currentDate.format(formatter));
    }
    private VBox createDateBox(String date) {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        Label label = new Label(date);
        vbox.getChildren().add(label);
        vbox.setPrefSize(50, 50); // Set preferred size for each date box
        vbox.setStyle("-fx-border-color: black; -fx-border-width: 1px;"); // Add border for styling
        return vbox;
    }

    public static void main(String[] args) {
        launch(args);
    }
}