package ru.alexdeadman.cfttesttask.ui.binmetadata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.alexdeadman.cfttesttask.data.binlist.BinlistRepository
import javax.inject.Inject

@HiltViewModel
class BinMetadataViewModel @Inject constructor(
    private val repository: BinlistRepository
) : ViewModel() {
    private val _binMetadataStateFlow = MutableStateFlow<BinMetadataState>(BinMetadataState.Default())
    val binMetadataStateFlow = _binMetadataStateFlow.asStateFlow()

    fun fetchBinMetadata(bin: String) {
        _binMetadataStateFlow.value = BinMetadataState.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchBinMetadata(bin)
//                .catch { }
                .collect {
                    _binMetadataStateFlow.value = BinMetadataState.Loaded(it)
                }
        }
    }
}