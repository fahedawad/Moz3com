package com.example.moz3com.PackageData;

import java.io.Serializable;

public class itmeList implements Serializable {
    String name;
    String price;
    Double i;
    String date;
    String username;
    String uid;
    double total,tax4,tax10,tax16;
    String type;
    String wieght;

    public String getWieght() {
        return wieght;
    }

    public void setWieght(String wieght) {
        this.wieght = wieght;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private itmeList(){}

    public itmeList(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public Double getI() {
        return i;
    }

    public void setI(Double i) {
        this.i = i;
    }

    public itmeList(String name, String price, Double i) {
        this.name = name;
        this.price = price;
        this.i=i;

    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getTax4() {
        return tax4;
    }

    public void setTax4(double tax4) {
        this.tax4 = tax4;
    }

    public double getTax10() {
        return tax10;
    }

    public void setTax10(double tax10) {
        this.tax10 = tax10;
    }

    public double getTax16() {
        return tax16;
    }

    public void setTax16(double tax16) {
        this.tax16 = tax16;
    }

    public itmeList(String name, String price, Double i, String date, String username, String uid, double total, String type, double tax10, double tax16,String wieght) {
        this.name = name;
        this.price = price;
        this.i = i;
        this.date = date;
        this.username = username;
        this.uid = uid;
        this.total = total;
        this.type = type;
        this.tax10 = tax10;
        this.tax16 = tax16;
        this.wieght=wieght;
    }

    public itmeList(String name, Double i, String date) {
        this.name = name;
        this.i = i;
        this.date = date;
    }

    public itmeList(String name, String price, Double i, String date , String uid) {
        this.name = name;
        this.price = price;
        this.i = i;
        this.date = date;
        this.uid = uid;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public String getDate() {
        return date;
    }

    public String getUsername() {
        return username;
    }

    public String getUid() {
        return uid;
    }
}
