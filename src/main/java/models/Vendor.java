package models;

public class Vendor {
    private int vendorId;
    private String name;
    private String contactInfo;
    private int marketId;

    public Vendor(int vendorId, String name, String contactInfo, int marketId) {
        this.vendorId = vendorId;
        this.name = name;
        this.contactInfo = contactInfo;
        this.marketId = marketId;
    }

    // Getters and Setters


    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public int getMarketId() {
        return marketId;
    }

    public void setMarketId(int marketId) {
        this.marketId = marketId;
    }
}
