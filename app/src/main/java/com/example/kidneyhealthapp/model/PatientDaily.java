package com.example.kidneyhealthapp.model;

public class PatientDaily {
    private int id;
    private int patientId;
    private int water;
    private String medicine;
    private String date;

    public PatientDaily(int id, int patientId, int water, String medicine, String date) {
        this.id = id;
        this.patientId = patientId;
        this.water = water;
        this.medicine = medicine;
        this.date = date;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getWater() {
        return water;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
