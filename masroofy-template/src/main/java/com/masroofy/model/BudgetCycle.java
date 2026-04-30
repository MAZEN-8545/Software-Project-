package com.masroofy.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents one budget cycle owned by the user.
 *
 * <p><b>Responsibilities (SRP):</b> Manages lifecycle — initialize, recalculate limits,
 * daily rollover. Does NOT persist itself or trigger alerts.</p>
 *
 * <p><b>Sequence diagrams:</b> US#1, US#2, US#3, US#5.</p>
 *
 * <p><b>OWNER: Mazen Mahmoud Abd El-Moez (20242258)</b></p>
 *
 * @version 1.0
 */
public class BudgetCycle {

    // ── Fields ────────────────────────────────────────────────────
    private int cycleId;
    private double totalAllowance;
    private LocalDate startDate;
    private LocalDate endDate;
    private double remainingBalance;
    private double safeDailyLimit;
    private boolean active;
    private List<Transaction> transactions = new ArrayList<>();

    // ── Core Methods ──────────────────────────────────────────────

    /**
     * Validates and initialises a new BudgetCycle (US#1).
     *
     * <p>Calls {@link #validateInputs(double, LocalDate, LocalDate)} first.
     * Sets remainingBalance = totalAllowance, active = true.</p>
     *
     * @param totalAllowance total budget — must be &gt; 0
     * @param start          start date — must not be null
     * @param end            end date — must be strictly after start
     */
    public void initialize(double totalAllowance, LocalDate start, LocalDate end) {
        // TODO (Mazen): Implement this method.
        // 1. Call validateInputs(totalAllowance, start, end)
        // 2. Set this.totalAllowance = totalAllowance
        // 3. Set this.remainingBalance = totalAllowance  (full balance at start)
        // 4. Set this.startDate = start
        // 5. Set this.endDate = end
        // 6. Set this.active = true
        throw new UnsupportedOperationException("initialize() not implemented yet — Mazen");
    }

    /**
     * Validates cycle inputs. Throws {@link IllegalArgumentException} on bad data.
     *
     * <p>Rules:
     * <ul>
     *   <li>totalAllowance must be &gt; 0  → message: "Allowance must be a positive number."</li>
     *   <li>start or end must not be null → message: "Dates cannot be empty."</li>
     *   <li>end must be strictly after start → message: "End date must be after start date."</li>
     * </ul>
     * </p>
     *
     * @param totalAllowance budget amount
     * @param start          start date
     * @param end            end date
     * @throws IllegalArgumentException if any rule is violated
     */
    public void validateInputs(double totalAllowance, LocalDate start, LocalDate end) {
        // TODO (Mazen): Implement validation rules listed above using if-throw statements.
        throw new UnsupportedOperationException("validateInputs() not implemented yet — Mazen");
    }

    /**
     * Returns remaining days in this cycle including today (min 0).
     *
     * <p>Formula: ChronoUnit.DAYS.between(LocalDate.now(), endDate) + 1, clamped to 0.</p>
     *
     * @return number of days remaining (never negative)
     */
    public long getRemainingDays() {
        // TODO (Mazen): Compute with ChronoUnit.DAYS.between(LocalDate.now(), endDate) + 1
        //               Use Math.max(result, 0) to prevent negative values.
        throw new UnsupportedOperationException("getRemainingDays() not implemented yet — Mazen");
    }

    /**
     * Recalculates safe daily limit after an expense is logged (US#2, US#5).
     *
     * <p>Formula: safeDailyLimit = remainingBalance / remainingDays.
     * If remainingDays == 0, set safeDailyLimit = 0.</p>
     */
    public void recalculateLimit() {
        // TODO (Mazen): Call getRemainingDays(). If days > 0 → safeDailyLimit = remainingBalance / days.
        //               If days == 0 → safeDailyLimit = 0.
        throw new UnsupportedOperationException("recalculateLimit() not implemented yet — Mazen");
    }

    /**
     * Applies daily rollover when a new day starts (US#5).
     *
     * <p>Positive rollover (yesterday's unspent money carried forward) → limit increases (show green).
     * Negative rollover (overspent yesterday) → limit decreases (show orange).
     * Formula is same as recalculateLimit: remainingBalance / remainingDays.</p>
     *
     * @param today today's date (pass LocalDate.now() from caller)
     */
    public void applyDailyRollover(LocalDate today) {
        // TODO (Mazen): Compute getRemainingDays(). If days > 0 → safeDailyLimit = remainingBalance / days.
        //               (The visual colour is applied by the controller — this method just sets the value.)
        throw new UnsupportedOperationException("applyDailyRollover() not implemented yet — Mazen");
    }

    // ── Getters & Setters ─────────────────────────────────────────
    // TODO (Mazen): Add standard getters and setters for all fields.

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
