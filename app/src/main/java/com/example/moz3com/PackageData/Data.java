package com.example.moz3com.PackageData;

public class Data {
    String name;
    String uri;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Data(String name, String uri) {
        this.name = name;
        this.uri = uri;
    }
}
