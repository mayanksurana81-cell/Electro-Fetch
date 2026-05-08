package com.example.electrofetch

import com.google.firebase.Timestamp
import java.util.Locale
import java.text.SimpleDateFormat
import java.util.Date
fun formatTime(timestamp: Long): String{
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
fun isDeviceOnline(lastSync: Long): Boolean{
    val currentTime = System.currentTimeMillis()
    return (currentTime - lastSync) <= 30000
}