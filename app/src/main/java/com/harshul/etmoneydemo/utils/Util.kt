package com.harshul.etmoneydemo.utils

import java.text.SimpleDateFormat
import java.util.*

object Util {

    fun formatDate(date: Date?, pattern: String?): String? {
        val simpleDateFormat = SimpleDateFormat.getDateInstance() as SimpleDateFormat
        simpleDateFormat.applyPattern(pattern)
        return date?.let { simpleDateFormat.format(it) }
    }

}