package com.example.spellingappv2.ui.speech

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
//
//@HiltViewModel
//class SpeechViewModel @Inject constructor(
//    palabra : String = ""
//): ViewModel(){
//    private  var  textToSpeech : TextToSpeech? = null
//
//    fun textToSpeech(context: Context, palabra : String){
//
//        textToSpeech = TextToSpeech(
//            context
//        ) {
//            if (it == TextToSpeech.SUCCESS) {
//                textToSpeech?.let { txtToSpeech ->
//                    txtToSpeech.language = Locale.US
//                    txtToSpeech.setSpeechRate(3.0f)
//                    txtToSpeech.speak(
//                        palabra,
//                        TextToSpeech.QUEUE_ADD,
//                        null,
//                        null
//                    )
//                }
//            }
//
//        }
//    }
//}
