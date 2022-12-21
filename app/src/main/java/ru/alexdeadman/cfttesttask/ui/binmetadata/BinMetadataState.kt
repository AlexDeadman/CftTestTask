package ru.alexdeadman.cfttesttask.ui.binmetadata

import ru.alexdeadman.cfttesttask.data.binlist.retrofit.binmetadata.BinMetadata

sealed class BinMetadataState {
    class Default: BinMetadataState()
    class Loading: BinMetadataState()
    class Loaded (val result: BinMetadata): BinMetadataState()
    class Error: BinMetadataState()
}
