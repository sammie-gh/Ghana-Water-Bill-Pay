package com.gh.sammie.ghanawater.model;

public class User {

    String name,phone,image,meter,billTotal;

    public User() {
    }

    public User(String name, String phone, String image, String meter, String billTotal) {
        this.name = name;
        this.phone = phone;
        this.image = image;
        this.meter = meter;
        this.billTotal = billTotal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBillTotal() {
        return billTotal;
    }

    public void setBillTotal(String billTotal) {
        this.billTotal = billTotal;
    }

    public String getMeter() {
        return meter;
    }

    public void setMeter(String meter) {
        this.meter = meter;
    }
}
