package com.masroofy.service;

import java.util.List;
import java.util.Map;

import com.masroofy.model.BudgetCycle;
import com.masroofy.model.Category;
import com.masroofy.model.Transaction;
import com.masroofy.repository.LocalStorageRepository;

/**
 * Facade service — shields the Dashboard UI from backend complexity.
 * 
 * <b>Sequence diagrams covered:</b> US#3, US#4.</p>
 *
 * <p>
 * <b>OWNER: Mahmoud Mokhtar Mohamed (20242320)</b></p>
 *
 * @version 1.0
 */
public class DashboardService {

    private final LocalStorageRepository repo;
    private final BudgetCalculator calculator;

    /**
     * Constructs the service with injected dependencies.
     *
     * @param repo the singleton repository
     * @param calculator the budget calculator
     */
    public DashboardService(LocalStorageRepository repo, BudgetCalculator calculator) {
        this.repo = repo;
        this.calculator = calculator;
    }

    /**
     * Builds the complete dashboard summary for the active cycle (US#3
     * sequence).
     *
     * @param cycleId the active cycle ID
     * @return populated summary; all fields are 0/false if no active cycle
     */
    public DashboardSummary getDashboardSummary(int cycleId) {
        DashboardSummary summary = new DashboardSummary();
        BudgetCycle cycle = repo.getActiveCycle(1);
        if (cycle == null) {
            return summary;
        }

        List<Transaction> txs = repo.getTransactions(cycleId);
        double totalSpent = txs.stream().mapToDouble(Transaction::getAmount).sum();
        double remainingBalance = cycle.getTotalAllowance() - totalSpent;
        long remainingDays = cycle.getRemainingDays();
        double sdl = (remainingDays > 0)
                ? calculator.calculateSafeDailyLimit(remainingBalance, (int) remainingDays)
                : 0.0;

        summary.remainingBalance = remainingBalance;
        summary.safeDailyLimit = sdl;
        summary.totalSpent = totalSpent;
        summary.spentPercentage = calculator.calculateSpentPercentage(totalSpent, cycle.getTotalAllowance());
        summary.remainingDays = remainingDays;
        summary.isFinalDay = remainingDays == 1;
        summary.isCycleEnded = remainingDays <= 0;
        return summary;
    }

    /**
     * Builds the pie chart dataset for the active cycle (US#4 sequence).
     *
     * @param cycleId the active cycle ID
     * @return map of Category to percentage of total allowance spent; empty if
     * no data
     */
    public Map<Category, Double> getPieChartData(int cycleId) {
        BudgetCycle cycle = repo.getActiveCycle(1);
        if (cycle == null) {
            return Map.of();
        }
        List<Transaction> txs = repo.getTransactions(cycleId);
        if (txs.isEmpty()) {
            return Map.of();
        }
        Map<Category, Double> totals = calculator.aggregateByCategory(txs);
        Map<Category, Double> pct = new java.util.HashMap<>();
        for (Map.Entry<Category, Double> e : totals.entrySet()) {
            pct.put(e.getKey(), calculator.calculateSpentPercentage(e.getValue(), cycle.getTotalAllowance()));
        }
        return pct;
    }

    // ── Inner class — DashboardSummary ────────────────────────────
    /**
     * Plain data object returned by {@link #getDashboardSummary(int)}. The
     * controller reads from public fields — no getters needed.
     */
    public static class DashboardSummary {

        /**
         * Money remaining in the cycle.
         */
        public double remainingBalance;
        /**
         * Safe daily spending limit for today.
         */
        public double safeDailyLimit;
        /**
         * Total spent so far in the cycle.
         */
        public double totalSpent;
        /**
         * Percentage of totalAllowance spent (0–100+).
         */
        public double spentPercentage;
        /**
         * Number of days left (including today).
         */
        public long remainingDays;
        /**
         * True if today is the last day of the cycle.
         */
        public boolean isFinalDay;
        /**
         * True if the cycle end date has passed.
         */
        public boolean isCycleEnded;

        /**
         * Default constructor — all numeric fields default to 0, booleans to
         * false.
         */
        public DashboardSummary() {
        }
    }
}
