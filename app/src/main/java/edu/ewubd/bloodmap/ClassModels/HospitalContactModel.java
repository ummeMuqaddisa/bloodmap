package edu.ewubd.bloodmap.ClassModels;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class HospitalContactModel {
    private String hospitalId;
    private String hospitalName;
    private String contactNumber;
    private String address;
    private double latitude;
    private double longitude;
    private List<String> availableFacilities;
    private boolean hasBloodBank;
    private Date createdAt;

    public HospitalContactModel() {
        this.availableFacilities = new ArrayList<>();
        this.createdAt = new Date();
    }

    public HospitalContactModel(String hospitalId, String hospitalName, String contactNumber, String address, 
                                double latitude, double longitude, List<String> availableFacilities, 
                                boolean hasBloodBank) {
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
        this.contactNumber = contactNumber;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.availableFacilities = availableFacilities;
        this.hasBloodBank = hasBloodBank;
        this.createdAt = new Date();
    }

    public String getHospitalId() { return hospitalId; }
    public void setHospitalId(String hospitalId) { this.hospitalId = hospitalId; }

    public String getHospitalName() { return hospitalName; }
    public void setHospitalName(String hospitalName) { this.hospitalName = hospitalName; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public List<String> getAvailableFacilities() { return availableFacilities; }
    public void setAvailableFacilities(List<String> availableFacilities) { this.availableFacilities = availableFacilities; }

    public boolean isHasBloodBank() { return hasBloodBank; }
    public void setHasBloodBank(boolean hasBloodBank) { this.hasBloodBank = hasBloodBank; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
