package ru.alexdeadman.cfttesttask.di.modules

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.alexdeadman.cfttesttask.data.binlist.retrofit.BinlistApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {
    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient = OkHttpClient()

    @Provides
    @Singleton
    fun provideRetrofit(
        gson: Gson,
        client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl("https://lookup.binlist.net/")
        .client(client)
        .build()

    @Provides
    @Singleton
    fun provideBinlistApi(retrofit: Retrofit): BinlistApi = retrofit.create(BinlistApi::class.java)
}