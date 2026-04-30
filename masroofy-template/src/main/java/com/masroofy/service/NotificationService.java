package com.masroofy.service;

import com.masroofy.model.BudgetCycle;
import com.masroofy.util.AlertUtils;

/**
 * Triggers budget threshold alerts (Observer pattern, SRP).
 *
 * <p><b>Design patterns:</b>
 * <ul>
 *   <li><b>Observer</b> — reacts to BudgetCycle state changes after every Transaction save.
 *       {@link BudgetCycle} never imports this class — fully decoupled.</li>
 *   <li><b>SRP</b> — only fires alerts. Does NOT compute thresholds; delegates to
 *       {@link BudgetCalculator}.</li>
 *   <li><b>OCP</b> — adding a new alert threshold (e.g. 90%) requires adding a new method here,
 *       not modifying existing logic.</li>
 * </ul>
 * </p>
 *
 * <p><b>Sequence diagrams covered:</b> US#2, US#6.</p>
 *
 * <p><b>OWNER: Mahmoud Mokhtar Mohamed (20242320)</b></p>
 *
 * @version 1.0
 */
public class NotificationService {

    /**
     * Threshold percentage that triggers the first warning alert (default: 80%).
     * Configurable for future expansion (OCP).
     */
    private double warningThreshold = 80.0;

    /**
     * Guards against firing the 80% alert more than once per cycle session.
     * Reset to true when a new cycle starts.
     */
    private boolean alertNotYetSent = true;

    private final BudgetCalculator calculator = new BudgetCalculator();

    /**
     * Checks the spending percentage and fires the appropriate alert (US#2, US#6 sequence).
     *
     * <p>Steps:
     * <ol>
     *   <li>Calculate spentPercentage via {@link BudgetCalculator#calculateSpentPercentage}.</li>
     *   <li>If pct &gt;= 100 → call {@link #sendBudgetExhaustedAlert()}.</li>
     *   <li>Else if pct &gt;= warningThreshold AND alertNotYetSent → call {@link #sendEightyPercentAlert()},
     *       then set alertNotYetSent = false.</li>
     * </ol>
     * </p>
     *
     * @param cycle      active budget cycle
     * @param totalSpent total amount spent in the cycle so far
     */
    public void checkAndNotify(BudgetCycle cycle, double totalSpent) {
        // TODO (Mahmoud Mokhtar): Implement the three-step logic described above.
        throw new UnsupportedOperationException("checkAndNotify() not implemented yet — Mahmoud Mokhtar");
    }

    /**
     * Fires the 80% budget warning dialog (US#6).
     * Uses {@link AlertUtils#showWarning(String)}.
     */
    public void sendEightyPercentAlert() {
        // TODO (Mahmoud Mokhtar): AlertUtils.showWarning("You have spent 80% of your budget!...");
        throw new UnsupportedOperationException("sendEightyPercentAlert() not implemented yet — Mahmoud Mokhtar");
    }

    /**
     * Fires the budget-exhausted alert dialog (US#6).
     * Uses {@link AlertUtils#showWarning(String)}.
     */
    public void sendBudgetExhaustedAlert() {
        // TODO (Mahmoud Mokhtar): AlertUtils.showWarning("Your budget is exhausted!...");
        throw new UnsupportedOperationException("sendBudgetExhaustedAlert() not implemented yet — Mahmoud Mokhtar");
    }

    // ── Getters & Setters ─────────────────────────────────────────

    public double getWarningThreshold()                         { return warningThreshold; }
    public void setWarningThreshold(double warningThreshold)    { this.warningThreshold = warningThreshold; }

    /** Resets the 80% alert guard flag. Call this when a new cycle starts. */
    public void resetAlertFlag()                                { this.alertNotYetSent = true; }
}
