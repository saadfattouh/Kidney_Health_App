package com.example.kidneyhealthapp.model;

import java.io.Serializable;
import java.util.Comparator;

public class Center implements Serializable {

    private int id;
    private String name;
    private String doctorName;
    private double lat;
    private double lon;
    private String location;
    private String info;
    private double distance;

    public String getLocation() {
        return location;
    }

    public Center(int id, String name, double lat, double lon, String location, String info) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.location = location;
        this.info = info;
    }

    public Center(int id, String name, String doctorName, double lat, double lon, String info) {
        this.id = id;
        this.name = name;
        this.doctorName = doctorName;
        this.lat = lat;
        this.lon = lon;
        this.info = info;
    }

    public double getDistance() {
        return distance;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getDoctorName() {
        return doctorName;
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

    public static Comparator<Center> DISTANCECOMPARE = new Comparator<Center>() {
        public int compare(Center s1, Center s2)
        {

            double distance1 = s1.getDistance();
            double distance2 = s2.getDistance();

            if(distance1 > distance2) return 1;
            if(distance2 > distance1) return -1;
            return 0;
            // For descending order
            // rollno2-rollno1;
        }
    };
}
