package com.example.kidneyhealthapp.model;

import java.util.ArrayList;

public class User {

    //all users have:
    private int id;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String phone;
    private int age;
    private boolean gender;
    private String location;

    private ArrayList<PatientInfo> medicinesList;

    public User(int id, String firstName, String lastName, String userName, String password, String phone, int age, boolean gender, String location, ArrayList<PatientInfo> medicinesList) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.phone = phone;
        this.age = age;
        this.gender = gender;
        this.location = location;

        this.medicinesList = medicinesList;
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

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public int getAge() {
        return age;
    }

    public boolean isGender() {
        return gender;
    }

    public String getLocation() {
        return location;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMedicinesList(ArrayList<PatientInfo> medicinesList) {
        this.medicinesList = medicinesList;
    }

    public ArrayList<PatientInfo> getMedicinesList() {
        return medicinesList;
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

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
