package ru.alexdeadman.cfttesttask.data.binlist.room

import ru.alexdeadman.cfttesttask.data.binlist.BinlistLocalDataSource

class RoomBinlistDatasource(
    private val binlistDao: BinlistDao
) : BinlistLocalDataSource {
    override suspend fun loadHistory(): List<BinEntity> = binlistDao.loadHistory()

    override suspend fun clearHistory() {
        binlistDao.clearHistory()
    }

    override suspend fun saveBin(bin: String) {
        binlistDao.saveBin(BinEntity(bin))
    }
}