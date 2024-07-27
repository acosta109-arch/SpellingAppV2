package com.sagrd.spellingappv2

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sagrd.spellingappv2.ui.dashboard.MainKidsScreen
import com.sagrd.spellingappv2.ui.main_screen.MainScreen
import com.sagrd.spellingappv2.ui.palabra.WordQuery
import com.sagrd.spellingappv2.ui.palabra.WordRegister
import com.sagrd.spellingappv2.ui.resumen.ResumenScreen
import com.sagrd.spellingappv2.ui.score.ScoreScreen
import com.sagrd.spellingappv2.ui.splash_screen.SplashScreen
import com.sagrd.spellingappv2.ui.usuario.RegistroUsuarioScreen
import com.sagrd.spellingappv2.ui.practica.PracticaScreen
import com.sagrd.spellingappv2.ui.theme.SpellingAppV2Theme
import com.sagrd.spellingappv2.util.Screen
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val textToSpeechEngine: TextToSpeech by lazy {
        TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeechEngine.language = Locale.US
                textToSpeechEngine.setSpeechRate(0.50f)
            }
        }
    }

    lateinit var service: CounterNotificationService


    private fun speak(text: String) {
        val textoEspaceado = text.subSequence(0, text.length)
            .chunked(1) // every 1 chars
            .joinToString(" ") // merge them applying space

        textToSpeechEngine.speak(textoEspaceado, TextToSpeech.QUEUE_ADD, null, null)

        textToSpeechEngine.speak(text, TextToSpeech.QUEUE_ADD, null, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        service = CounterNotificationService(applicationContext)
        setContent {
            SpellingAppV2Theme {
                // A surface container using the 'background' color from the theme service.showNotification(Counter.value)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navHostController = rememberNavController()
                    NavHost(
                        navController = navHostController,
                        startDestination = Screen.SplashScreen.route
                    ) {
                        composable(Screen.SplashScreen.route) {
                            SplashScreen(navHostController)
                        }
                        composable(Screen.MainScreen.route) {
                            MainScreen(navHostController)
                        }
                        composable(Screen.RegistroUsuarioScreen.route) {
                            RegistroUsuarioScreen(navHostController)
                        }
                        composable(Screen.WordQuery.route) {
                            WordQuery(navHostController)
                        }
                        composable(Screen.WordRegister.route) {
                            WordRegister(navHostController)
                        }
                        composable(Screen.ScoreScreen.route) {
                            ScoreScreen(navHostController)
                        }
                        composable(Screen.MainKidsScreen.route + "/{userId}") { navBackStack ->
                            val user = navBackStack.arguments?.getString("userId")
                            MainKidsScreen(navHostController, usuarioId = user?.toInt())
                        }
                        composable(Screen.PracticaScreen.route + "/{palabraId}") { navBackStack ->
                            val palabra = navBackStack.arguments?.getString("palabraId")
                            PracticaScreen(
                                onSpeech = { speak(it) },
                                navHostController = navHostController, palabraId = palabra?.toInt()
                            )
                        }
                        composable(Screen.ResumenScreen.route) {
                            ResumenScreen(navHostController)
                        }
                    }
                }
            }
        }
    }
}
