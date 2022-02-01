package com.example.kidneyhealthapp.model;

public class PatientInfo {

    private int id;
    private int userId;
    private String medical;
    private int quantity;

    public PatientInfo(int id, int userId, String medical, int quantity) {
        this.id = id;
        this.userId = userId;
        this.medical = medical;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getMedical() {
        return medical;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setMedical(String medical) {
        this.medical = medical;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
