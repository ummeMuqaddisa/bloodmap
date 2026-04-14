package edu.ewubd.bloodmap.ClassModels;

import java.util.HashMap;
import java.util.Map;

public class BloodBankModel {
    private String bloodBankId;
    private String bankName;
    private String contactNumber;
    private String address;
    private Map<String, Integer> inventory;

    public BloodBankModel() {
        this.inventory = new HashMap<>();
    }

    public BloodBankModel(String bloodBankId, String bankName, String contactNumber, String address) {
        this.bloodBankId = bloodBankId;
        this.bankName = bankName;
        this.contactNumber = contactNumber;
        this.address = address;
        this.inventory = new HashMap<>();
    }

    public String getBloodBankId() { return bloodBankId; }
    public void setBloodBankId(String bloodBankId) { this.bloodBankId = bloodBankId; }

    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Map<String, Integer> getInventory() { return inventory; }
    public void setInventory(Map<String, Integer> inventory) { this.inventory = inventory; }
}
