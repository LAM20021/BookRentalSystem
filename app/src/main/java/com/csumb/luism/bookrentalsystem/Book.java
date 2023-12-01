package com.csumb.luism.bookrentalsystem;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Book")
public class Book {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String author;
    private String title;
    private double fee;
    private boolean isReserved;

    private String customerUsername;
    private int pickupDay;
    private int returnDay;

    public Book() {
        // default constructor - no initialization.
    }

    public Book(String title, String author, double fee) {
        this.title = title;
        this.author = author;
        this.fee = fee;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    public int getPickupDay() {
        return pickupDay;
    }

    public void setPickupDay(int pickupDay) {
        this.pickupDay = pickupDay;
    }

    public int getReturnDay() {
        return returnDay;
    }

    public void setReturnDay(int returnDay) {
        this.returnDay = returnDay;
    }
}
