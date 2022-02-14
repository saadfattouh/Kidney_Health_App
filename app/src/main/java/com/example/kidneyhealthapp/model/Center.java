package com.example.kidneyhealthapp.model;

public class Center {
    private int id;
    private String name;
    private String doctorName;

    public String getDoctorName() {
        return doctorName;
    }

    private double lat;
    private double lon;
    private String info;

    public Center(int id, String name, double lat, double lon, String info) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getInfo() {
        return info;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
