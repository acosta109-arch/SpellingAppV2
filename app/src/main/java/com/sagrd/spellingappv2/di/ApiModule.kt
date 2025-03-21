package com.sagrd.spellingappv2.di

import com.sagrd.spellingappv2.data.remote.palabras.PalabrasManagerApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object ApiModule {
    private const val BASE_URL_PALABRAS = "https://spellingappv2.azurewebsites.net/"

    @Provides
    @Singleton
    fun providesMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun providesArticuloManagerApi(moshi: Moshi): PalabrasManagerApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_PALABRAS)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(PalabrasManagerApi::class.java)
    }
}