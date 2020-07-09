package com.example.moz3com.PackageData;

public class DataJard {
    String date,name;
    double total;
    double sale;
    double bay;
    double count;
    double mrb7;

    public double getContaval() {
        return contaval;
    }

    public void setContaval(double contaval) {
        this.contaval = contaval;
    }

    public DataJard(String date, String name, double total, double sale, double bay, double count, double mrb7, double contaval) {
        this.date = date;
        this.name = name;
        this.total = total;
        this.sale = sale;
        this.bay = bay;
        this.count = count;
        this.mrb7 = mrb7;
        this.contaval = contaval;
    }

    double contaval;

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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getSale() {
        return sale;
    }

    public void setSale(double sale) {
        this.sale = sale;
    }

    public double getBay() {
        return bay;
    }

    public void setBay(double bay) {
        this.bay = bay;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public double getMrb7() {
        return mrb7;
    }

    public void setMrb7(double mrb7) {
        this.mrb7 = mrb7;
    }


}
