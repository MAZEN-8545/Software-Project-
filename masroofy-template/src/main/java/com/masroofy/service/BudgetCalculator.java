package com.masroofy.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.masroofy.model.Category;
import com.masroofy.model.Transaction;


/**
 * Stateless service for all budget calculations (SRP, Strategy pattern).
 * No persistence, no UI — pure computation only.
 *
 * Sequence diagrams: US#1, US#2, US#3, US#4, US#5, US#6.
 *
 * @author Mahmoud Mokhtar Mohamed
 * @version 1.0
 */
public class BudgetCalculator {

    /**
     * Calculates the safe daily spending limit (US#1, US#3, US#5).
     *
     * <p>
     * Formula: {@code remainingBalance / remainingDays}. If remainingDays &lt;=
     * 0, returns 0.</p>
     *
     * @param remainingBalance money left in the cycle
     * @param remainingDays days left (must be &gt;= 1 to avoid division by
     * zero)
     * @return safe daily limit, or 0 if no days remain
     */
    public double calculateSafeDailyLimit(double remainingBalance, int remainingDays) {
        if (remainingDays <= 0) {
            return 0;
        }
        return remainingBalance / remainingDays;
    }

    /**
     * Calculates what percentage of the total allowance has been spent (US#6).
     *
     * <p>
     * Formula: {@code (totalSpent / totalAllowance) * 100.0}. Returns 0 if
     * totalAllowance &lt;= 0 (prevents division by zero).</p>
     *
     * @param totalSpent total amount spent so far
     * @param totalAllowance original cycle budget
     * @return percentage spent, can exceed 100 if over budget
     */
    public double calculateSpentPercentage(double totalSpent, double totalAllowance) {
        if (totalAllowance <= 0) {
            return 0;
        }
        return (totalSpent / totalAllowance) * 100.0;
    }

    /**
     * Groups transactions by category and totals each group (US#4).
     *
     * <p>
     * Used by {@link DashboardService#getPieChartData(int)} to build the pie
     * chart dataset. Neither DashboardService nor NotificationService iterate
     * transactions themselves — they call this method instead (ISP).</p>
     *
     * @param transactions list of transactions to aggregate
     * @return map of Category → total amount spent in that category
     */
    public Map<Category, Double> aggregateByCategory(List<Transaction> transactions) {
        Map<Category, Double> result = new HashMap<>();
        for (Transaction tx : transactions) {
            Category cat = tx.getCategory();
            result.merge(cat, tx.getAmount(), Double::sum);
        }
        return result;
    }
}
