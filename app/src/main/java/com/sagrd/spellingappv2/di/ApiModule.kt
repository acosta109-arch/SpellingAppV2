package com.sagrd.spellingappv2.di

import android.content.Context
import android.speech.tts.TextToSpeech
import com.sagrd.spellingappv2.data.remote.palabras.PalabrasManagerApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import android.util.Log
import com.sagrd.spellingappv2.data.remote.pines.PinesManagerApi

@InstallIn(SingletonComponent::class)
@Module
object ApiModule {
    private const val BASE_URL_APP = "https://spellingappv2.azurewebsites.net/"

    @Provides
    @Singleton
    fun providesMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun providesPalabraManagerApi(moshi: Moshi): PalabrasManagerApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_APP)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(PalabrasManagerApi::class.java)
    }

    @Provides
    @Singleton
    fun providesPinesManagerApi(moshi: Moshi): PinesManagerApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_APP)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(PinesManagerApi::class.java)
    }



    @Provides
    @Singleton
    fun provideTextToSpeech(@ApplicationContext context: Context): TextToSpeech {
        return TextToSpeech(context) { status ->
            if (status != TextToSpeech.SUCCESS) {
                Log.e("TextToSpeech", "Initialization failed with status: $status")
            }
        }
    }
}