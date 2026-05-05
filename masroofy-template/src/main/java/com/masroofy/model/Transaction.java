package com.masroofy.model;

import java.time.LocalDateTime;


public class Transaction {

    private int transactionId;
    private double amount;
    private String note;
    private LocalDateTime timestamp;
    private Category category;

    public Transaction(double amount, Category category, LocalDateTime timestamp) {
        validateAmount(amount);
        if (category == null)
            throw new IllegalArgumentException("Please select a category.");
        this.amount    = amount;
        this.category  = category;
        this.timestamp = timestamp;
    }


    public Transaction(int transactionId, double amount, String note,
                       Category category, LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.amount        = amount;
        this.note          = note;
        this.category      = category;
        this.timestamp     = timestamp;
    }

    public static void validateAmount(double amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Please enter a valid number.");
    }


    public void editAmount(double newAmount) {
        validateAmount(newAmount);
        this.amount = newAmount;
    }


    public void changeCategory(Category newCategory) {
        if (newCategory == null)
            throw new IllegalArgumentException("Category cannot be null.");
        this.category = newCategory;
    }
    public int getTransactionId()              { return transactionId; }
    public void setTransactionId(int id)       { this.transactionId = id; }

    public double getAmount()                  { return amount; }

    public String getNote()                    { return note; }
    public void setNote(String note)           { this.note = note; }

    public LocalDateTime getTimestamp()        { return timestamp; }

    public Category getCategory()              { return category; }
}