package ru.alexdeadman.cfttesttask.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.alexdeadman.cfttesttask.data.binlist.BinlistRemoteDataSource
import ru.alexdeadman.cfttesttask.data.binlist.BinlistRepository
import ru.alexdeadman.cfttesttask.data.binlist.retrofit.BinlistApi
import ru.alexdeadman.cfttesttask.data.binlist.retrofit.RetrofitBinlistDataSource
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
    fun provideBinlistRepository(
        remote: BinlistRemoteDataSource
    ): BinlistRepository = BinlistRepository(remote)
}