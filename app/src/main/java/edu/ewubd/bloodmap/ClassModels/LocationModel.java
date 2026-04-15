package edu.ewubd.bloodmap.ClassModels;

import com.google.firebase.firestore.DocumentId;

public class LocationModel {
    @DocumentId
    private String id;
    private String name;
    private double latitude;
    private double longitude;
    private String type; // e.g. "hospital" or "area"

    public LocationModel() {
        // Default constructor required for calls to DataSnapshot.getValue(LocationModel.class)
    }

    public LocationModel(String name, double latitude, double longitude, String type) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        // This is what the ArrayAdapter uses to display the suggestions
        return name;
    }
}
