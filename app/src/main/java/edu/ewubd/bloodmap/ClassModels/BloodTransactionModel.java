package edu.ewubd.bloodmap.ClassModels;

public class BloodTransactionModel {
    private String transactionId;
    private String requesterUid;
    private String donorUid;
    private String bloodGroup;
    private String hospitalNameArea;
    private String patientName;
    private String contactNumber;
    private String neededByTime;
    private String status;
    private long createdAt;
    private long completedAt;

    public BloodTransactionModel() {
    }

    public BloodTransactionModel(String transactionId, String requesterUid, String bloodGroup, String hospitalNameArea, String patientName, String contactNumber, String neededByTime, String status) {
        this.transactionId = transactionId;
        this.requesterUid = requesterUid;
        this.donorUid = "";
        this.bloodGroup = bloodGroup;
        this.hospitalNameArea = hospitalNameArea;
        this.patientName = patientName;
        this.contactNumber = contactNumber;
        this.neededByTime = neededByTime;
        this.status = status;
        this.createdAt = System.currentTimeMillis();
        this.completedAt = 0;
    }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getRequesterUid() { return requesterUid; }
    public void setRequesterUid(String requesterUid) { this.requesterUid = requesterUid; }

    public String getDonorUid() { return donorUid; }
    public void setDonorUid(String donorUid) { this.donorUid = donorUid; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getHospitalNameArea() { return hospitalNameArea; }
    public void setHospitalNameArea(String hospitalNameArea) { this.hospitalNameArea = hospitalNameArea; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getNeededByTime() { return neededByTime; }
    public void setNeededByTime(String neededByTime) { this.neededByTime = neededByTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getCompletedAt() { return completedAt; }
    public void setCompletedAt(long completedAt) { this.completedAt = completedAt; }
}
