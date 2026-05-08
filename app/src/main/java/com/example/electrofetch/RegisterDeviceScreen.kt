package com.example.electrofetch

import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore

@Composable
fun RegisterDeviceScreen(
    onRegisterComplete: () -> Unit,
    onCancle: () -> Unit
){
    val context = LocalContext.current
    val user = Firebase.auth.currentUser ?: return
    val deviceId = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ANDROID_ID
    )
    var nickname by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = "Register this Device",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = nickname,
            onValueChange = {nickname = it},
            label = {Text("Device Nickname")},
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if(nickname.isBlank()) return@Button

                isLoading = true

                Firebase.firestore
                    .collection("users")
                    .document(user.uid)
                    .collection("devices")
                    .document(deviceId)
                    .set(
                        mapOf(
                            "name" to nickname,
                            "type" to "mobile",
                            "platform" to "android",
                            "battery" to 0,
                            "lastSync" to System.currentTimeMillis()
                        ),
                        SetOptions.merge()
                    )
                    .addOnSuccessListener {
                        onRegisterComplete()
                    }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
        Spacer(modifier = Modifier.height(24.dp))

        TextButton(
            onClick = {
                onCancle()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancle")
        }

        if(isLoading){
            Spacer(modifier = Modifier.height(24.dp))
            CircularProgressIndicator()
        }
    }
}