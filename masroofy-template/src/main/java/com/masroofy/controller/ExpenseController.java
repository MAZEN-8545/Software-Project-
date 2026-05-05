package com.masroofy.controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import com.masroofy.Main;
import com.masroofy.model.BudgetCycle;
import com.masroofy.model.Category;
import com.masroofy.model.CategoryType;
import com.masroofy.model.Transaction;
import com.masroofy.service.BudgetCalculator;
import com.masroofy.service.NotificationService;
import com.masroofy.util.AlertUtils;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for expense-log.fxml — handles US#2 and US#6.
 *
 * @author Mahmoud Mohamed Elsawy
 * @version 2.1
 */
public class ExpenseController implements Initializable {

    @FXML
    private TextField txtAmount;
    @FXML
    private ChoiceBox<CategoryType> cbCategory;
    @FXML
    private TextField txtNote;
    @FXML
    private Label lblError;
    @FXML
    private Label lblDailyLimitHint;

    private NotificationService notificationService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        notificationService = new NotificationService();
        cbCategory.setItems(FXCollections.observableArrayList(CategoryType.values()));
        cbCategory.setValue(CategoryType.FOOD);
        lblError.setVisible(false);

        // Guard: repository may not be ready if FXML is loaded before
        // Application.start()
        if (Main.getRepository() == null) {
            lblDailyLimitHint.setText("—");
            return;
        }

        BudgetCycle activeCycle = Main.getRepository().getActiveCycle(1);
        if (activeCycle != null) {
            BudgetCalculator calc = new BudgetCalculator();
            double sdl = calc.calculateSafeDailyLimit(
                    activeCycle.getRemainingBalance(),
                    (int) activeCycle.getRemainingDays());
            lblDailyLimitHint.setText(String.format("EGP %.2f", sdl));
        } else {
            lblDailyLimitHint.setText("No active cycle");
        }
    }

    /**
     * US#2 — Save Expense button handler.
     *
     * KEY FIX: always fetch the active cycle fresh from the DB at click time.
     * The field populated in initialize() is only used for the hint label and
     * can be stale by the time the user clicks Save. More importantly, after
     * saving the transaction we call updateCycle() to persist the new
     * remaining_balance. Without this the balance column stays at its original
     * value and every subsequent getActiveCycle() returns wrong data.
     */
    @FXML
    private void onSaveExpenseClick() {
        lblError.setVisible(false);

        if (Main.getRepository() == null) {
            showError("Application not ready — please restart.");
            return;
        }

        try {
            String raw = txtAmount.getText();
            if (raw == null || raw.isBlank()) {
                showError("Please enter an amount.");
                return;
            }

            double amount = Double.parseDouble(raw.trim());
            Transaction.validateAmount(amount);

            CategoryType selectedType = cbCategory.getValue();
            if (selectedType == null)
                throw new IllegalArgumentException("Please select a category.");
            Category category = new Category(selectedType);

            // Always fetch fresh — never reuse the initialize() snapshot
            BudgetCycle cycle = Main.getRepository().getActiveCycle(1);
            if (cycle == null) {
                showError("No active budget cycle. Please set up a cycle first.");
                return;
            }

            // Persist transaction
            Transaction tx = new Transaction(amount, category, LocalDateTime.now());
            tx.setNote(txtNote.getText() == null ? "" : txtNote.getText().trim());
            Main.getRepository().saveTransaction(cycle.getCycleId(), tx);

            // Update balance → persist updated cycle → DB stays in sync
            cycle.setRemainingBalance(cycle.getRemainingBalance() - amount);
            cycle.recalculateLimit();
            Main.getRepository().updateCycle(cycle);

            // Notifications (US#6)
            double totalSpent = cycle.getTotalAllowance() - cycle.getRemainingBalance();
            notificationService.checkAndNotify(cycle, totalSpent);

            AlertUtils.showInfo("Saved!",
                    "Expense of EGP " + String.format("%.2f", amount) + " saved successfully.");
            navigateToDashboard();

        } catch (NumberFormatException e) {
            showError("Please enter a valid number.");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            showError("Failed to save expense: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onBackClick() {
        navigateToDashboard();
    }

    private void showError(String msg) {
        lblError.setText(msg);
        lblError.setVisible(true);
    }

    private void navigateToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
            Scene scene = new Scene(loader.load(), 1050, 720);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
            Stage stage = (Stage) txtAmount.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Masroofy — Dashboard");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
