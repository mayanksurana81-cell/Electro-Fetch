package com.example.electrofetch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.*
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.rpc.context.AttributeContext


@Composable
fun AuthScreen(
    onLoginSuccess: () -> Unit
){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val auth = Firebase.auth

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = "Login / SignUp",
            fontSize = 24.sp
        )
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value =  email,
            onValueChange = {email = it},
            label = {Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            label = {Text("Password")},
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        onLoginSuccess()
                    }
                    .addOnFailureListener {
                        println("Login failed: ${it.message}")
                    }
            },
           modifier =  Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                auth.createUserWithEmailAndPassword(email , password)
                    .addOnSuccessListener {
                        onLoginSuccess()
                    }
                    .addOnFailureListener {
                        println("SignUp Failed: ${it.message}")
                    }
            },
           modifier =  Modifier.fillMaxWidth()
        ) {
            Text("SignUp")
        }
    }
}