package models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private int transactionId;
    private int customerId;
    private LocalDateTime transactionDate;
    private BigDecimal total_amount;

    // Constructor
    public Transaction(int transactionId, int customerId, LocalDateTime transactionDate, BigDecimal total_amount) {
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.transactionDate = transactionDate;
        this.total_amount = total_amount;
    }

    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(BigDecimal total_amount) {
        this.total_amount = total_amount;
    }

    // toString Method
    @Override
    public String toString() {
        return "Transactions{" +
                "transactionId=" + transactionId +
                ", customerId=" + customerId +
                ", transactionDate=" + transactionDate +
                ", totalAmount=" + total_amount +
                '}';
    }
}
