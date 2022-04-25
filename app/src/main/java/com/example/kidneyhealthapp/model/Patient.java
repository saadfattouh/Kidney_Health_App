package com.example.kidneyhealthapp.model;

import java.io.Serializable;

public class Patient implements Serializable {

    //all users have:
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private int age;
    private int gender;
    private LatLon location;


    public Patient(int id, String firstName, String lastName, String email, String phone, String address, int age, int gender) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.age = age;
        this.gender = gender;
    }

    public String getAddress() {
        return address;
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

    public String getEmail() {
        return email;
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

    public void setEmail(String email) {
        this.email = email;
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
