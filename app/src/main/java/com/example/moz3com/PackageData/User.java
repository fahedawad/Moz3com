package com.example.moz3com.PackageData;

public class User {
    String user;
    String id;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User(String user, String id) {
        this.user = user;
        this.id =id;
    }
}
