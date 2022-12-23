package ru.alexdeadman.cfttesttask.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.alexdeadman.cfttesttask.data.binlist.room.BinlistDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideBinlistDatabase(
        @ApplicationContext context: Context
    ): BinlistDatabase =
        Room.databaseBuilder(
            context,
            BinlistDatabase::class.java,
            "binlist_database"
        ).build()
}