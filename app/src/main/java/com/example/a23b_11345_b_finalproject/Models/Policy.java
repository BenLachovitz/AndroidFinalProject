package com.example.a23b_11345_b_finalproject.Models;


public class Policy {
    private String policyIMG;
    private String rDate;
    private String uDate;
    private float price;

    public Policy() {
    }

    public String getPolicyIMG() {
        return policyIMG;
    }

    public Policy setPolicyIMG(String policyIMG) {
        this.policyIMG = policyIMG;
        return this;
    }

    public String getrDate() {
        return rDate;
    }

    public Policy setrDate(String rDate) {
        this.rDate = rDate;
        return this;
    }

    public String getuDate() {
        return uDate;
    }

    public Policy setuDate(String uDate) {
        this.uDate = uDate;
        return this;
    }

    public float getPrice() {
        return price;
    }

    public Policy setPrice(float price) {
        this.price = price;
        return this;
    }

    @Override
    public String toString() {
        return "Policy{" +
                "policyIMG='" + policyIMG + '\'' +
                ", rDate='" + rDate + '\'' +
                ", uDate='" + uDate + '\'' +
                ", price=" + price +
                '}';
    }
}
