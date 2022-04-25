package com.example.kidneyhealthapp.model;

public class PatientDaily {
    private int id;
    private int water;
    private String medicine;
    private String date;

    public PatientDaily(int id, int water, String medicine, String date) {
        this.id = id;
        this.water = water;
        this.medicine = medicine;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getWater() {
        return water;
    }

    public String getMedicine() {
        return medicine;
    }

    public String getDate() {
        return date;
    }

}
