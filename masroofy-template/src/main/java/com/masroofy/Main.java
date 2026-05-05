package com.masroofy;

import java.io.IOException;

import com.masroofy.model.BudgetCycle;
import com.masroofy.repository.LocalStorageRepository;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main entry point for the Masroofy Budget Management application.
 *
 * <p>
 * <b>OWNER: Mahmoud Mokhtar Mohamed — Team Leader (20242320)</b></p>
 *
 * @version 1.0
 */
public class Main extends Application {

    /**
     * Shared singleton repository — accessed via {@link #getRepository()}.
     */
    private static LocalStorageRepository repository;

    /**
     * JavaFX start method — called by the JavaFX runtime after
     * {@link #launch()}.
     *
     * @param stage the primary window provided by JavaFX
     * @throws IOException if an FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        // US#1: system loads data stored last time when it starts
        repository = LocalStorageRepository.getInstance();

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
