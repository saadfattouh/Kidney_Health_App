package com.example.kidneyhealthapp.model;

public class Appointment {
    private int id;
    private String doctorName;
    private String centerName;
    private int patientId;
    private String date;
    private int status;
    private String resultInfo;
    private String patientStatus;
    private String patientName;

    public Appointment(int id, String doctorName, String centerName, int patientId, String date, int status, String resultInfo, String patientStatus) {
        this.id = id;
        this.doctorName = doctorName;
        this.centerName = centerName;
        this.patientId = patientId;
        this.date = date;
        this.status = status;
        this.resultInfo = resultInfo;
        this.patientStatus = patientStatus;
    }

    //Constructor for appointment requests
    public Appointment(int id, String doctorName, String centerName, int patientId, String date, int status, String resultInfo, String patientStatus, String patientName) {
        this.id = id;
        this.doctorName = doctorName;
        this.centerName = centerName;
        this.patientId = patientId;
        this.date = date;
        this.status = status;
        this.resultInfo = resultInfo;
        this.patientStatus = patientStatus;
        this.patientName = patientName;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getCenterName() {
        return centerName;
    }

    public int getId() {
        return id;
    }

    public int getPatientId() {
        return patientId;
    }

    public String getDate() {
        return date;
    }

    public int getStatus() {
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

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setResultInfo(String resultInfo) {
        this.resultInfo = resultInfo;
    }

    public void setPatientStatus(String patientStatus) {
        this.patientStatus = patientStatus;
    }
}
