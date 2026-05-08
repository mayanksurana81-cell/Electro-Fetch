package com.example.electrofetch

data class Device(
    val name: String = "",
    val battery: Int = 0,
    val lastSync: Long = 0
)