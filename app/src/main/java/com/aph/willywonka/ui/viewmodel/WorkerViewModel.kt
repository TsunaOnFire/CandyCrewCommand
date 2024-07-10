package com.aph.willywonka.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aph.willywonka.data.model.WorkerBO
import com.aph.willywonka.domain.GetWorkerByIdUseCase
import com.aph.willywonka.utils.isFullDownload
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkerViewModel @Inject constructor(
    private val getWorkerByIdUseCase: GetWorkerByIdUseCase,
): ViewModel() {
    val worker : MutableState<WorkerBO?> = mutableStateOf(null)
    val isLoading : MutableState<Boolean> = mutableStateOf(false)

    fun requestWorker(id: Int) {

        viewModelScope.launch {
            isLoading.value = true
            val result = getWorkerByIdUseCase(id)

            if(result != null && result.isFullDownload()){
                worker.value = result
            }
            isLoading.value = false
        }
    }

}
