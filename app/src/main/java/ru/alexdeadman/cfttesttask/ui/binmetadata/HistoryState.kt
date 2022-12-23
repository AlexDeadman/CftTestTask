package ru.alexdeadman.cfttesttask.ui.binmetadata

import ru.alexdeadman.cfttesttask.data.binlist.retrofit.binmetadata.BinMetadata

sealed class HistoryState {
    object Default : HistoryState()
    class Loaded (val result: List<String>): HistoryState()
    class Error (val throwable: Throwable) : HistoryState()
}
