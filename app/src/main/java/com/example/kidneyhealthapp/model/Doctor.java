package com.example.kidneyhealthapp.model;

public class Doctor {
    private  int id;
    private int centerId;
    private  String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String phone;
    private String about;

    public Doctor(int id, int centerId, String firstName, String lastName, String userName, String password, String phone, String about) {
        this.id = id;
        this.centerId = centerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.phone = phone;
        this.about = about;
    }

    public int getId() {
        return id;
    }

    public int getCenterId() {
        return centerId;
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

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getAbout() {
        return about;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCenterId(int centerId) {
        this.centerId = centerId;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
