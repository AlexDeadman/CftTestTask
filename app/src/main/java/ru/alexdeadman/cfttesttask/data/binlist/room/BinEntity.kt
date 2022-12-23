package ru.alexdeadman.cfttesttask.data.binlist.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.alexdeadman.cfttesttask.data.binlist.room.BinEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class BinEntity(
    @ColumnInfo(name = "bin")
    @PrimaryKey
    val bin: String
) {
    companion object {
        const val TABLE_NAME = "bin_entity_table"
    }
}
