package com.kelompok4.pengenalantanaman.model;

public class User {

    private String username;


    private String created;

    public User(String username, String username1, String created) {
        this.username = username1;
        this.created = created;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
