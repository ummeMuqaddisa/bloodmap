package edu.ewubd.bloodmap.ClassModels;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BloodBankModel {
    private String bloodBankId;
    private String bankName;
    private String contactNumber;
    private String address;
    private double latitude;
    private double longitude;
    private Map<String, Integer> availableStock;
    private boolean isOpen24Hours;
    private Date lastUpdated;

    public BloodBankModel() {
        this.availableStock = new HashMap<>();
        this.lastUpdated = new Date();
    }

    public BloodBankModel(String bloodBankId, String bankName, String contactNumber, String address,
                          double latitude, double longitude, Map<String, Integer> availableStock,
                          boolean isOpen24Hours) {
        this.bloodBankId = bloodBankId;
        this.bankName = bankName;
        this.contactNumber = contactNumber;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.availableStock = availableStock;
        this.isOpen24Hours = isOpen24Hours;
        this.lastUpdated = new Date();
    }

    public String getBloodBankId() { return bloodBankId; }
    public void setBloodBankId(String bloodBankId) { this.bloodBankId = bloodBankId; }

    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public Map<String, Integer> getAvailableStock() { return availableStock; }
    public void setAvailableStock(Map<String, Integer> availableStock) { this.availableStock = availableStock; }

    public boolean isOpen24Hours() { return isOpen24Hours; }
    public void setOpen24Hours(boolean open24Hours) { isOpen24Hours = open24Hours; }

    public Date getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Date lastUpdated) { this.lastUpdated = lastUpdated; }
}
