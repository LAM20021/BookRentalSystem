package com.csumb.luism.bookrentalsystem;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Customer.class, Book.class, Librarian.class, LogEntry.class}, version = 1)
public abstract class BookRentalSystemDatabase extends RoomDatabase {
    public abstract BookRentalSystemDao getBookRentalSystemDao();

    private volatile static BookRentalSystemDatabase dbInstance;

    public static BookRentalSystemDatabase getDatabase(Context context) {
        if(dbInstance == null) {
            synchronized (Customer.class) {
                if (dbInstance == null) {
                    dbInstance = Room.databaseBuilder(context.getApplicationContext(),
                                    BookRentalSystemDatabase.class,
                                    "BookRentalSystem.DB").
                            allowMainThreadQueries().
                            build();
                }
            }
        }
        return dbInstance;
    }


}

