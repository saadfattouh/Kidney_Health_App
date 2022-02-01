package com.example.kidneyhealthapp.model;

public class Appointment {
    private int id;
    private int doctorId;
    private int patientId;
    private String date;
    private boolean status;
    private String resultInfo;
    private String patientStatus;

    public Appointment(int id, int doctorId, int patientId, String date, boolean status, String resultInfo, String patientStatus) {
        this.id = id;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.date = date;
        this.status = status;
        this.resultInfo = resultInfo;
        this.patientStatus = patientStatus;
    }

    public int getId() {
        return id;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public int getPatientId() {
        return patientId;
    }

    public String getDate() {
        return date;
    }

    public boolean isStatus() {
        return status;
    }

    public String getResultInfo() {
        return resultInfo;
    }

    public String getPatientStatus() {
        return patientStatus;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }

    public void setPatientStatus(String patientStatus) {
        this.patientStatus = patientStatus;
    }
}
