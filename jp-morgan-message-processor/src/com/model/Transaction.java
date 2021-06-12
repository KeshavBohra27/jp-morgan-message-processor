package com.model;

import java.util.Date;

import com.enums.TransactionStatus;

public class Transaction {
    private double value;
    private TransactionStatus transactionStatus;
    private Date transactionDate;

    public Transaction() {
        this.transactionStatus = TransactionStatus.NOT_ADJUSTED;
        this.transactionDate = new Date();
    }

    public Transaction(double value) {
        this.value = value;
        this.transactionStatus = TransactionStatus.NOT_ADJUSTED;
        this.transactionDate = new Date();
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
}
