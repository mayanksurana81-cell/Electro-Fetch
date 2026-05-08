package com.example.electrofetch

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceScreen(
    onLogOut: () -> Unit
){
    val user = Firebase.auth.currentUser
    if(user == null) return
    val userId = user.uid
    var devices by remember { mutableStateOf(listOf<Device>()) }
    val context = LocalContext.current
    val deviceId = android.provider.Settings.Secure.getString(
        context.contentResolver,
        android.provider.Settings.Secure.ANDROID_ID
    )
    var isDeviceRegistered by remember { mutableStateOf(false) }



    LaunchedEffect(true) {
        val db = Firebase.firestore
        db.collection("users")
            .document(userId)
            .collection("devices")
            .addSnapshotListener { snapshot, error ->
                if(error!=null) return@addSnapshotListener
                if(snapshot != null){
                    devices = snapshot.documents.mapNotNull{
                        it.toObject(Device::class.java)
                    }
                }
            }
    }

        LaunchedEffect(Unit) {
            val db = Firebase.firestore

            db.collection("users")
                .document(userId)
                .collection("devices")
                .document(deviceId)
                .get()
                .addOnSuccessListener { document ->
                    isDeviceRegistered = document.exists()
                }
        }

    LaunchedEffect(isDeviceRegistered) {
        if (isDeviceRegistered){
            val db = Firebase.firestore
            while (true){
                val battery = getBatteryPercentage(context)
                db.collection("users")
                    .document(userId)
                    .collection("devices")
                    .document(deviceId)
                    .update(
                        mapOf(
                            "battery" to battery,
                            "lastSync" to System.currentTimeMillis()
                        )
                    )
                delay(15000)
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Electrofetch")},
                actions = {
                    Text(
                        text = "Logout",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable{
                                Firebase.auth.signOut()
                                onLogOut()
                            }
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }){
                Icon(Icons.Default.Refresh , contentDescription = "Refresh")
            }
        }
    ) {
        padding ->
        Column(modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .padding(16.dp),
        ) {
            devices.forEach { device ->
                DeviceCard(
                    devicename = device.name,
                    battery = device.battery,
                    lastsync = device.lastSync
                )
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}
@Composable
fun DeviceCard(
    devicename: String,
    battery: Int,
    lastsync: Long
){
    Card (
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp)
    ){
        Column (
            modifier = Modifier.padding(20.dp)
        ){
            Text(devicename, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            val batteryColor = when{
                battery>=50 -> MaterialTheme.colorScheme.primary
                battery in 20..49 -> MaterialTheme.colorScheme.tertiary
                else -> MaterialTheme.colorScheme.error
            }
            Text(
                text = "Battery: $battery%",
                color = batteryColor
            )
            Spacer(Modifier.height(6.dp))

            val online = isDeviceOnline(lastsync)
            Text(
                text = if(online)"Status: Online" else "Status: Offline"
            )
            Spacer(Modifier.height(6.dp))
            Text("Last sync: ${formatTime(lastsync)}")
        }
    }
}