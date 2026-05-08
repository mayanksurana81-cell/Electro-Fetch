package com.example.electrofetch

import com.google.firebase.auth.FirebaseAuth
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.electrofetch.ui.theme.ElectroFetchTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val auth = Firebase.auth
            var appState by remember {
                mutableStateOf("AUTH")
            }
            val user = auth.currentUser
            if(user == null){
                appState = "AUTH"
            }
            when(appState){
                "AUTH" ->{
                    AuthScreen (
                        onLoginSuccess = {
                            appState = "CHECK_DEVICE"
                        }
                    )
                }
                "CHECK_DEVICE" -> {
                    checkDeviceScreen(
                        onDeviceExists = {
                            appState = "MAIN"
                        },
                        onDeviceNotExists = {
                            appState = "REGISTER_DEVICE"
                        }
                    )
                }
                "REGISTER_DEVICE" -> {
                    RegisterDeviceScreen(
                        onRegisterComplete ={
                            appState = "MAIN"
                        },
                        onCancle = {
                            appState = "MAIN"
                        }
                    )
                }
                "MAIN" ->{
                    DeviceScreen(
                        onLogOut = {
                            Firebase.auth.signOut()
                            appState = "AUTH"
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ElectroFetchTheme {
        Greeting("Android")
    }
}