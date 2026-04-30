package com.masroofy.service;

import com.masroofy.model.Category;
import com.masroofy.model.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stateless service for all budget calculations.
 *
 * <p><b>Design patterns:</b>
 * <ul>
 *   <li><b>Strategy</b> — encapsulates interchangeable calculation algorithms.
 *       BudgetCycle and DashboardService call these methods; they never contain the formulas.</li>
 *   <li><b>SRP</b> — pure computation only. No persistence, no UI logic.</li>
 * </ul>
 * </p>
 *
 * <p><b>Sequence diagrams covered:</b> US#1, US#2, US#3, US#4, US#5, US#6.</p>
 *
 * <p><b>OWNER: Mahmoud Mokhtar Mohamed (20242320)</b></p>
 *
 * @version 1.0
 */
public class BudgetCalculator {

    /**
     * Calculates the safe daily spending limit (US#1, US#3, US#5).
     *
     * <p>Formula: {@code remainingBalance / remainingDays}. If remainingDays &lt;= 0, returns 0.</p>
     *
     * @param remainingBalance money left in the cycle
     * @param remainingDays    days left (must be &gt;= 1 to avoid division by zero)
     * @return safe daily limit, or 0 if no days remain
     */
    public double calculateSafeDailyLimit(double remainingBalance, int remainingDays) {
        // TODO (Mahmoud Mokhtar): if (remainingDays <= 0) return 0;
        //                          return remainingBalance / remainingDays;
        throw new UnsupportedOperationException("calculateSafeDailyLimit() not implemented yet — Mahmoud Mokhtar");
    }

    /**
     * Calculates what percentage of the total allowance has been spent (US#6).
     *
     * <p>Formula: {@code (totalSpent / totalAllowance) * 100.0}.
     * Returns 0 if totalAllowance &lt;= 0 (prevents division by zero).</p>
     *
     * @param totalSpent     total amount spent so far
     * @param totalAllowance original cycle budget
     * @return percentage spent, can exceed 100 if over budget
     */
    public double calculateSpentPercentage(double totalSpent, double totalAllowance) {
        // TODO (Mahmoud Mokhtar): Guard for totalAllowance <= 0, then return (totalSpent / totalAllowance) * 100.0
        throw new UnsupportedOperationException("calculateSpentPercentage() not implemented yet — Mahmoud Mokhtar");
    }

    /**
     * Groups transactions by category and totals each group (US#4).
     *
     * <p>Used by {@link DashboardService#getPieChartData(int)} to build the pie chart dataset.
     * Neither DashboardService nor NotificationService iterate transactions themselves — they
     * call this method instead (ISP).</p>
     *
     * @param transactions list of transactions to aggregate
     * @return map of Category → total amount spent in that category
     */
    public Map<Category, Double> aggregateByCategory(List<Transaction> transactions) {
        // TODO (Mahmoud Mokhtar):
        // Map<Category, Double> result = new HashMap<>();
        // For each tx: result.merge(tx.getCategory(), tx.getAmount(), Double::sum);
        // return result;
        throw new UnsupportedOperationException("aggregateByCategory() not implemented yet — Mahmoud Mokhtar");
    }
}
