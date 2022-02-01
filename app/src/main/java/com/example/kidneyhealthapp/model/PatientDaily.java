package com.example.kidneyhealthapp.model;

public class PatientDaily {
    private int id;
    private int patientId;
    private boolean hasTaken;

    public PatientDaily(int id, int patientId, boolean hasTaken) {
        this.id = id;
        this.patientId = patientId;
        this.hasTaken = hasTaken;
    }

    public int getId() {
        return id;
    }

    public int getPatientId() {
        return patientId;
    }

    public boolean isHasTaken() {
        return hasTaken;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public void setHasTaken(boolean hasTaken) {
        this.hasTaken = hasTaken;
    }
}
