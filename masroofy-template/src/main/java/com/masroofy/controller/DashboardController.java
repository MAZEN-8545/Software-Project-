package com.masroofy.controller;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.masroofy.Main;
import com.masroofy.model.BudgetCycle;
import com.masroofy.model.Transaction;
import com.masroofy.service.BudgetCalculator;
import com.masroofy.service.DashboardService;
import com.masroofy.service.DashboardService.DashboardSummary;
import com.masroofy.util.AlertUtils;
import com.masroofy.util.DateUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

/**
 * Controller for dashboard.fxml — handles US#3 and US#4.
 * Only uses DashboardService (Facade pattern).
 *
 * @author Mahmoud Mohamed Elsawy
 * @version 2.0
 */
public class DashboardController implements Initializable {

    @FXML
    private Label lblDailyLimit;
    @FXML
    private Label lblRemainingBalance;
    @FXML
    private Label lblRemainingDays;
    @FXML
    private Label lblTotalSpent;
    @FXML
    private Label lblCycleDates;
    @FXML
    private ProgressBar progressSpent;
    @FXML
    private Label lblSpentPercentage;
    @FXML
    private Label lblStatusBadge;
    @FXML
    private ListView<String> listRecentTx;
    @FXML
    private Label lblNoTx;
    @FXML
    private PieChart pieChartSpending;

    private DashboardService dashboardService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dashboardService = new DashboardService(
                Main.getRepository(), new BudgetCalculator());
        loadDashboard();
    }

    /**
     * Loads all KPIs and updates the UI (US#3 sequence diagram).
     */
    private void loadDashboard() {
        BudgetCycle cycle = Main.getRepository().getActiveCycle(1);
        if (cycle == null) {
            navigateToSetup();
            return;
        }

        // Cycle date range label
        lblCycleDates.setText(cycle.getStartDate() + "  →  " + cycle.getEndDate());

        DashboardSummary summary = dashboardService.getDashboardSummary(cycle.getCycleId());

        // KPI labels
        lblRemainingBalance.setText(String.format("EGP %.2f", summary.remainingBalance));
        lblTotalSpent.setText(String.format("EGP %.2f", summary.totalSpent));
        lblRemainingDays.setText(summary.remainingDays + " days");

        // Safe daily limit — colour by status
        String sdlText = String.format("EGP %.2f", summary.safeDailyLimit);
        lblDailyLimit.setText(sdlText);
        if (summary.spentPercentage >= 100) {
            lblDailyLimit.setStyle("-fx-text-fill:#E74C3C;-fx-font-size:26px;-fx-font-weight:bold;");
        } else if (summary.spentPercentage >= 80) {
            lblDailyLimit.setStyle("-fx-text-fill:#F39C12;-fx-font-size:26px;-fx-font-weight:bold;");
        } else {
            lblDailyLimit.setStyle("-fx-text-fill:#2ECC71;-fx-font-size:26px;-fx-font-weight:bold;");
        }

        // Progress bar
        double progress = Math.min(summary.spentPercentage / 100.0, 1.0);
        progressSpent.setProgress(progress);
        lblSpentPercentage.setText(String.format("%.1f%% spent", summary.spentPercentage));

        // Bar colour via inline style on the accent
        if (summary.spentPercentage >= 100) {
            progressSpent.setStyle("-fx-accent: #E74C3C;");
        } else if (summary.spentPercentage >= 80) {
            progressSpent.setStyle("-fx-accent: #F39C12;");
        } else {
            progressSpent.setStyle("-fx-accent: #2ECC71;");
        }

        // Status badge
        if (summary.isCycleEnded) {
            lblStatusBadge.setText("● Cycle ended");
            lblStatusBadge.setStyle("-fx-text-fill:#E74C3C;-fx-font-size:12px;");
        } else if (summary.isFinalDay) {
            lblStatusBadge.setText("⚠  Final Day — spend wisely!");
            lblStatusBadge.setStyle("-fx-text-fill:#F39C12;-fx-font-size:12px;");
        } else if (summary.spentPercentage >= 80) {
            lblStatusBadge.setText("⚠  80% of budget consumed");
            lblStatusBadge.setStyle("-fx-text-fill:#F39C12;-fx-font-size:12px;");
        } else {
            lblStatusBadge.setText("● Active cycle");
            lblStatusBadge.setStyle("-fx-text-fill:#2ECC71;-fx-font-size:12px;");
        }

        // Recent transactions (last 5)
        loadRecentTransactions(cycle.getCycleId());

        // US#4: Pie chart — spending by category
        loadPieChart(cycle.getCycleId());

        // Cycle ended prompt
        if (summary.isCycleEnded) {
            boolean start = AlertUtils.showConfirm("Cycle Ended",
                    "Your budget cycle has ended. Start a new cycle?");
            if (start)
                navigateToSetup();
        }
    }

    /**
     * Loads up to 5 most recent transactions for the preview list.
     */
    private void loadRecentTransactions(int cycleId) {
        List<Transaction> txs = Main.getRepository().getTransactions(cycleId);
        if (txs.isEmpty()) {
            listRecentTx.setVisible(false);
            lblNoTx.setVisible(true);
            return;
        }
        listRecentTx.setVisible(true);
        lblNoTx.setVisible(false);

        ObservableList<String> items = FXCollections.observableArrayList();
        int limit = Math.min(txs.size(), 5);
        for (int i = 0; i < limit; i++) {
            Transaction tx = txs.get(i);
            String note = (tx.getNote() != null && !tx.getNote().isEmpty()) ? "  ·  " + tx.getNote() : "";
            items.add(String.format("  %-14s  EGP %8.2f   %s%s",
                    tx.getCategory().getDisplayName(),
                    tx.getAmount(),
                    DateUtils.toDisplayString(tx.getTimestamp()),
                    note));
        }
        listRecentTx.setItems(items);
    }

    /**
     * Loads the pie chart showing spending breakdown by category (US#4).
     */
    private void loadPieChart(int cycleId) {
        Map<com.masroofy.model.Category, Double> data = dashboardService.getPieChartData(cycleId);
        if (data.isEmpty()) {
            pieChartSpending.setVisible(false);
            return;
        }
        pieChartSpending.setVisible(true);
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        for (Map.Entry<com.masroofy.model.Category, Double> e : data.entrySet()) {
            String label = e.getKey().getDisplayName();
            double pct = e.getValue();
            pieData.add(new PieChart.Data(label + " (" + String.format("%.1f%%", pct) + ")", pct));
        }
        pieChartSpending.setData(pieData);
        pieChartSpending.setTitle("Spending by Category");
        pieChartSpending.setStyle("-fx-text-fill:#E8F4F8;");
    }

    private void navigateToSetup() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/cycle-setup.fxml"));
            Scene scene = new Scene(loader.load(), 700, 600);
            scene.getStylesheets().add(
                    getClass().getResource("/css/styles.css").toExternalForm());
            Stage stage = (Stage) progressSpent.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Masroofy — Setup Budget Cycle");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Log Expense button */
    @FXML
    private void onLogExpenseClick() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/expense-log.fxml"));
            Scene scene = new Scene(loader.load(), 700, 580);
            scene.getStylesheets().add(
                    getClass().getResource("/css/styles.css").toExternalForm());
            Stage stage = (Stage) lblDailyLimit.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Masroofy — Log Expense");
        } catch (Exception e) {
            AlertUtils.showError("Could not open expense screen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** View History button */
    @FXML
    private void onViewHistoryClick() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/history.fxml"));
            Scene scene = new Scene(loader.load(), 1000, 680);
            scene.getStylesheets().add(
                    getClass().getResource("/css/styles.css").toExternalForm());
            Stage stage = (Stage) lblDailyLimit.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Masroofy — Transaction History");
        } catch (Exception e) {
            AlertUtils.showError("Could not open history: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** New Cycle button */
    @FXML
    private void onNewCycleClick() {
        boolean confirmed = AlertUtils.showConfirm("New Cycle",
                "Starting a new cycle will deactivate the current one. Continue?");
        if (confirmed)
            navigateToSetup();
    }

    /**
     * US#11 — Reset Cycle Early: clears all transactions and resets the current cycle.
     */
    @FXML
    private void onResetCycleClick() {
        BudgetCycle cycle = Main.getRepository().getActiveCycle(1);
        if (cycle == null) {
            AlertUtils.showError("No active cycle found.");
            return;
        }

        boolean confirmed = AlertUtils.showConfirm("Reset Cycle Early",
                "This will delete ALL transactions and reset your cycle to start fresh.\n\n" +
                "Your total allowance will remain: EGP " + String.format("%.2f", cycle.getTotalAllowance()) + "\n\n" +
                "This action cannot be undone. Continue?");

        if (confirmed) {
            // Clear all transactions and reset cycle
            Main.getRepository().resetCycleEarly(cycle.getCycleId(), cycle.getTotalAllowance());

            AlertUtils.showInfo("Cycle Reset", "Your cycle has been reset. All transactions have been cleared.");

            // Refresh dashboard
            loadDashboard();
        }
    }

    /**
     * US#12 — Navigate to Settings screen.
     */
    @FXML
    private void onSettingsClick() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/settings.fxml"));
            Scene scene = new Scene(loader.load(), 600, 500);
            scene.getStylesheets().add(
                    getClass().getResource("/css/styles.css").toExternalForm());
            Stage stage = (Stage) lblDailyLimit.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Masroofy — Settings");
        } catch (Exception e) {
            AlertUtils.showError("Could not open settings: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void refresh() {
        loadDashboard();
    }
}
