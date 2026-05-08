package com.example.electrofetch

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager

fun getBatteryPercentage(context: Context) : Int{
    val intent = context.registerReceiver(
        null,
        IntentFilter(Intent.ACTION_BATTERY_CHANGED)
    )
    val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL , -1) ?: -1
    val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE , -1) ?: -1
    return level*100/scale

}