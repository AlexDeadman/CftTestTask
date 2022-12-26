package ru.alexdeadman.cfttesttask.ui.binmetadata.states

@Suppress("CanSealedSubClassBeObject")
sealed class HistoryState {
    object Default : HistoryState()
    class Loaded (val result: List<String>): HistoryState()
    class Cleared : HistoryState()
    class Error (val throwable: Throwable) : HistoryState()
}
