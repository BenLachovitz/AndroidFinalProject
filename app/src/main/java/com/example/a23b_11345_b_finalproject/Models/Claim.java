package com.example.a23b_11345_b_finalproject.Models;

import java.util.ArrayList;
import java.util.Date;

public class Claim {
    private ArrayList<String> images;
    private String description;
    private String witnesses;
    private String claimDate;
    private String location;
    private String uId;
    private boolean stared = false;
    private boolean done = false;

    public Claim() {
    }

    public String getuId() {
        return uId;
    }

    public Claim setuId(String uId) {
        this.uId = uId;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Claim setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getWitnesses() {
        return witnesses;
    }

    public Claim setWitnesses(String witnesses) {
        this.witnesses = witnesses;
        return this;
    }

    public String getClaimDate() {
        return claimDate;
    }

    public Claim setClaimDate(String claimDate) {
        this.claimDate = claimDate;
        return this;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public Claim setImages(ArrayList<String> images) {
        this.images = images;
        return this;
    }

    public boolean isStared() {
        return stared;
    }

    public Claim setStared(boolean stared) {
        this.stared = stared;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public Claim setLocation(String location) {
        this.location = location;
        return this;
    }

    public boolean isDone() {
        return done;
    }

    public Claim setDone(boolean done) {
        this.done = done;
        return this;
    }

    @Override
    public String toString() {
        return "Claim{" +
                "images=" + images +
                ", description='" + description + '\'' +
                ", witnesses='" + witnesses + '\'' +
                ", claimDate='" + claimDate + '\'' +
                ", location='" + location + '\'' +
                ", uId='" + uId + '\'' +
                ", stared=" + stared +
                ", done=" + done +
                '}';
    }
}
