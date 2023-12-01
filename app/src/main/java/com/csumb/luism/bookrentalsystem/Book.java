package com.csumb.luism.bookrentalsystem;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Arrays;
import androidx.room.TypeConverters;

@Entity(tableName = "Book")
public class Book {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String author;
    private String title;
    private double fee;

    private String customerUsername;

    @ColumnInfo(typeAffinity = ColumnInfo.TEXT)
    private char[] availability;

    public Book() {
        // default constructor - no initialization.
    }

    public Book(String title, String author, double fee) {
        this.title = title;
        this.author = author;
        this.fee = fee;
        this.availability = getDefaultAvailability();
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

    public String getCustomerUsername() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    public char[] getDefaultAvailability() {
        char[] defaultAvailability = new char[32]; // Assuming December has 31 days
        Arrays.fill(defaultAvailability, '+'); // Fill the array with '+'
        return defaultAvailability;
    }

    public char[] getAvailability() {
        return availability;
    }

    public void setAvailability(char[] availability) {
        this.availability = availability;
    }


}
