package ru.alexdeadman.cfttesttask.data.binlist.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BinlistDao {
    @Query("select * from ${BinEntity.TABLE_NAME}")
    suspend fun loadHistory(): List<BinEntity>

    @Query("delete from ${BinEntity.TABLE_NAME}")
    suspend fun clearHistory()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun saveBin(binEntity: BinEntity)
}