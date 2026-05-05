package com.masroofy.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class BudgetCycle {

    private int cycleId;
    private double totalAllowance;
    private LocalDate startDate;
    private LocalDate endDate;
    private double remainingBalance;
    private double safeDailyLimit;
    private boolean active;
    private List<Transaction> transactions = new ArrayList<>();

    public void initialize(double totalAllowance, LocalDate start, LocalDate end) {
        validateInputs(totalAllowance, start, end);
        this.totalAllowance    = totalAllowance;
        this.startDate         = start;
        this.endDate           = end;
        this.remainingBalance  = totalAllowance;   // full budget at start
        this.active            = true;
        recalculateLimit();                        // set initial safeDailyLimit
    }

    public void validateInputs(double totalAllowance, LocalDate start, LocalDate end) {
        if (totalAllowance <= 0)
            throw new IllegalArgumentException("Allowance must be a positive number.");
        if (start == null || end == null)
            throw new IllegalArgumentException("Dates cannot be empty.");
        if (!end.isAfter(start))
            throw new IllegalArgumentException("End date must be after start date.");
    }


    public long getRemainingDays() {
        long days = ChronoUnit.DAYS.between(LocalDate.now(), endDate) + 1;
        return Math.max(days, 0);
    }

    public void recalculateLimit() {
        long days = getRemainingDays();
        if (days > 0)
            safeDailyLimit = remainingBalance / days;
        else
            safeDailyLimit = 0;
    }

    public void applyDailyRollover(LocalDate today) {
        long days = getRemainingDays();
        if (days > 0)
            safeDailyLimit = remainingBalance / days;
        else
            safeDailyLimit = 0;
    }
    public int getCycleId()                            { return cycleId; }
    public void setCycleId(int cycleId)                { this.cycleId = cycleId; }

    public double getTotalAllowance()                  { return totalAllowance; }
    public void setTotalAllowance(double v)            { this.totalAllowance = v; }

    public LocalDate getStartDate()                    { return startDate; }
    public void setStartDate(LocalDate d)              { this.startDate = d; }

    public LocalDate getEndDate()                      { return endDate; }
    public void setEndDate(LocalDate d)                { this.endDate = d; }

    public double getRemainingBalance()                { return remainingBalance; }
    public void setRemainingBalance(double v)          { this.remainingBalance = v; }

    public double getSafeDailyLimit()                  { return safeDailyLimit; }
    public void setSafeDailyLimit(double v)            { this.safeDailyLimit = v; }

    public boolean isActive()                          { return active; }
    public void setActive(boolean active)              { this.active = active; }

    public List<Transaction> getTransactions()         { return transactions; }
    public void setTransactions(List<Transaction> t)   { this.transactions = t; }
}