package com.masroofy.util;

import com.masroofy.model.BudgetCycle;

/**
 * Implements the state machine for a {@link BudgetCycle} as described in the SDS State Diagram.
 *
 * <p>The 8 states in the SDS state diagram are encoded as the {@link CycleState} enum.
 * Each public method represents one transition arrow in the diagram.</p>
 *
 * <p><b>OWNER: Mohamed Arafa Khalaf (20240517)</b></p>
 *
 * @version 1.0
 */
public class StateManager {

    /**
     * All possible states of a BudgetCycle (mirrors the SDS state diagram exactly).
     */
    public enum CycleState {
        /** Cycle inputs entered; validation and SDL calculation in progress. */
        INITIALIZING,
        /** Cycle is saved and running normally. */
        ACTIVE,
        /** An expense was just logged; recalculating limits. */
        EXPENSE_LOGGED,
        /** A new day started; rollover is being applied. */
        DAILY_ROLLOVER,
        /** 80% of budget consumed — warning alert triggered. */
        ALERT_TRIGGERED,
        /** 100% of budget consumed — exhausted alert triggered. */
        BUDGET_EXHAUSTED,
        /** endDate reached — user is prompted to start a new cycle. */
        CYCLE_ENDED,
        /** User reset the cycle — data cleared, navigating to setup. */
        RESET_CLEARED
    }

    private CycleState currentState;
    private final BudgetCycle cycle;

    /**
     * Creates a StateManager for the given cycle, starting in {@link CycleState#INITIALIZING}.
     *
     * @param cycle the BudgetCycle this manager tracks
     */
    public StateManager(BudgetCycle cycle) {
        // TODO (Mohamed Arafa): this.cycle = cycle; this.currentState = CycleState.INITIALIZING;
        throw new UnsupportedOperationException("Constructor not implemented yet — Mohamed Arafa");
    }

    /**
     * Transitions {@code INITIALIZING → ACTIVE} after {@code saveCycle()} succeeds (US#1).
     */
    public void onCycleSaved() {
        // TODO (Mohamed Arafa): currentState = CycleState.ACTIVE;
        throw new UnsupportedOperationException("onCycleSaved() not implemented yet — Mohamed Arafa");
    }

    /**
     * Transitions {@code ACTIVE → EXPENSE_LOGGED} then back to {@code ACTIVE} after a
     * transaction is saved and limits are recalculated (US#2).
     */
    public void onExpenseLogged() {
        // TODO (Mohamed Arafa):
        // 1. currentState = CycleState.EXPENSE_LOGGED;
        // 2. cycle.recalculateLimit();
        // 3. currentState = CycleState.ACTIVE;
        throw new UnsupportedOperationException("onExpenseLogged() not implemented yet — Mohamed Arafa");
    }

    /**
     * Transitions {@code ACTIVE → DAILY_ROLLOVER → ACTIVE} when a new day starts (US#5).
     */
    public void onNewDay() {
        // TODO (Mohamed Arafa):
        // 1. currentState = CycleState.DAILY_ROLLOVER;
        // 2. cycle.applyDailyRollover(java.time.LocalDate.now());
        // 3. currentState = CycleState.ACTIVE;
        throw new UnsupportedOperationException("onNewDay() not implemented yet — Mohamed Arafa");
    }

    /**
     * Transitions to {@code ALERT_TRIGGERED} when spent &gt;= 80% (US#6).
     */
    public void onEightyPercentReached() {
        // TODO (Mohamed Arafa): currentState = CycleState.ALERT_TRIGGERED;
        throw new UnsupportedOperationException("onEightyPercentReached() not implemented yet — Mohamed Arafa");
    }

    /**
     * Transitions to {@code BUDGET_EXHAUSTED} when spent &gt;= 100% (US#6).
     */
    public void onBudgetExhausted() {
        // TODO (Mohamed Arafa): currentState = CycleState.BUDGET_EXHAUSTED;
        throw new UnsupportedOperationException("onBudgetExhausted() not implemented yet — Mohamed Arafa");
    }

    /**
     * Transitions {@code ACTIVE → CYCLE_ENDED} when endDate is reached.
     */
    public void onEndDateReached() {
        // TODO (Mohamed Arafa): currentState = CycleState.CYCLE_ENDED;
        throw new UnsupportedOperationException("onEndDateReached() not implemented yet — Mohamed Arafa");
    }

    /**
     * Transitions to {@code RESET_CLEARED} when user resets the cycle.
     */
    public void onUserReset() {
        // TODO (Mohamed Arafa): currentState = CycleState.RESET_CLEARED;
        throw new UnsupportedOperationException("onUserReset() not implemented yet — Mohamed Arafa");
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
