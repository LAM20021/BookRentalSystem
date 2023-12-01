package com.csumb.luism.bookrentalsystem;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Librarian")
public class Librarian {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username;
    private String password;

    public Librarian() {
        // default constructor - no initialization.
    }

    public Librarian(String username, String password) {
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
