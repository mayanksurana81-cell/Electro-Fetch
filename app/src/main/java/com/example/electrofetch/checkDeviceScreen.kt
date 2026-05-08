package com.example.electrofetch

import android.provider.Settings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import java.nio.file.WatchEvent

@Composable
fun checkDeviceScreen(
    onDeviceExists : () -> Unit,
    onDeviceNotExists :() -> Unit
){
    val context = LocalContext.current
    val user = Firebase.auth.currentUser ?: return
    val deviceId = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ANDROID_ID
    )
    LaunchedEffect(Unit) {
        Firebase.firestore
            .collection("usere")
            .document(user.uid)
            .collection("devices")
            .document(deviceId)
            .get()
            .addOnSuccessListener { document ->
                if(document.exists()){
                    onDeviceExists()
                }else{
                    onDeviceNotExists()
                }
            }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        CircularProgressIndicator()
    }
}