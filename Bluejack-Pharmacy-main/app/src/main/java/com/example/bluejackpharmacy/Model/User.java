package com.example.bluejackpharmacy.Model;

public class User {
    String id;
    String name;
    String email;
    String password;
    String number;

    public User() {
    }

    public User(String id, String name, String email, String password, String number) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
