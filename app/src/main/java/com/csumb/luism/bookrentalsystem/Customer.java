package com.csumb.luism.bookrentalsystem;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "Customer")
public class Customer {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username;
    private String password;

    public Customer ()
    {
        // default constructor - no initialization.
    }

    public Customer (String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public void setPassword(String password) {
        this.password = password;
    }

}

