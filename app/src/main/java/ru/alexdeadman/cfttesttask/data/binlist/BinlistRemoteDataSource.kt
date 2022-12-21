package ru.alexdeadman.cfttesttask.data.binlist

import ru.alexdeadman.cfttesttask.data.binlist.retrofit.binmetadata.BinMetadata

interface BinlistRemoteDataSource {
    suspend fun getBinMetadata(bin: String): BinMetadata
}