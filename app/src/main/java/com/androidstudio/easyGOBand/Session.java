package com.androidstudio.easyGOBand;

public class Session {
    private int id;
    private String name;

    public Session(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Session() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
