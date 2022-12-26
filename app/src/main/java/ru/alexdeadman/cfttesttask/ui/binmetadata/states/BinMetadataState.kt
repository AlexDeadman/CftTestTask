package ru.alexdeadman.cfttesttask.ui.binmetadata.states

import ru.alexdeadman.cfttesttask.data.binlist.retrofit.binmetadata.BinMetadata

sealed class BinMetadataState {
    object Default : BinMetadataState()
    object Loading : BinMetadataState()
    class Loaded (val result: BinMetadata): BinMetadataState()
    class Error (val throwable: Throwable) : BinMetadataState()
}
