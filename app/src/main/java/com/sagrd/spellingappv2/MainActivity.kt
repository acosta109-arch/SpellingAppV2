package com.sagrd.spellingappv2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.sagrd.spellingappv2.presentation.navigation.nav_spelling_app
import com.sagrd.spellingappv2.ui.theme.SpellingAppV2Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpellingAppV2Theme {
                val navHost = rememberNavController()
                Surface(
                    modifier = Modifier.systemBarsPadding()
                ) {
                    nav_spelling_app(
                        navHostController = navHost,
                        onLoginSuccess = {}
                    )
                }
            }
        }
    }
}