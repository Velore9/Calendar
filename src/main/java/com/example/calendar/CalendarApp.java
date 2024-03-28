package com.example.calendar;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CalendarApp extends Application {

    private LocalDate currentDate;
    private Label monthLabel;


    @Override
    public void start(Stage primaryStage) {
        currentDate = LocalDate.now();

        BorderPane root = new BorderPane();
        root.setPrefSize(600, 400);

        // Create navigation pane with buttons
        HBox navigationPane = new HBox(10);
        navigationPane.setAlignment(Pos.CENTER);

        Button prevButton = new Button("Prev"); // last month
        prevButton.setOnAction(event -> {
            currentDate = currentDate.minusMonths(1);
            updateCalendar();
            populateCalendar(root);
        });

        Button nextButton = new Button("Next"); // next month
        nextButton.setOnAction(event -> {
            currentDate = currentDate.plusMonths(1);
            updateCalendar();
            populateCalendar(root);
        });

        navigationPane.getChildren().addAll(prevButton, nextButton);

        // Create month label
        monthLabel = new Label();
        monthLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        updateCalendar();
        populateCalendar(root);

        // Create calendar grid panes for each date
        GridPane calendarPane = new GridPane();
        calendarPane.setAlignment(Pos.CENTER);
        calendarPane.setHgap(10);
        calendarPane.setVgap(10);
        updateCalendar();
        populateCalendar(root);

        // Add components to root pane. For GUI purposes
        root.setTop(monthLabel);
        BorderPane.setAlignment(monthLabel, Pos.CENTER);
        root.setCenter(calendarPane);
        root.setBottom(navigationPane);
        BorderPane.setAlignment(navigationPane, Pos.CENTER);

        //creates the scene, set title etc
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Calendar App");
        primaryStage.show();

        //intialises calendar or else first GUI will be empty
        populateCalendar(root);
    }

    private void updateCalendar() {
        monthLabel.setText(currentDate.getMonth().toString() + " " + currentDate.getYear());
    }

    private void populateCalendar(BorderPane root) {
        GridPane calendarPane = new GridPane();
        calendarPane.setAlignment(Pos.CENTER);
        calendarPane.setHgap(10);
        calendarPane.setVgap(10);

        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < daysOfWeek.length; i++) {
            Label dayLabel = new Label(daysOfWeek[i]);
            dayLabel.setStyle("-fx-font-weight: bold;");
            calendarPane.add(dayLabel, i, 0);
        }

        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        int startColumn = firstDayOfMonth.getDayOfWeek().getValue() % 7;

        int dayOfMonth = 1;
        int row = 1;
        for (int col = startColumn; col < 7; col++) {
            VBox dateBox = createDateBox(String.valueOf(dayOfMonth));
            calendarPane.add(dateBox, col, row);
            dayOfMonth++;
        }

        while (dayOfMonth <= currentDate.lengthOfMonth()) {
            row++;
            for (int col = 0; col < 7 && dayOfMonth <= currentDate.lengthOfMonth(); col++) {
                VBox dateBox = createDateBox(String.valueOf(dayOfMonth));
                calendarPane.add(dateBox, col, row);
                dayOfMonth++;
            }
        }

        root.setCenter(calendarPane);
        BorderPane.setAlignment(calendarPane, Pos.CENTER);
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
