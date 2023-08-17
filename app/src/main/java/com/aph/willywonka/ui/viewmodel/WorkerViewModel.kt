package com.aph.willywonka.ui.viewmodel

import androidx.lifecycle.MutableLiveData
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
    val worker = MutableLiveData<WorkerBO?>()//Filtered List
    val isLoading = MutableLiveData<Boolean>()

    fun requestWorker(id: Int) {

        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getWorkerByIdUseCase(id)

            if(result != null && result.isFullDownload()){
                worker.postValue(result)
            }
            isLoading.postValue(false)
        }
    }

}
