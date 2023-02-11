package com.harshul.etmoneydemo.data.db

import androidx.room.TypeConverter
import java.util.*

class TypeConverters {

    @TypeConverter
    fun timeStampToDate(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimeStamp(date: Date?): Long? {
        return date?.time
    }

}