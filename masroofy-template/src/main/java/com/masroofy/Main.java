package com.masroofy;

import com.masroofy.model.BudgetCycle;
import com.masroofy.repository.LocalStorageRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main entry point for the Masroofy Budget Management application.
 *
 * <p>On startup this class:
 * <ol>
 *   <li>Initialises the SQLite database (via {@link LocalStorageRepository#getInstance()}).</li>
 *   <li>Checks for an active budget cycle.</li>
 *   <li>Opens the Dashboard if a cycle exists; otherwise opens the Setup screen (US#1).</li>
 * </ol>
 * </p>
 *
 * <p><b>OWNER: Mahmoud Mokhtar Mohamed — Team Leader (20242320)</b></p>
 *
 * @version 1.0
 */
public class Main extends Application {

    /** Shared singleton repository — accessed via {@link #getRepository()}. */
    private static LocalStorageRepository repository;

    /**
     * JavaFX start method — called by the JavaFX runtime after {@link #launch()}.
     *
     * @param stage the primary window provided by JavaFX
     * @throws IOException if an FXML file cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        // TODO (Mahmoud Mokhtar):
        // 1. repository = LocalStorageRepository.getInstance();
        //    (This call initialises the DB and seeds the default user.)
        //
        // 2. BudgetCycle activeCycle = repository.getActiveCycle(1);
        //
        // 3. Decide which screen to show:
        //    - activeCycle == null → load "/fxml/cycle-setup.fxml", size 700×600, title "Masroofy — Setup Budget Cycle"
        //    - activeCycle != null → load "/fxml/dashboard.fxml", size 1050×720, title "Masroofy — Dashboard"
        //
        // 4. Apply stylesheet: scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        //
        // 5. stage.setTitle(title); stage.setScene(scene);
        //    stage.setMinWidth(700); stage.setMinHeight(520);
        //    stage.show();
        throw new UnsupportedOperationException("start() not implemented yet — Mahmoud Mokhtar");
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
