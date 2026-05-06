package com.masroofy.model;

import java.time.LocalDateTime;

/**
 * Represents a single expense transaction within a budget cycle.
 * Contains amount, category, optional note, and timestamp.
 *
 * @version 1.0
 */
public class Transaction {

    /** Database-generated unique identifier. */
    private int transactionId;
    /** Amount spent in this transaction (must be positive). */
    private double amount;
    /** Optional note describing the expense. */
    private String note;
    /** When the transaction occurred. */
    private LocalDateTime timestamp;
    /** The category this expense belongs to. */
    private Category category;

    /**
     * Constructs a new Transaction for saving (no ID yet).
     *
     * @param amount the expense amount (must be > 0)
     * @param category the expense category (cannot be null)
     * @param timestamp when the transaction occurred
     * @throws IllegalArgumentException if amount &lt;= 0 or category is null
     */
    public Transaction(double amount, Category category, LocalDateTime timestamp) {
        validateAmount(amount);
        if (category == null)
            throw new IllegalArgumentException("Please select a category.");
        this.amount    = amount;
        this.category  = category;
        this.timestamp = timestamp;
    }

    /**
     * Constructs a Transaction loaded from the database (has ID).
     *
     * @param transactionId the database ID
     * @param amount the expense amount
     * @param note optional description
     * @param category the expense category
     * @param timestamp when the transaction occurred
     */
    public Transaction(int transactionId, double amount, String note,
                       Category category, LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.amount        = amount;
        this.note          = note;
        this.category      = category;
        this.timestamp     = timestamp;
    }

    /**
     * Validates that the amount is positive.
     *
     * @param amount the amount to validate
     * @throws IllegalArgumentException if amount &lt;= 0
     */
    public static void validateAmount(double amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Please enter a valid number.");
    }

    /**
     * Updates the transaction amount after validation.
     *
     * @param newAmount the new amount (must be > 0)
     * @throws IllegalArgumentException if newAmount &lt;= 0
     */
    public void editAmount(double newAmount) {
        validateAmount(newAmount);
        this.amount = newAmount;
    }

    /**
     * Changes the category of this transaction.
     *
     * @param newCategory the new category (cannot be null)
     * @throws IllegalArgumentException if newCategory is null
     */
    public void changeCategory(Category newCategory) {
        if (newCategory == null)
            throw new IllegalArgumentException("Category cannot be null.");
        this.category = newCategory;
    }

    /**
     * @return the transaction ID
     */
    public int getTransactionId()              { return transactionId; }

    /**
     * @param id the transaction ID to set
     */
    public void setTransactionId(int id)       { this.transactionId = id; }

    /**
     * @return the transaction amount
     */
    public double getAmount()                  { return amount; }

    /**
     * @return the optional note
     */
    public String getNote()                    { return note; }

    /**
     * @param note the note to set
     */
    public void setNote(String note)           { this.note = note; }

    /**
     * @return the transaction timestamp
     */
    public LocalDateTime getTimestamp()        { return timestamp; }

    /**
     * @return the transaction category
     */
    public Category getCategory()              { return category; }
}