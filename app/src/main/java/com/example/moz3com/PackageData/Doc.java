package com.example.moz3com.PackageData;

public class Doc {
    String name;
    String date;
    String total;
    String totaltxt;
    String sheknum;
    String bank;
    String bio;

    public Doc(String name, String date, String total, String totaltxt, String sheknum, String bank, String bio, String type) {
        this.name = name;
        this.date = date;
        this.total = total;
        this.totaltxt = totaltxt;
        this.sheknum = sheknum;
        this.bank = bank;
        this.bio = bio;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotaltxt() {
        return totaltxt;
    }

    public void setTotaltxt(String totaltxt) {
        this.totaltxt = totaltxt;
    }

    public String getSheknum() {
        return sheknum;
    }

    public void setSheknum(String sheknum) {
        this.sheknum = sheknum;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }


}
