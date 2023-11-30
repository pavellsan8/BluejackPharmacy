package com.example.bluejackpharmacy.Model;

public class Transaction {
    String id;
    String obatId;
    String user;
    String date;
    int quantity;

    public Transaction() {
    }

    public Transaction(String id, String obatId, String user, String date, int quantity) {
        this.id = id;
        this.obatId = obatId;
        this.user = user;
        this.date = date;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObatId() {
        return obatId;
    }

    public void setObatId(String obatId) {
        this.obatId = obatId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
