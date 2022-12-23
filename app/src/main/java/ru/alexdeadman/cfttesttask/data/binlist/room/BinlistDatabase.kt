package ru.alexdeadman.cfttesttask.data.binlist.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [BinEntity::class],
    version = 1,
    exportSchema = true
)
abstract class BinlistDatabase : RoomDatabase() {
    abstract fun binlistDao(): BinlistDao
}