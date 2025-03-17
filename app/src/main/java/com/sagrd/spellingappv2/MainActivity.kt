package com.sagrd.spellingappv2

import com.sagrd.spellingappv2.ui.theme.SpellingAppV2Theme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.sagrd.spellingappv2.data.local.database.SpellingAppDb
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent{
            SpellingAppV2Theme{
                val navHost = rememberNavController()
                val spellingAppDb = Room.databaseBuilder(
                    applicationContext,
                    SpellingAppDb::class.java,
                    "SpellingAppDb"
                ).build()


            }
        }
    }
}
