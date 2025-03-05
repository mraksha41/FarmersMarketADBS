package models;

public class Market {
    private int mID;
    private String mName;
    private String operatingHours;
    private int addressID;

    // Constructor
    public Market(int mID, String mName, String operatingHours, int addressID) {
        this.mID = mID;
        this.mName = mName;
        this.operatingHours = operatingHours;
        this.addressID = addressID;
    }

    public Market(int mID, String mName, String operatingHours, Address address) {
        this.mID = mID;
        this.mName = mName;
        this.operatingHours = operatingHours;
        this.addressID = address.getAddressID();
    }

    // Getters and Setters
    public int getMID() {
        return mID;
    }

    public void setMID(int mID) {
        this.mID = mID;
    }

    public String getMName() {
        return mName;
    }

    public void setMName(String mName) {
        this.mName = mName;
    }

    public String getOperatingHours() {
        return operatingHours;
    }

    public void setOperatingHours(String operatingHours) {
        this.operatingHours = operatingHours;
    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    @Override
    public String toString() {
        return "Market{" +
                "mID=" + mID +
                ", mName='" + mName + '\'' +
                ", operatingHours='" + operatingHours + '\'' +
                ", addressID=" + addressID +
                '}';
    }
}