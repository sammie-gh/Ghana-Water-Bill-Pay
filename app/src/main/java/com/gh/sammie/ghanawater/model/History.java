package com.gh.sammie.ghanawater.model;

public class History {
    String status;
    String desc;
    String price;
    String email;
    String id;
    String actionPerformed;
    long timeStamp;


    public History() {
    }


    public History(String status, String desc, String price, String email, String id, String actionPerformed, long timeStamp) {
        this.status = status;
        this.desc = desc;
        this.price = price;
        this.email = email;
        this.id = id;
        this.actionPerformed = actionPerformed;
        this.timeStamp = timeStamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActionPerformed() {
        return actionPerformed;
    }

    public void setActionPerformed(String actionPerformed) {
        this.actionPerformed = actionPerformed;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
