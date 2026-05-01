package com.masroofy.service;

import com.masroofy.model.BudgetCycle;
import com.masroofy.util.AlertUtils;

/**
 * Triggers budget threshold alerts (Observer pattern, SRP).
 *
 * @version 1.0
 */
public class NotificationService {

    /**
     * Threshold percentage that triggers the first warning alert (default:
     * 80%). Configurable for future expansion (OCP).
     */
    private double warningThreshold = 80.0;

    /**
     * Guards against firing the 80% alert more than once per cycle session.
     * Reset to true when a new cycle starts.
     */
    private boolean alertNotYetSent = true;

    private final BudgetCalculator calculator = new BudgetCalculator();

    /**
     * Checks the spending percentage and fires the appropriate alert (US#2,
     * US#6 sequence).
     *
     * @param cycle active budget cycle
     * @param totalSpent total amount spent in the cycle so far
     */
    public void checkAndNotify(BudgetCycle cycle, double totalSpent) {
        double pct = calculator.calculateSpentPercentage(totalSpent, cycle.getTotalAllowance());
        if (pct >= 100) {
            sendBudgetExhaustedAlert();
        } else if (pct >= warningThreshold && alertNotYetSent) {
            sendEightyPercentAlert();
            alertNotYetSent = false;
        }
    }

    /**
     * Fires the 80% budget warning dialog (US#6). Uses
     * {@link AlertUtils#showWarning(String)}.
     */
    public void sendEightyPercentAlert() {
        AlertUtils.showWarning("You have spent 80% of your budget!\nSpend carefully for the rest of the cycle.");
    }

    /**
     * Fires the budget-exhausted alert dialog (US#6). Uses
     * {@link AlertUtils#showWarning(String)}.
     */
    public void sendBudgetExhaustedAlert() {
        AlertUtils.showWarning("Your budget is exhausted!\nConsider starting a new cycle.");
    }

    // ── Getters & Setters ─────────────────────────────────────────
    public double getWarningThreshold() {
        return warningThreshold;
    }

    public void setWarningThreshold(double warningThreshold) {
        this.warningThreshold = warningThreshold;
    }

    /**
     * Resets the 80% alert guard flag. Call this when a new cycle starts.
     */
    public void resetAlertFlag() {
        this.alertNotYetSent = true;
    }
}
