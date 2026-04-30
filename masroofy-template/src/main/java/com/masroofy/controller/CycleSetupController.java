package com.masroofy.controller;

import com.masroofy.Main;
import com.masroofy.model.BudgetCycle;
import com.masroofy.service.BudgetCalculator;
import com.masroofy.util.AlertUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

/**
 * Controller for {@code cycle-setup.fxml} — handles US#1 (Set Initial Budget Cycle).
 *
 * <p><b>Sequence diagram (US#1):</b> User → BudgetCycle.validateInputs() →
 * BudgetCalculator.calculateSafeDailyLimit() → LocalStorageRepository.saveCycle() →
 * navigate to dashboard.</p>
 *
 * <p><b>OWNER: Mahmoud Mohamed Elsawy (20240558)</b></p>
 *
 * @version 1.0
 */
public class CycleSetupController implements Initializable {

    // ── FXML fields injected by FXMLLoader ─────────────────────────
    @FXML private TextField  txtTotalAllowance;
    @FXML private DatePicker dpStartDate;
    @FXML private DatePicker dpEndDate;
    @FXML private Label      lblError;

    private BudgetCalculator calculator;

    /**
     * Called automatically by JavaFX after FXML loading.
     * Sets up the calculator and initial UI state.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO (Mahmoud Elsawy):
        // 1. calculator = new BudgetCalculator();
        // 2. lblError.setVisible(false);
        // 3. dpStartDate.setValue(LocalDate.now());   // convenient default
        throw new UnsupportedOperationException("initialize() not implemented yet — Mahmoud Elsawy");
    }

    /**
     * Triggered by the "Save Cycle →" button (US#1).
     *
     * <p>Steps (must match the US#1 sequence diagram exactly):
     * <ol>
     *   <li>Hide lblError.</li>
     *   <li>Parse {@code txtTotalAllowance} as double. Show error on NumberFormatException.</li>
     *   <li>Read start and end dates from DatePickers.</li>
     *   <li>Create a new {@link BudgetCycle}; call {@code cycle.validateInputs(amount, start, end)}.
     *       Show the exception message on IllegalArgumentException.</li>
     *   <li>Compute days = ChronoUnit.DAYS.between(start, end) + 1.</li>
     *   <li>Compute SDL via {@code calculator.calculateSafeDailyLimit(amount, (int) days)}.</li>
     *   <li>Call {@code cycle.initialize(amount, start, end)}; call {@code cycle.setSafeDailyLimit(sdl)}.</li>
     *   <li>Call {@code Main.getRepository().saveCycle(cycle)}.</li>
     *   <li>Call {@link #navigateToDashboard()}.</li>
     * </ol>
     * </p>
     */
    @FXML
    private void onSaveCycleClick() {
        // TODO (Mahmoud Elsawy): Implement the 9 steps listed above.
        throw new UnsupportedOperationException("onSaveCycleClick() not implemented yet — Mahmoud Elsawy");
    }

    /**
     * Displays an error message under the form.
     *
     * @param msg the message to display (in red)
     */
    private void showError(String msg) {
        // TODO (Mahmoud Elsawy): lblError.setText(msg); lblError.setVisible(true);
        throw new UnsupportedOperationException("showError() not implemented yet — Mahmoud Elsawy");
    }

    /**
     * Navigates to the dashboard screen after a successful cycle save.
     * Loads {@code /fxml/dashboard.fxml} with window size 1050×720.
     */
    private void navigateToDashboard() {
        // TODO (Mahmoud Elsawy):
        // FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
        // Scene scene = new Scene(loader.load(), 1050, 720);
        // scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        // Stage stage = (Stage) txtTotalAllowance.getScene().getWindow();
        // stage.setScene(scene); stage.setTitle("Masroofy — Dashboard");
        throw new UnsupportedOperationException("navigateToDashboard() not implemented yet — Mahmoud Elsawy");
    }
}
