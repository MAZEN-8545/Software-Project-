package com.masroofy.model;

import java.time.LocalDateTime;

/**
 * Represents a single expense record. Validates its own fields (SRP).
 *
 * <p><b>Rules:</b> amount must be &gt; 0, category must not be null.
 * No DB or UI logic lives here.</p>
 *
 * <p><b>Sequence diagrams:</b> US#2, US#6, US#7.</p>
 *
 * <p><b>OWNER: Mazen Mahmoud Abd El-Moez (20242258)</b></p>
 *
 * @version 1.0
 */
public class Transaction {

    // ── Fields ────────────────────────────────────────────────────
    private int transactionId;
    private double amount;
    private String note;
    private LocalDateTime timestamp;
    private Category category;

    /**
     * Creates and validates a new Transaction from user input (US#2).
     *
     * @param amount    must be &gt; 0  (validated via {@link #validateAmount(double)})
     * @param category  must not be null
     * @param timestamp when the expense occurred
     * @throws IllegalArgumentException if amount &lt;= 0 or category is null
     */
    public Transaction(double amount, Category category, LocalDateTime timestamp) {
        // TODO (Mazen): Call validateAmount(amount).
        //               If category == null → throw IllegalArgumentException("Please select a category.")
        //               Assign all fields.
        throw new UnsupportedOperationException("Transaction constructor not implemented yet — Mazen");
    }

    /**
     * Constructor for loading records from the database (all fields known).
     *
     * @param transactionId DB primary key
     * @param amount        stored amount
     * @param note          optional note (may be null)
     * @param category      resolved Category object
     * @param timestamp     stored timestamp
     */
    public Transaction(int transactionId, double amount, String note,
                       Category category, LocalDateTime timestamp) {
        // TODO (Mazen): Assign all five fields directly (no validation needed — data came from DB).
        throw new UnsupportedOperationException("DB constructor not implemented yet — Mazen");
    }

    /**
     * Validates that amount is a positive number (US#2 sequence: validateAmount step).
     *
     * @param amount value to check
     * @throws IllegalArgumentException with message "Please enter a valid number." if amount &lt;= 0
     */
    public static void validateAmount(double amount) {
        // TODO (Mazen): if (amount <= 0) throw new IllegalArgumentException("Please enter a valid number.");
        throw new UnsupportedOperationException("validateAmount() not implemented yet — Mazen");
    }

    /**
     * Updates the transaction amount (US#7 edit flow).
     *
     * @param newAmount must be &gt; 0
     */
    public void editAmount(double newAmount) {
        // TODO (Mazen): Call validateAmount(newAmount), then set this.amount = newAmount.
        throw new UnsupportedOperationException("editAmount() not implemented yet — Mazen");
    }

    /**
     * Changes the category of this transaction (US#7 edit flow).
     *
     * @param newCategory must not be null
     * @throws IllegalArgumentException with message "Category cannot be null." if newCategory is null
     */
    public void changeCategory(Category newCategory) {
        // TODO (Mazen): Validate newCategory != null, then set this.category = newCategory.
        throw new UnsupportedOperationException("changeCategory() not implemented yet — Mazen");
    }

    // ── Getters & Setters ─────────────────────────────────────────
    // TODO (Mazen): Add getters and setters for all fields.

    public int getTransactionId()                      { return transactionId; }
    public void setTransactionId(int id)               { this.transactionId = id; }

    public double getAmount()                          { return amount; }

    public String getNote()                            { return note; }
    public void setNote(String note)                   { this.note = note; }

    public LocalDateTime getTimestamp()                { return timestamp; }

    public Category getCategory()                      { return category; }
}
