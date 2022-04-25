package com.example.kidneyhealthapp.model;

import java.io.Serializable;

public class Doctor implements Serializable {
    private  int id;
    private  String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String details;

    public Doctor(int id, String firstName, String lastName, String email, String phone, String details) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.details = details;
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

    public String getDetails() {
        return details;
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

    public void setDetails(String details) {
        this.details = details;
    }
}
