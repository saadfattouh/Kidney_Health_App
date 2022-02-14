package com.example.kidneyhealthapp.model;

public class Instruction {

    int id;
    String date;
    String instruction;

    public Instruction(int id, String date, String instruction) {
        this.id = id;
        this.date = date;
        this.instruction = instruction;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}
