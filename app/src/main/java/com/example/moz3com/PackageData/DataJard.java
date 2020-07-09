package com.example.moz3com.PackageData;

public class DataJard {
    String date,name;
    String total;
    String sale;
    String bay;
    String count;
    String contaval;

    public String getContaval() {
        return contaval;
    }

    public void setContaval(String contaval) {
        this.contaval = contaval;
    }

    public DataJard(String date, String name, String total, String sale, String bay, String count, String mrb7 , String contaval) {
        this.date = date;
        this.name = name;
        this.total = total;
        this.sale = sale;
        this.bay = bay;
        this.count = count;
        this.contaval=contaval;
        this.mrb7 = mrb7;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public String getBay() {
        return bay;
    }

    public void setBay(String bay) {
        this.bay = bay;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getMrb7() {
        return mrb7;
    }

    public void setMrb7(String mrb7) {
        this.mrb7 = mrb7;
    }

    String mrb7;



}
