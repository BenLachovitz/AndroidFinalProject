package com.example.a23b_11345_b_finalproject.Models;

public class DataOfUser {
    private String userName;
    private String licencePlate;
    private String model;
    private int year;

    public DataOfUser() {
    }

    public String getUserName() {
        return userName;
    }

    public DataOfUser setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public DataOfUser setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
        return this;
    }

    public String getModel() {
        return model;
    }

    public DataOfUser setModel(String model) {
        this.model = model;
        return this;
    }

    public int getYear() {
        return year;
    }

    public DataOfUser setYear(int year) {
        this.year = year;
        return this;
    }

    @Override
    public String toString() {
        return "DataOfUser{" +
                "userName='" + userName + '\'' +
                ", licencePlate='" + licencePlate + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                '}';
    }
}
