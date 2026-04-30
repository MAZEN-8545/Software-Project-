package com.masroofy.service;

import java.util.Map;

import com.masroofy.model.BudgetCycle;
import com.masroofy.model.Category;
import com.masroofy.repository.LocalStorageRepository;

/**
 * Facade service — shields the Dashboard UI from backend complexity.
 *
 * <p><b>Design patterns:</b>
 * <ul>
 *   <li><b>Facade</b> — coordinates {@link LocalStorageRepository}, {@link BudgetCalculator},
 *       and {@link BudgetCycle} behind two simple methods. The Dashboard Activity never
 *       touches the repository or calculator directly.</li>
 *   <li><b>ISP</b> — exposes only {@code getDashboardSummary()} and {@code getPieChartData()}.
 *       The expense-entry flow never touches dashboard methods.</li>
 * </ul>
 * </p>
 *
 * <p><b>Sequence diagrams covered:</b> US#3, US#4.</p>
 *
 * <p><b>OWNER: Mahmoud Mokhtar Mohamed (20242320)</b></p>
 *
 * @version 1.0
 */
public class DashboardService {

    private final LocalStorageRepository repo;
    private final BudgetCalculator calculator;

    /**
     * Constructs the service with injected dependencies.
     *
     * @param repo       the singleton repository
     * @param calculator the budget calculator
     */
    public DashboardService(LocalStorageRepository repo, BudgetCalculator calculator) {
        this.repo = repo;
        this.calculator = calculator;
    }

    /**
     * Builds the complete dashboard summary for the active cycle (US#3 sequence).
     *
     * <p>Steps (all delegated — no direct SQL here):
     * <ol>
     *   <li>Call {@code repo.getActiveCycle(1)}. If null, return empty {@link DashboardSummary}.</li>
     *   <li>Call {@code repo.getTransactions(cycleId)} to get all transactions.</li>
     *   <li>Compute {@code totalSpent} by summing all transaction amounts.</li>
     *   <li>Compute {@code remainingBalance = cycle.getTotalAllowance() - totalSpent}.</li>
     *   <li>Compute {@code remainingDays = cycle.getRemainingDays()}.</li>
     *   <li>Compute {@code sdl} via {@link BudgetCalculator#calculateSafeDailyLimit}
     *       (pass 0 if remainingDays == 0).</li>
     *   <li>Fill and return the {@link DashboardSummary} object.</li>
     * </ol>
     * </p>
     *
     * @param cycleId the active cycle ID
     * @return populated summary; all fields are 0/false if no active cycle
     */
    public DashboardSummary getDashboardSummary(int cycleId) {
        // TODO (Mazen): Follow the 7 steps described above.
        throw new UnsupportedOperationException("getDashboardSummary() not implemented yet — Mazen");
    }

    /**
     * Builds the pie chart dataset for the active cycle (US#4 sequence).
     *
     * <p>Steps:
     * <ol>
     *   <li>Get active cycle — return empty map if null.</li>
     *   <li>Get transactions — return empty map if list is empty.</li>
     *   <li>Call {@link BudgetCalculator#aggregateByCategory} to get totals per category.</li>
     *   <li>For each entry, compute percentage via
     *       {@link BudgetCalculator#calculateSpentPercentage}.</li>
     *   <li>Return map of Category → percentage.</li>
     * </ol>
     * </p>
     *
     * @param cycleId the active cycle ID
     * @return map of Category to percentage of total allowance spent; empty if no data
     */
    public Map<Category, Double> getPieChartData(int cycleId) {
        // TODO (Mazen): Follow the 5 steps above.
        throw new UnsupportedOperationException("getPieChartData() not implemented yet — Mazen");
    }

    // ── Inner class — DashboardSummary ────────────────────────────

    /**
     * Plain data object returned by {@link #getDashboardSummary(int)}.
     * The controller reads from public fields — no getters needed.
     */
    public static class DashboardSummary {
        /** Money remaining in the cycle. */
        public double remainingBalance;
        /** Safe daily spending limit for today. */
        public double safeDailyLimit;
        /** Total spent so far in the cycle. */
        public double totalSpent;
        /** Percentage of totalAllowance spent (0–100+). */
        public double spentPercentage;
        /** Number of days left (including today). */
        public long remainingDays;
        /** True if today is the last day of the cycle. */
        public boolean isFinalDay;
        /** True if the cycle end date has passed. */
        public boolean isCycleEnded;

        /** Default constructor — all numeric fields default to 0, booleans to false. */
        public DashboardSummary() {}
    }
}
