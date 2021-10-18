package com.example.seminarsample.utils

import java.text.SimpleDateFormat
import java.util.*

fun Long.getCurrentDate(): String {
    val date = Date(this)
    return SimpleDateFormat("yyyy-MM-dd").format(date)
}