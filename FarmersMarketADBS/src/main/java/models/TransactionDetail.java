package models;

import java.math.BigDecimal;

public class TransactionDetail {
    private int transactionDetailId;
    private int transactionId;
    private int productId;
    private int quantity;
    private BigDecimal totalPrice;

    // Constructor
    public TransactionDetail(int transactionDetailId, int transactionId, int productId,
                             int quantity, BigDecimal totalPrice) {
        this.transactionDetailId = transactionDetailId;
        this.transactionId = transactionId;
        this.productId = productId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    // Getters and Setters
    public int getTransactionDetailId() {
        return transactionDetailId;
    }

    public void setTransactionDetailId(int transactionDetailId) {
        this.transactionDetailId = transactionDetailId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    // toString Method
    @Override
    public String toString() {
        return "TransactionDetail{" +
                "transactionDetailId=" + transactionDetailId +
                ", transactionId=" + transactionId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
