package ru.alexdeadman.cfttesttask.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.alexdeadman.cfttesttask.data.binlist.BinlistLocalDataSource
import ru.alexdeadman.cfttesttask.data.binlist.BinlistRemoteDataSource
import ru.alexdeadman.cfttesttask.data.binlist.BinlistRepository
import ru.alexdeadman.cfttesttask.data.binlist.retrofit.BinlistApi
import ru.alexdeadman.cfttesttask.data.binlist.retrofit.RetrofitBinlistDataSource
import ru.alexdeadman.cfttesttask.data.binlist.room.BinlistDatabase
import ru.alexdeadman.cfttesttask.data.binlist.room.RoomBinlistDatasource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideBinlistRemoteDataSource(api: BinlistApi): BinlistRemoteDataSource =
        RetrofitBinlistDataSource(api)

    @Provides
    @Singleton
    fun provideLocalBinlistDataSource(database: BinlistDatabase): BinlistLocalDataSource =
        RoomBinlistDatasource(database.binlistDao())

    @Provides
    @Singleton
    fun provideBinlistRepository(
        remote: BinlistRemoteDataSource,
        local: BinlistLocalDataSource
    ): BinlistRepository = BinlistRepository(remote, local)
}