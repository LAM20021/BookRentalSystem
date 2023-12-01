package com.csumb.luism.bookrentalsystem; // Replace this with your actual package name

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "log_table")
public class LogEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "operationType")
    private String operationType;

    @ColumnInfo(name = "details")
    private String details;

    @ColumnInfo(name = "timestamp")
    private String timestamp;

    // Constructors
    public LogEntry(String operationType, String details, String timestamp) {
        this.operationType = operationType;
        this.details = details;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
