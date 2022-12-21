package ru.alexdeadman.cfttesttask.data.binlist

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.alexdeadman.cfttesttask.data.binlist.retrofit.binmetadata.BinMetadata

class BinlistRepository(
    private val binlistRemoteDataSource: BinlistRemoteDataSource
) {
    fun fetchBinMetadata(bin: String): Flow<BinMetadata> = flow {
        emit(binlistRemoteDataSource.getBinMetadata(bin))
    }
}