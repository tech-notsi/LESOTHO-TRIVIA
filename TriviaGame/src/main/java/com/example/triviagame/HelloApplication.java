package com.example.triviagame;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HelloApplication extends Application {

    // Declare instance variables
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;

    private Label questionLabel;
    private ToggleGroup answerGroup;
    private Label feedbackLabel;
    private ProgressBar progressBar;
    private Label scoreLabel;

    private RadioButton option1;
    private RadioButton option2;
    private RadioButton option3;
    private RadioButton option4;

    // Method to initialize radio buttons
    private void initializeRadioButtons() {
        option1 = new RadioButton();
        option2 = new RadioButton();
        option3 = new RadioButton();
        option4 = new RadioButton();
        answerGroup = new ToggleGroup();
        option1.setToggleGroup(answerGroup);
        option2.setToggleGroup(answerGroup);
        option3.setToggleGroup(answerGroup);
        option4.setToggleGroup(answerGroup);
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize JavaFX components
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 800, 600);

        //css
        var cssFile = getClass().getResource("Style.css");

        scene.getStylesheets().add(cssFile.toExternalForm());// attach the css file

        initializeRadioButtons(); // Initialize radio buttons
        loadQuestions(); // Load questions


        questionLabel = new Label();
        ImageView imageView = new ImageView();
        feedbackLabel = new Label();
        progressBar = new ProgressBar();
        scoreLabel = new Label("Score: 0");

        Button nextButton = new Button("Next Question");
        nextButton.setOnAction(e -> nextQuestion());

        VBox optionsBox = new VBox(10, option1, option2, option3, option4);
        VBox.setMargin(optionsBox, new Insets(10));

        HBox centerBox = new HBox(10, imageView, optionsBox);
        centerBox.setPadding(new Insets(10));

        // Set layout
        root.setTop(questionLabel);
        root.setCenter(centerBox);
        root.setBottom(new HBox(10, feedbackLabel, progressBar, scoreLabel, nextButton));
        BorderPane.setMargin(root.getBottom(), new Insets(10));

        // Set the title, scene, and show the stage
        primaryStage.setTitle("Trivia Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        displayQuestion(); // Display the first question
    }

    // Method to load questions
    public void loadQuestions() {
        questions = new ArrayList<>();
        // Add text questions and  images
        questions.add(new Question("What is the capital city of Lesotho?",
                new String[]{"Maseru", "Cape Town", "Johannesburg", "Pretoria"}, 0, null));
        questions.add(new Question("Which is the tallest mountain of Lesotho?",
                new String[]{"The Alps", "The Rockies", "The Andes", "The Drakensberg"}, 3, null));
        questions.add(new Question("What is the official language of Lesotho?",
                new String[]{"English", "Seshoeshoe", "Sesotho", "Zulu"}, 2, null));
        questions.add(new Question("What is the name of this waterfall?",
                new String[]{"Metolong Fall", "Maletsunyane Fall", "Mohale Fall", "Katse Fall"}, 1, "image1.jpg"));
        questions.add(new Question("What is the name of this Basotho Hat?",
                new String[]{"Qiloane", "Mokhoro", "Mokorotlo", "Moshoeshoe"}, 2, "image5.jpg"));
    }

    // Method to display the current question
    private void displayQuestion() {
        if (questions.isEmpty()) {
            // Handle the case where no questions are available
            questionLabel.setText("No questions available");
            feedbackLabel.setText("");
            progressBar.setProgress(0);
            return;
        }

        Question currentQuestion = questions.get(currentQuestionIndex);
        questionLabel.setText(currentQuestion.getText());

        // Clear previous image and answer options
        HBox centerBox = (HBox) ((BorderPane) questionLabel.getParent()).getCenter();
        centerBox.getChildren().clear();

        // Add radio buttons back to the layout
        centerBox.getChildren().addAll(option1, option2, option3, option4);

        // Display image if available
        if (currentQuestion.getImagePath() != null && !currentQuestion.getImagePath().isEmpty()) {
            try {
                String url = Objects.requireNonNull(getClass().getResource(currentQuestion.getImagePath())).toExternalForm();
                Image image = new Image(url);

                // Create image view and layout components
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(200);
                imageView.setFitHeight(200);
                imageView.setPreserveRatio(true);
                HBox imageBox = new HBox(imageView);
                imageBox.setPadding(new Insets(10));

                // Insert image into the layout
                centerBox.getChildren().add(0, imageBox);
            } catch (NullPointerException e) {
                // Handle the exception gracefully, e.g., log a warning message
                System.err.println("Error loading image: " + currentQuestion.getImagePath());
            }
        }

        // Populate answer options
        String[] options = currentQuestion.getOptions();
        option1.setText(options[0]);
        option2.setText(options[1]);
        option3.setText(options[2]);
        option4.setText(options[3]);

        progressBar.setProgress((double) (currentQuestionIndex + 1) / questions.size());
    }

    // Method to move to the next question
    // Method to move to the next question
    private void nextQuestion() {
        int num = questions.size();
        RadioButton selectedOption = (RadioButton) answerGroup.getSelectedToggle();
        if (selectedOption != null) {
            // Existing code to evaluate answer and update score
            int selectedOptionIndex = answerGroup.getToggles().indexOf(selectedOption);
            Question currentQuestion = questions.get(currentQuestionIndex);
            if (currentQuestion.getCorrectAnswerIndex() == selectedOptionIndex) {
                feedbackLabel.setText("Correct!");
                score++;
            } else {
                feedbackLabel.setText("Incorrect!");
            }
            scoreLabel.setText("Score: " + score);

            // Handle moving to the next question or ending the game
            currentQuestionIndex++;
            if (currentQuestionIndex < num) {
                displayQuestion();
            } else {
                // End of questions reached
                displayFinalScore();
            }
        } else {
            feedbackLabel.setText("Please select an answer.");
        }
    }

    // Method to display final score summary
    private void displayFinalScore() {
        feedbackLabel.setText("End of questions.");
        progressBar.setProgress(1.0);

        // Calculate final score
        int totalQuestions = questions.size();
        String finalScoreMessage = "Final Score: " + score + " / " + totalQuestions;
        scoreLabel.setText(finalScoreMessage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
