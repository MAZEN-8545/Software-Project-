package com.masroofy.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.masroofy.Main;
import com.masroofy.model.BudgetCycle;
import com.masroofy.model.Category;
import com.masroofy.model.CategoryType;
import com.masroofy.model.Transaction;
import com.masroofy.util.AlertUtils;
import com.masroofy.util.DateUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.Stage;

/**
 * Controller for history.fxml — handles US#7 (Transaction History Review) and US#8 (Edit/Delete Transaction).
 *
 * @author Mahmoud Mohamed Elsawy
 * @version 3.0
 */
public class HistoryController implements Initializable {

    @FXML
    private ListView<String> listTransactions;
    @FXML
    private ChoiceBox<String> cbFilter;
    @FXML
    private DatePicker dpFrom;
    @FXML
    private DatePicker dpTo;
    @FXML
    private Label lblNoTransactions;
    @FXML
    private Label lblTxCount;
    @FXML
    private Label lblTotalShown;

    private int activeCycleId = -1;
    private List<Transaction> currentTransactions;
    private Transaction selectedTransaction;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> filters = FXCollections.observableArrayList();
        filters.add("All Categories");
        for (CategoryType t : CategoryType.values())
            filters.add(t.getDisplayName());
        cbFilter.setItems(filters);
        cbFilter.setValue("All Categories");

        BudgetCycle cycle = Main.getRepository().getActiveCycle(1);
        if (cycle != null)
            activeCycleId = cycle.getCycleId();

        loadTransactions(null, null, null);
    }

    /**
     * US#7 — loads and renders transactions.
     */
    private void loadTransactions(CategoryType type, String fromDate, String toDate) {
        if (activeCycleId == -1) {
            showEmpty("No active cycle found.");
            updateSummary(List.of());
            return;
        }

        if (type == null && fromDate == null && toDate == null) {
            currentTransactions = Main.getRepository().getTransactions(activeCycleId);
        } else {
            currentTransactions = Main.getRepository().filterTransactions(activeCycleId, type, fromDate, toDate);
        }

        updateSummary(currentTransactions);

        if (currentTransactions.isEmpty()) {
            showEmpty(type == null
                    ? "No Transactions Found — Log your first expense"
                    : "No transactions found for the selected filter.");
            return;
        }

        lblNoTransactions.setVisible(false);
        listTransactions.setVisible(true);

        ObservableList<String> items = FXCollections.observableArrayList();
        for (Transaction tx : currentTransactions) {
            String note = (tx.getNote() != null && !tx.getNote().isEmpty())
                    ? "  ·  " + tx.getNote()
                    : "";
            String line = String.format("  %-14s  EGP %8.2f   %s%s",
                    tx.getCategory().getDisplayName(),
                    tx.getAmount(),
                    DateUtils.toDisplayString(tx.getTimestamp()),
                    note);
            items.add(line);
        }
        listTransactions.setItems(items);
    }

    /**
     * US#8 — Get selected transaction from list.
     */
    private Transaction getSelectedTransaction() {
        int selectedIndex = listTransactions.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0 && currentTransactions != null && selectedIndex < currentTransactions.size()) {
            return currentTransactions.get(selectedIndex);
        }
        return null;
    }

    /**
     * US#8 — Edit selected transaction.
     */
    @FXML
    private void onEditClick() {
        Transaction tx = getSelectedTransaction();
        if (tx == null) {
            AlertUtils.showError("Please select a transaction to edit.");
            return;
        }

        // Show dialog to edit amount
        TextInputDialog amountDialog = new TextInputDialog(String.format("%.2f", tx.getAmount()));
        amountDialog.setTitle("Edit Transaction");
        amountDialog.setHeaderText("Edit Amount");
        amountDialog.setContentText("New amount (EGP):");
        
        amountDialog.showAndWait().ifPresent(amountStr -> {
            try {
                final double[] newAmountHolder = new double[1];
                newAmountHolder[0] = Double.parseDouble(amountStr.trim());
                if (newAmountHolder[0] <= 0) {
                    AlertUtils.showError("Amount must be greater than 0.");
                    return;
                }

                // Show dialog to edit category
                List<String> categories = FXCollections.observableArrayList();
                for (CategoryType t : CategoryType.values()) {
                    categories.add(t.getDisplayName());
                }
                ChoiceDialog<String> categoryDialog = new ChoiceDialog<>(tx.getCategory().getDisplayName(), categories);
                categoryDialog.setTitle("Edit Transaction");
                categoryDialog.setHeaderText("Edit Category");
                categoryDialog.setContentText("Select category:");

                categoryDialog.showAndWait().ifPresent(categoryName -> {
                    final CategoryType[] newTypeHolder = new CategoryType[1];
                    for (CategoryType t : CategoryType.values()) {
                        if (t.getDisplayName().equals(categoryName)) {
                            newTypeHolder[0] = t;
                            break;
                        }
                    }
                    if (newTypeHolder[0] == null) return;

                    // Show dialog to edit note
                    TextInputDialog noteDialog = new TextInputDialog(tx.getNote() != null ? tx.getNote() : "");
                    noteDialog.setTitle("Edit Transaction");
                    noteDialog.setHeaderText("Edit Note (optional)");
                    noteDialog.setContentText("Note:");

                    noteDialog.showAndWait().ifPresent(newNote -> {
                        // Update transaction
                        tx.editAmount(newAmountHolder[0]);
                        tx.changeCategory(new Category(newTypeHolder[0]));
                        tx.setNote(newNote.trim().isEmpty() ? null : newNote.trim());

                        // Save to database
                        Main.getRepository().updateTransaction(tx);

                        // Recalculate and update cycle
                        recalculateAndUpdateCycle();

                        // Refresh the list
                        loadTransactions(null, null, null);

                        AlertUtils.showInfo("Success", "Transaction updated successfully!");
                    });
                });
            } catch (NumberFormatException e) {
                AlertUtils.showError("Invalid amount. Please enter a valid number.");
            }
        });
    }

    /**
     * US#8 — Delete selected transaction.
     */
    @FXML
    private void onDeleteClick() {
        Transaction tx = getSelectedTransaction();
        if (tx == null) {
            AlertUtils.showError("Please select a transaction to delete.");
            return;
        }

        boolean confirmed = AlertUtils.showConfirm("Delete Transaction",
                String.format("Are you sure you want to delete this transaction?\n\n%s - EGP %.2f",
                        tx.getCategory().getDisplayName(), tx.getAmount()));

        if (confirmed) {
            // Delete from database
            Main.getRepository().deleteTransaction(tx.getTransactionId());

            // Recalculate and update cycle
            recalculateAndUpdateCycle();

            // Refresh the list
            loadTransactions(null, null, null);

            AlertUtils.showInfo("Success", "Transaction deleted successfully!");
        }
    }

    /**
     * US#8 — Recalculate cycle after transaction edit/delete and update Safe Daily Limit.
     */
    private void recalculateAndUpdateCycle() {
        BudgetCycle cycle = Main.getRepository().getActiveCycle(1);
        if (cycle == null) return;

        // Get all transactions for this cycle to recalculate totals
        List<Transaction> allTransactions = Main.getRepository().getTransactions(cycle.getCycleId());
        double totalSpent = allTransactions.stream().mapToDouble(Transaction::getAmount).sum();

        // Update remaining balance
        double newRemainingBalance = cycle.getTotalAllowance() - totalSpent;
        cycle.setRemainingBalance(newRemainingBalance);

        // Recalculate safe daily limit
        cycle.recalculateLimit();

        // Update cycle in database
        Main.getRepository().updateCycle(cycle);
    }

    private void updateSummary(List<Transaction> txs) {
        double total = txs.stream().mapToDouble(Transaction::getAmount).sum();
        if (lblTxCount != null)
            lblTxCount.setText(txs.size() + " transaction" + (txs.size() == 1 ? "" : "s"));
        if (lblTotalShown != null)
            lblTotalShown.setText(String.format("EGP %.2f", total));
    }

    private void showEmpty(String message) {
        listTransactions.setVisible(false);
        lblNoTransactions.setText(message);
        lblNoTransactions.setVisible(true);
    }

    /** US#7 opt — Apply Filter button */
    @FXML
    private void onApplyFilterClick() {
        String selected = cbFilter.getValue();
        CategoryType type = null;
        if (selected != null && !selected.equals("All Categories")) {
            for (CategoryType t : CategoryType.values()) {
                if (t.getDisplayName().equals(selected)) {
                    type = t;
                    break;
                }
            }
        }
        String from = dpFrom.getValue() != null ? dpFrom.getValue().toString() : null;
        String to = dpTo.getValue() != null ? dpTo.getValue().toString() : null;
        loadTransactions(type, from, to);
    }

    /** Clear filter button */
    @FXML
    private void onClearFilterClick() {
        cbFilter.setValue("All Categories");
        dpFrom.setValue(null);
        dpTo.setValue(null);
        loadTransactions(null, null, null);
    }

    @FXML
    private void onBackClick() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/dashboard.fxml"));
            Scene scene = new Scene(loader.load(), 1050, 720);
            scene.getStylesheets().add(
                    getClass().getResource("/css/styles.css").toExternalForm());
            Stage stage = (Stage) listTransactions.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Masroofy — Dashboard");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
