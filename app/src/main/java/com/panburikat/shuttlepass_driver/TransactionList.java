package com.panburikat.shuttlepass_driver;

public class TransactionList {
    private String transactionID;
    private String referenceNum;
    private String transactionType;
    private String description;
    private String date;
    private String amount;

    public TransactionList(String transactionID, String referenceNum, String transactionType, String description, String date, String amount) {
        this.transactionID = transactionID;
        this.referenceNum = referenceNum;
        this.transactionType = transactionType;
        this.description = description;
        this.date = date;
        this.amount = amount;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public String getReferenceNum() {
        return referenceNum;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }
}
