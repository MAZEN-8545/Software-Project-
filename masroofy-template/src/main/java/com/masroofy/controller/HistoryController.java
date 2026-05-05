package com.masroofy.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.masroofy.Main;
import com.masroofy.model.BudgetCycle;
import com.masroofy.model.CategoryType;
import com.masroofy.model.Transaction;
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
import javafx.stage.Stage;

/**
 * Controller for history.fxml — handles US#7 (Transaction History Review).
 *
 * @author Mahmoud Mohamed Elsawy
 * @version 2.0
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

        List<Transaction> txs;
        if (type == null && fromDate == null && toDate == null) {
            txs = Main.getRepository().getTransactions(activeCycleId);
        } else {
            txs = Main.getRepository().filterTransactions(activeCycleId, type, fromDate, toDate);
        }

        updateSummary(txs);

        if (txs.isEmpty()) {
            showEmpty(type == null
                    ? "No Transactions Found — Log your first expense"
                    : "No transactions found for the selected filter.");
            return;
        }

        lblNoTransactions.setVisible(false);
        listTransactions.setVisible(true);

        ObservableList<String> items = FXCollections.observableArrayList();
        for (Transaction tx : txs) {
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
