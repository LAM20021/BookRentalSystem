package com.csumb.luism.bookrentalsystem;

import androidx.room.TypeConverter;

public class Converters {

    @TypeConverter
    public static String fromCharArray(char[] charArray) {
        return new String(charArray);
    }

    @TypeConverter
    public static char[] toCharArray(String stringValue) {
        return stringValue.toCharArray();
    }
}
