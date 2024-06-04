package com.example.chatapplication.models;

public class User {
    public String id;
    public String name;
    public String email;
    public String role;
    public String image;

    public User() {
    }

    public User(String id, String name, String email, String role, String image) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.image = image;
    }
}
