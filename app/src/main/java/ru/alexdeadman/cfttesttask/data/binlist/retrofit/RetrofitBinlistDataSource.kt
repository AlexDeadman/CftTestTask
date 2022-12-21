package ru.alexdeadman.cfttesttask.data.binlist.retrofit

import ru.alexdeadman.cfttesttask.data.binlist.BinlistRemoteDataSource
import ru.alexdeadman.cfttesttask.data.binlist.retrofit.binmetadata.BinMetadata

class RetrofitBinlistDataSource(
    private val binlistApi: BinlistApi
) : BinlistRemoteDataSource {
    override suspend fun getBinMetadata(bin: String): BinMetadata = binlistApi.getBinMetadata(bin)
}