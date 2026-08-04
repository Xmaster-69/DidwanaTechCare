package com.didwanatechcare.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.didwanatechcare.app.navigation.AppNavigation
import com.didwanatechcare.app.ui.theme.DidwanaTechCareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DidwanaTechCareTheme {
                Surface(modifier = Modifier.fillMaxSize()) { AppNavigation() }
            }
        }
    }
}