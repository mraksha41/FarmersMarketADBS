package models;

import java.math.BigDecimal;

public class Product {
    private int pID;
    private String pName;
    private String pCategory;
    private BigDecimal pPrice;
    private int qtyInStock; // Quantity in Stock
    private int vID; // Vendor ID

    // Constructors
    public Product() {
    }

    public Product(int pID, String pName, String pCategory, BigDecimal pPrice, int qtyInStock, int vID) {
        this.pID = pID;
        this.pName = pName;
        this.pCategory = pCategory;
        this.pPrice = pPrice;
        this.qtyInStock = qtyInStock;
        this.vID = vID;
    }

    // Getters and Setters
    public int getpID() {
        return pID;
    }

    public void setpID(int pID) {
        this.pID = pID;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpCategory() {
        return pCategory;
    }

    public void setpCategory(String pCategory) {
        this.pCategory = pCategory;
    }

    public BigDecimal getpPrice() {
        return pPrice;
    }

    public void setpPrice(BigDecimal pPrice) {
        this.pPrice = pPrice;
    }

    public int getQtyInStock() {
        return qtyInStock;
    }

    public void setQtyInStock(int qtyInStock) {
        this.qtyInStock = qtyInStock;
    }

    public int getvID() {
        return vID;
    }

    public void setvID(int vID) {
        this.vID = vID;
    }

    // ToString Method
    @Override
    public String toString() {
        return "Product{" +
                "pID=" + pID +
                ", pName='" + pName + '\'' +
                ", pCategory='" + pCategory + '\'' +
                ", pPrice=" + pPrice +
                ", qtyInStock=" + qtyInStock +
                ", vID=" + vID +
                '}';
    }
}
