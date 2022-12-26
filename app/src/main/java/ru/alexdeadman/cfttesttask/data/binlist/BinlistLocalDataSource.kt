package ru.alexdeadman.cfttesttask.data.binlist

import ru.alexdeadman.cfttesttask.data.binlist.room.BinEntity

interface BinlistLocalDataSource {
    suspend fun loadHistory(): List<BinEntity>
    suspend fun clearHistory()
    suspend fun saveBin(bin: String)
}