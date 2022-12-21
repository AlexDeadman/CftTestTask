package ru.alexdeadman.cfttesttask.data.binlist.retrofit

import retrofit2.http.GET
import retrofit2.http.Path
import ru.alexdeadman.cfttesttask.data.binlist.retrofit.binmetadata.BinMetadata

interface BinlistApi {
    @GET("/{bin}")
    suspend fun getBinMetadata(@Path("bin") bin: String): BinMetadata
}