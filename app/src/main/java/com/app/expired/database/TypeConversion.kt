package com.app.expired.database

import android.net.Uri
import androidx.room.TypeConverter
import java.time.LocalDate

class TypeConversion {

    //Converts from string "dd-mm-yyyy" to LocalDate "yyyy-mm-dd"
    @TypeConverter
    fun fromString(value: String): LocalDate {
        return value.let { LocalDate.parse(it) }
    }

    //Converts from LocalDate "yyyy-mm-dd" to string "dd-mm-yyyy"
    @TypeConverter
    fun dateToString(date: LocalDate): String {
        return date.toString()
    }
}