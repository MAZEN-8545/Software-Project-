package com.masroofy.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

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

/**
 * Controller for cycle-setup.fxml — handles US#1 (Set Initial Budget Cycle).
 *
 * @author Mahmoud Mohamed Elsawy
 * @version 2.0
 */
public class CycleSetupController implements Initializable {

    @FXML
    private TextField txtTotalAllowance;
    @FXML
    private DatePicker dpStartDate;
    @FXML
    private DatePicker dpEndDate;
    @FXML
    private Label lblError;

    private BudgetCalculator calculator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        calculator = new BudgetCalculator();
        lblError.setVisible(false);
        dpStartDate.setValue(LocalDate.now());
    }

    /**
     * US#1 — Save Cycle button handler.
     * Validates → calculates SDL → saves → navigates to dashboard.
     */
    @FXML
    private void onSaveCycleClick() {
        lblError.setVisible(false);
        try {
            double amount = Double.parseDouble(txtTotalAllowance.getText().trim());
            LocalDate start = dpStartDate.getValue();
            LocalDate end = dpEndDate.getValue();

            BudgetCycle cycle = new BudgetCycle();
            cycle.validateInputs(amount, start, end);

            int days = (int) ChronoUnit.DAYS.between(start, end) + 1;
            double sdl = calculator.calculateSafeDailyLimit(amount, days);

            cycle.initialize(amount, start, end);
            cycle.setSafeDailyLimit(sdl);

            Main.getRepository().saveCycle(cycle);
            navigateToDashboard();

        } catch (NumberFormatException e) {
            showError("Please enter a valid number.");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            showError("Failed to save cycle. Please try again.");
            e.printStackTrace();
        }
    }

    private void showError(String msg) {
        lblError.setText(msg);
        lblError.setVisible(true);
    }

    private void navigateToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/dashboard.fxml"));
            Scene scene = new Scene(loader.load(), 1050, 720);
            scene.getStylesheets().add(
                    getClass().getResource("/css/styles.css").toExternalForm());
            Stage stage = (Stage) txtTotalAllowance.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Masroofy — Dashboard");
        } catch (Exception e) {
            AlertUtils.showError("Could not load dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
