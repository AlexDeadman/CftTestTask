package ru.alexdeadman.cfttesttask.data.binlist

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.alexdeadman.cfttesttask.data.binlist.retrofit.binmetadata.BinMetadata
import ru.alexdeadman.cfttesttask.data.binlist.room.BinEntity

class BinlistRepository(
    private val binlistRemoteDataSource: BinlistRemoteDataSource,
    private val binlistLocalDataSource: BinlistLocalDataSource
) {
    fun fetchBinMetadata(bin: String): Flow<BinMetadata> = flow {
        binlistLocalDataSource.saveBin(bin)
        emit(binlistRemoteDataSource.getBinMetadata(bin))
    }

    fun fetchHistory(): Flow<List<BinEntity>> = flow {
        emit(binlistLocalDataSource.loadHistory())
    }

    fun clearHistory(): Flow<Unit> = flow {
        emit(binlistLocalDataSource.clearHistory())
    }
}