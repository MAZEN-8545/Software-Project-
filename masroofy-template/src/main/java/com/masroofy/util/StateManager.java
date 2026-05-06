package com.masroofy.util;

import com.masroofy.model.BudgetCycle;

/**
 * Implements the state machine for a {@link BudgetCycle} as described in the
 * SDS State Diagram.
 *
 * <b>OWNER: Mohamed Arafa Khalaf (20240517)</b>
 *
 * @version 1.0
 */
public class StateManager {

    /**
     * All possible states of a BudgetCycle (mirrors the SDS state diagram
     * exactly).
     */
    public enum CycleState {
        /**
         * Cycle inputs entered; validation and SDL calculation in progress.
         */
        INITIALIZING,
        /**
         * Cycle is saved and running normally.
         */
        ACTIVE,
        /**
         * An expense was just logged; recalculating limits.
         */
        EXPENSE_LOGGED,
        /**
         * A new day started; rollover is being applied.
         */
        DAILY_ROLLOVER,
        /**
         * 80% of budget consumed — warning alert triggered.
         */
        ALERT_TRIGGERED,
        /**
         * 100% of budget consumed — exhausted alert triggered.
         */
        BUDGET_EXHAUSTED,
        /**
         * endDate reached — user is prompted to start a new cycle.
         */
        CYCLE_ENDED,
        /**
         * User reset the cycle — data cleared, navigating to setup.
         */
        RESET_CLEARED
    }

    private CycleState currentState;
    private final BudgetCycle cycle;

    /**
     * Creates a StateManager for the given cycle, starting in
     * {@link CycleState#INITIALIZING}.
     *
     * @param cycle the BudgetCycle this manager tracks
     */
    public StateManager(BudgetCycle cycle) {
        if (cycle == null) {
            throw new IllegalArgumentException("BudgetCycle must not be null.");
        }
        this.cycle = cycle;
        this.currentState = CycleState.INITIALIZING;
    }

    /**
     * Transitions {@code INITIALIZING → ACTIVE} after {@code saveCycle()}
     * succeeds (US#1).
     */
    public void onCycleSaved() {
        currentState = CycleState.ACTIVE;
    }

    /**
     * Transitions {@code ACTIVE → EXPENSE_LOGGED} then back to {@code ACTIVE}
     * after a transaction is saved and limits are recalculated (US#2).
     */
    public void onExpenseLogged() {
        currentState = CycleState.EXPENSE_LOGGED;
        cycle.recalculateLimit();
        currentState = CycleState.ACTIVE;
    }

    /**
     * Transitions {@code ACTIVE → DAILY_ROLLOVER → ACTIVE} when a new day
     * starts (US#5).
     */
    public void onNewDay() {
        currentState = CycleState.DAILY_ROLLOVER;
        cycle.applyDailyRollover(java.time.LocalDate.now());
        currentState = CycleState.ACTIVE;
    }

    /**
     * Transitions to {@code ALERT_TRIGGERED} when spent &gt;= 80% (US#6).
     */
    public void onEightyPercentReached() {
        currentState = CycleState.ALERT_TRIGGERED;
    }

    /**
     * Transitions to {@code BUDGET_EXHAUSTED} when spent &gt;= 100% (US#6).
     */
    public void onBudgetExhausted() {
        currentState = CycleState.BUDGET_EXHAUSTED;
    }

    /**
     * Transitions {@code ACTIVE → CYCLE_ENDED} when endDate is reached.
     */
    public void onEndDateReached() {
        currentState = CycleState.CYCLE_ENDED;
    }

    /**
     * Transitions to {@code RESET_CLEARED} when user resets the cycle.
     */
    public void onUserReset() {
        currentState = CycleState.RESET_CLEARED;
    }

    /**
     * Returns the current state of the cycle.
     *
     * @return the current {@link CycleState}
     */
    public CycleState getCurrentState() {
        return currentState;
    }
}
