package com.masroofy.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a budget cycle with defined start/end dates and spending limits.
 * Tracks remaining balance and calculates safe daily spending limits.
 *
 * @version 1.0
 */
public class BudgetCycle {

    /** Database-generated unique identifier. */
    private int cycleId;
    /** Total budget allocated for this cycle. */
    private double totalAllowance;
    /** When the budget cycle begins. */
    private LocalDate startDate;
    /** When the budget cycle ends. */
    private LocalDate endDate;
    /** Money remaining in the cycle (decreases with each expense). */
    private double remainingBalance;
    /** Calculated safe daily spending limit to stay within budget. */
    private double safeDailyLimit;
    /** Whether this cycle is currently active. */
    private boolean active;
    /** All transactions associated with this cycle. */
    private List<Transaction> transactions = new ArrayList<>();

    /**
     * Initializes a new budget cycle with validation.
     * Sets remaining balance to the total allowance and marks cycle as active.
     *
     * @param totalAllowance the budget amount (must be > 0)
     * @param start the cycle start date (cannot be null)
     * @param end the cycle end date (must be after start)
     * @throws IllegalArgumentException if validation fails
     */
    public void initialize(double totalAllowance, LocalDate start, LocalDate end) {
        validateInputs(totalAllowance, start, end);
        this.totalAllowance    = totalAllowance;
        this.startDate         = start;
        this.endDate           = end;
        this.remainingBalance  = totalAllowance;   // full budget at start
        this.active            = true;
        recalculateLimit();                        // set initial safeDailyLimit
    }

    /**
     * Validates cycle inputs before initialization.
     *
     * @param totalAllowance the budget amount
     * @param start the start date
     * @param end the end date
     * @throws IllegalArgumentException if any validation fails
     */
    public void validateInputs(double totalAllowance, LocalDate start, LocalDate end) {
        if (totalAllowance <= 0)
            throw new IllegalArgumentException("Allowance must be a positive number.");
        if (start == null || end == null)
            throw new IllegalArgumentException("Dates cannot be empty.");
        if (!end.isAfter(start))
            throw new IllegalArgumentException("End date must be after start date.");
    }

    /**
     * Calculates the number of days remaining in the cycle (including today).
     *
     * @return days remaining, or 0 if cycle has ended
     */
    public long getRemainingDays() {
        long days = ChronoUnit.DAYS.between(LocalDate.now(), endDate) + 1;
        return Math.max(days, 0);
    }

    /**
     * Recalculates the safe daily limit based on remaining balance and days.
     * Formula: remainingBalance / remainingDays
     */
    public void recalculateLimit() {
        long days = getRemainingDays();
        if (days > 0)
            safeDailyLimit = remainingBalance / days;
        else
            safeDailyLimit = 0;
    }

    /**
     * Applies daily rollover — recalculates limit for a new day.
     *
     * @param today the current date
     */
    public void applyDailyRollover(LocalDate today) {
        long days = getRemainingDays();
        if (days > 0)
            safeDailyLimit = remainingBalance / days;
        else
            safeDailyLimit = 0;
    }

    /**
     * @return the cycle ID
     */
    public int getCycleId()                            { return cycleId; }

    /**
     * @param cycleId the cycle ID to set
     */
    public void setCycleId(int cycleId)                { this.cycleId = cycleId; }

    /**
     * @return the total budget allowance
     */
    public double getTotalAllowance()                  { return totalAllowance; }

    /**
     * @param v the total allowance to set
     */
    public void setTotalAllowance(double v)            { this.totalAllowance = v; }

    /**
     * @return the cycle start date
     */
    public LocalDate getStartDate()                    { return startDate; }

    /**
     * @param d the start date to set
     */
    public void setStartDate(LocalDate d)              { this.startDate = d; }

    /**
     * @return the cycle end date
     */
    public LocalDate getEndDate()                      { return endDate; }

    /**
     * @param d the end date to set
     */
    public void setEndDate(LocalDate d)                { this.endDate = d; }

    /**
     * @return the remaining balance
     */
    public double getRemainingBalance()                { return remainingBalance; }

    /**
     * @param v the remaining balance to set
     */
    public void setRemainingBalance(double v)          { this.remainingBalance = v; }

    /**
     * @return the safe daily spending limit
     */
    public double getSafeDailyLimit()                  { return safeDailyLimit; }

    /**
     * @param v the safe daily limit to set
     */
    public void setSafeDailyLimit(double v)            { this.safeDailyLimit = v; }

    /**
     * @return true if the cycle is active
     */
    public boolean isActive()                          { return active; }

    /**
     * @param active the active status to set
     */
    public void setActive(boolean active)              { this.active = active; }

    /**
     * @return the list of transactions in this cycle
     */
    public List<Transaction> getTransactions()         { return transactions; }

    /**
     * @param t the transaction list to set
     */
    public void setTransactions(List<Transaction> t)   { this.transactions = t; }
}