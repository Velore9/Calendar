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

    private LocalDate currentDate; // to open up in the current month
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

        navigationPane.getChildren().addAll(prevButton, nextButton); // put the info the buttons with the styling of the Hbox

        // Create month label
        monthLabel = new Label();
        monthLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");
        /*updateCalendar();
        populateCalendar(root);*/

        GridPane calendarPane = new GridPane();
        calendarPane.setAlignment(Pos.CENTER);
        calendarPane.setHgap(10);
        calendarPane.setVgap(10);
        /*updateCalendar();
        populateCalendar(root);*/

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

    private void updateCalendar() { // whenever u go to next month and then sets the month
        monthLabel.setText(currentDate.getMonth().toString() + " " + currentDate.getYear());
    }

    private void populateCalendar(BorderPane root) {
        GridPane calendarPane = new GridPane();
        calendarPane.setAlignment(Pos.CENTER);
        /*calendarPane.setHgap(10); maybe i dont need gaps i think it looks cooler
        calendarPane.setVgap(10);*/

        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < daysOfWeek.length; i++) { // for adding days of week on top
            Label dayLabel = new Label(daysOfWeek[i]);
            dayLabel.setStyle("-fx-font-weight: bold;");
            calendarPane.add(dayLabel, i, 0);
        }

        LocalDate firstDayOfMonth = currentDate.withDayOfMonth(1);
        int startColumn = firstDayOfMonth.getDayOfWeek().getValue() % 7; // find out what day of the week the month starts in, by finding remainder then sets the column to align at the designated day of the week

        int dayOfMonth = 1;
        int row = 1;
        for (int col = startColumn; col < 7; col++) { // for very first week due to it not being a full column (most of the time)
            VBox dateBox = createDateBox(String.valueOf(dayOfMonth));
            calendarPane.add(dateBox, col, row);
            dayOfMonth++;
        }

        while (dayOfMonth <= currentDate.lengthOfMonth()) { // uses the method createDateBox to put the number associated with that day into the box respectively. For each month
            row++; // starts from 2nd week (most of the time)
            for (int col = 0; col < 7 && dayOfMonth <= currentDate.lengthOfMonth(); col++) { // for each week. stops when its less than length of the days of the week or months end apruptly
                VBox dateBox = createDateBox(String.valueOf(dayOfMonth));
                calendarPane.add(dateBox, col, row); // finds location of where box needs to be (vertically and horizontally and add it to GridPane) GridPane being almost like a full on 2d array
                dayOfMonth++;
            }
        }

        root.setCenter(calendarPane);
        BorderPane.setAlignment(calendarPane, Pos.CENTER);
    }

    private VBox createDateBox(String date) { // for each box and adds some styling
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        Label label = new Label(date); // to be able to set the text (or this case label)
        vbox.getChildren().add(label);
        vbox.setPrefSize(300, 300); // i set this so that its appropriate for my mac. might change it to adjust in ratio with resolution
        vbox.setStyle("-fx-border-color: black; -fx-border-width: 0.5px;"); // Add border for styling
        return vbox;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
