package com.masroofy;

import java.io.IOException;

import com.masroofy.model.BudgetCycle;
import com.masroofy.repository.LocalStorageRepository;
import com.masroofy.util.AlertUtils;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Main entry point for the Masroofy Budget Management application.
 *
 * <p>
 * <b>OWNER: Mahmoud Mokhtar Mohamed — Team Leader (20242320)</b></p>
 *
 * @version 2.0
 */
public class Main extends Application {

    /**
     * Shared singleton repository — accessed via {@link #getRepository()}.
     */
    private static LocalStorageRepository repository;

    /**
     * JavaFX start method — called by the JavaFX runtime after
     * launch().
     *
     * @param stage the primary window provided by JavaFX
     * @throws IOException if an FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        // US#1: system loads data stored last time when it starts
        repository = LocalStorageRepository.getInstance();

        // US#12: Check privacy lock before showing main screen
        if (repository.isPrivacyLockEnabled()) {
            if (!showAuthenticationDialog()) {
                // Authentication failed or cancelled - close the app
                System.exit(0);
                return;
            }
        }

        showMainScreen(stage);
    }

    /**
     * US#12 — Shows the PIN authentication dialog.
     *
     * @return true if authentication successful, false otherwise
     */
    private boolean showAuthenticationDialog() {
        int attempts = 0;
        final int maxAttempts = 3;

        while (attempts < maxAttempts) {
            javafx.scene.control.TextInputDialog dialog = new javafx.scene.control.TextInputDialog();
            dialog.setTitle("Privacy Lock — Authentication Required");
            dialog.setHeaderText("🔒 Enter your PIN to access Masroofy");
            dialog.setContentText("PIN:");

            // Use PasswordField for secure input
            PasswordField pwdField = new PasswordField();
            pwdField.setPromptText("Enter 4-6 digit PIN");
            pwdField.setPrefWidth(280);
            pwdField.setPrefHeight(42);
            pwdField.getStyleClass().add("password-field");

            Label attemptLabel = new Label("⚠ Attempt " + (attempts + 1) + " of " + maxAttempts);
            attemptLabel.getStyleClass().add("attempt-label");

            Label infoLabel = new Label("🔒 This app is protected by a privacy lock.");
            infoLabel.setStyle("-fx-text-fill: #B8D4E3; -fx-font-size: 14px;");

            VBox content = new VBox(16);
            content.setStyle("-fx-padding: 8 4; -fx-background-color: #0F1923;");
            content.getChildren().addAll(infoLabel, attemptLabel, pwdField);

            dialog.getDialogPane().setContent(content);
            dialog.getDialogPane().setPrefWidth(400);

            // Apply CSS stylesheet to the dialog
            dialog.getDialogPane().getStylesheets().add(
                getClass().getResource("/css/styles.css").toExternalForm());

            java.util.Optional<String> result = dialog.showAndWait();

            if (result.isPresent()) {
                String pin = pwdField.getText().trim();
                if (repository.validatePin(pin)) {
                    return true; // Authentication successful
                } else {
                    attempts++;
                    if (attempts < maxAttempts) {
                        AlertUtils.showError("Incorrect PIN. " + (maxAttempts - attempts) + " attempts remaining.");
                    }
                }
            } else {
                // User cancelled
                return false;
            }
        }

        AlertUtils.showError("Too many failed attempts. Access denied.");
        return false;
    }

    /**
     * Shows the main application screen.
     */
    private void showMainScreen(Stage stage) throws IOException {
        // Check for active cycle to decide which screen to show first
        BudgetCycle activeCycle = repository.getActiveCycle(1);
        String fxml;
        String title;
        int w, h;

        if (activeCycle == null) {
            fxml = "/fxml/cycle-setup.fxml";
            title = "Masroofy — Setup Budget Cycle";
            w = 700;
            h = 600;
        } else {
            fxml = "/fxml/dashboard.fxml";
            title = "Masroofy — Dashboard";
            w = 1050;
            h = 720;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Scene scene = new Scene(loader.load(), w, h);
        scene.getStylesheets().add(
                getClass().getResource("/css/styles.css").toExternalForm());

        stage.setTitle(title);
        stage.setScene(scene);
        stage.setMinWidth(700);
        stage.setMinHeight(520);
        stage.show();
    }

    /**
     * Returns the shared {@link LocalStorageRepository} instance (Singleton).
     * Used by all controllers to access the database.
     *
     * @return the singleton repository
     */
    public static LocalStorageRepository getRepository() {
        return repository;
    }

    /**
     * Application entry point — passes control to the JavaFX runtime.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        launch();
    }
}
