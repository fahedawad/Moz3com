package com.example.moz3com.PackageData;

public class AccountData {
    String date,total,type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AccountData(String date, String total, String type) {
        this.date = date;
        this.total = total;
        this.type =type;
    }
}
