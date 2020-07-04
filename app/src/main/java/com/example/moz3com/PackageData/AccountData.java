package com.example.moz3com.PackageData;

public class AccountData {
    String date,total;

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

    public AccountData(String date, String total) {
        this.date = date;
        this.total = total;
    }
}
