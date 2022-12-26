package ru.alexdeadman.cfttesttask.ui.binmetadata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.alexdeadman.cfttesttask.data.binlist.BinlistRepository
import ru.alexdeadman.cfttesttask.ui.binmetadata.states.BinMetadataState
import ru.alexdeadman.cfttesttask.ui.binmetadata.states.HistoryState
import javax.inject.Inject

@HiltViewModel
class BinMetadataViewModel @Inject constructor(
    private val repository: BinlistRepository,
) : ViewModel() {

    private val _binMetadataStateFlow = MutableStateFlow<BinMetadataState>(BinMetadataState.Default)
    val binMetadataStateFlow = _binMetadataStateFlow.asStateFlow()

    private val _historyStateFlow = MutableStateFlow<HistoryState>(HistoryState.Default)
    val historyStateFlow = _historyStateFlow.asStateFlow()

    init {
        fetchHistory()
    }

    fun fetchBinMetadata(bin: String) {
        _binMetadataStateFlow.value = BinMetadataState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchBinMetadata(bin)
                .catch {
                    _binMetadataStateFlow.value = BinMetadataState.Error(it)
                }
                .collect {
                    _binMetadataStateFlow.value = BinMetadataState.Loaded(it)
                }
            fetchHistory()
        }
    }

    private fun fetchHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchHistory()
                .catch {
                    _historyStateFlow.value = HistoryState.Error(it)
                }
                .collect {
                    _historyStateFlow.value = HistoryState.Loaded(
                        it.map { entity -> entity.bin }.reversed()
                    )
                }
        }
    }

    fun clearHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearHistory()
                .catch {
                    _historyStateFlow.value = HistoryState.Error(it)
                }
                .collect {
                    _historyStateFlow.value = HistoryState.Cleared()
                }
            fetchHistory()
        }
    }
}