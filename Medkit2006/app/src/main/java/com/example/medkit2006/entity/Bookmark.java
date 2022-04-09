package com.example.medkit2006.entity;

public class Bookmark {

    private String facilityName;
    private String notes;

    public Bookmark(String facilityName, String notes) {
        this.facilityName = facilityName;
        this.notes = notes;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getFacilityName() {
        return this.facilityName;
    }

}
