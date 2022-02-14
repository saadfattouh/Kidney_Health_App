package com.example.kidneyhealthapp.model;

import java.util.ArrayList;

public class User {

    //all users have:
    private int id;
    private String firstName;
    private String lastName;
    private String userName;
    private String phone;
    private int age;
    private int gender;
    private LatLon location;


    public User(int id, String firstName, String lastName, String userName, String phone, int age, int gender, LatLon location, ArrayList<PatientInfo> medicinesList) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.phone = phone;
        this.age = age;
        this.gender = gender;
        this.location = location;
    }

    public User(int id, String firstName, String lastName, String userName, String phone, int age, int gender) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.phone = phone;
        this.age = age;
        this.gender = gender;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhone() {
        return phone;
    }

    public int getAge() {
        return age;
    }



    public LatLon getLocation() {
        return location;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAge(int age) {
        this.age = age;
    }



    public void setLocation(LatLon location) {
        this.location = location;
    }
}
