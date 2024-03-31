package com.example.calendar;
import javafx.application.Application;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CalendarApp extends Application {

    private LocalDate currentDate; // to open up in the current month. I did nto use a calendar control only to get the values of the specified field and where it fits (e.g 4th falls on what day of week)
    private Label monthLabel;
    private Map<LocalDate, List<Assignment>> assignmentsMap;
    @Override
    public void start(Stage primaryStage) {
        currentDate = LocalDate.now();
        assignmentsMap = new HashMap<>();

        BorderPane root = new BorderPane();
        root.setPrefSize(600, 400);

        // Create navigation pane with buttons
        HBox navigationPane = new HBox(10);
        navigationPane.setAlignment(Pos.CENTER);



        Button prevButton = new Button("Prev"); // last month
        prevButton.setOnAction(event -> {
            currentDate = currentDate.minusMonths(1);
            updateCalendar(root);
        });

        Button nextButton = new Button("Next"); // next month
        nextButton.setOnAction(event -> {
            currentDate = currentDate.plusMonths(1);
            updateCalendar(root);
        });
        navigationPane.getChildren().addAll(prevButton, nextButton); // put the info the buttons with the styling of the Hbox


        // Create month label
        monthLabel = new Label();
        monthLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold;");

        // calendarPane
        GridPane calendarPane = new GridPane();
        calendarPane.setAlignment(Pos.CENTER);
        calendarPane.setHgap(10);
        calendarPane.setVgap(10);

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
        updateCalendar(root);
    }



    private void updateCalendar(BorderPane root) { // whenever u go to next month and then sets the month
        monthLabel.setText(currentDate.getMonth().toString() + " " + currentDate.getYear());
        populateCalendar(root);
    }


    private void populateCalendar(BorderPane root) {
        GridPane calendarPane = new GridPane();
        calendarPane.setAlignment(Pos.CENTER);



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
        Label dateLabel = new Label(date); // to be able to set the text (or this case label)
        dateLabel.setStyle("-fx-border-width: 0px; -fx-padding: 0px;");
        dateLabel.setAlignment(Pos.TOP_LEFT);

        VBox nestedVBox = new VBox();
        nestedVBox.setAlignment(Pos.CENTER);
        nestedVBox.setPrefSize(200, 100); // Set preferred size for nested VBox
        nestedVBox.setStyle("-fx-border-width: 0.5px;");

        vbox.getChildren().addAll(dateLabel, nestedVBox);
        vbox.setPrefSize(300, 300); // i set this so that its appropriate for my mac. might change it to adjust in ratio with resolution
        vbox.setStyle("-fx-border-color: black; -fx-border-width: 0.5px;"); // Add border for styling

        LocalDate selectedDate = currentDate.withDayOfMonth(Integer.parseInt(date));
        List<Assignment> assignments = assignmentsMap.getOrDefault(selectedDate, new ArrayList<>());

        for (Assignment assignment : assignments) {
            Label assignmentLabel = new Label(assignment.getText());
            assignmentLabel.setStyle("-fx-padding: 5px; -fx-background-color: " + assignment.getColor()); // saving the color for next time it comes back
            nestedVBox.getChildren().add(assignmentLabel);
        }

        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) { // do the method when the box is clicked
                VBox clickedBox = (VBox) e.getSource();
                displayInputWindow(clickedBox, nestedVBox, selectedDate);
            }
        };
        vbox.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
        return vbox;
    }


    private void displayInputWindow(VBox clickedBox, VBox nestedVBox, LocalDate selectedDate) {
        // Create a new stage (window)
        Stage inputStage = new Stage();
        inputStage.initModality(Modality.APPLICATION_MODAL);
        inputStage.setTitle("Input Window");

        // Create input fields
        TextField textField = new TextField();
        Button submitButton = new Button("Submit");

        final ComboBox<String> priorityColor = new ComboBox();
        priorityColor.getItems().addAll("High", "Normal", "Low");
        priorityColor.setValue("Normal");

        GridPane priority = new GridPane();
        priority.setVgap(4);
        priority.setHgap(10);
        priority.add(new Label("Priority: "), 2, 0);
        priority.add(priorityColor, 3, 0);

        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String inputText = textField.getText();
                String selectedPriority = priorityColor.getValue();

                Assignment assignment = new Assignment(inputText, getPriorityColor(selectedPriority));

                Label assignmentLabel = new Label(inputText);
                assignmentLabel.setStyle("-fx-padding: 5px; -fx-background-color: " + assignment.getColor()); // initialising the colors first time


                // Set text in nested VBox
                nestedVBox.getChildren().add(assignmentLabel);

                if (!assignmentsMap.containsKey(selectedDate)) {
                    assignmentsMap.put(selectedDate, new ArrayList<>());
                }
                assignmentsMap.get(selectedDate).add(assignment);

                // Close the input window
                inputStage.close();
            }
        });

        VBox vbox = new VBox(10); // spacing
        vbox.getChildren().addAll(new Label("Enter the name of your assignment: "), textField, submitButton);

        vbox.setAlignment(Pos.CENTER);

        // Set the scene of the input stage
        Scene scene = new Scene(vbox, 300, 150);
        vbox.getChildren().add(priority);
        inputStage.setScene(scene);

        // Show the input window
        inputStage.showAndWait(); // Wait for the input window to close
    }

    private String getPriorityColor(String priority) {
        switch (priority) {
            case "High":
                return "red";
            case "Low":
                return "green";
            default:
                return "yellow";
        }
    }
    public static void main(String[] args) {
        launch(args);
    }

}
