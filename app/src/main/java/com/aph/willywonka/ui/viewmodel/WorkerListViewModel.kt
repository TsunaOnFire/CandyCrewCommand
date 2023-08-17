package com.aph.willywonka.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aph.willywonka.data.model.WorkerBO
import com.aph.willywonka.domain.GetWorkerListUseCase
import com.aph.willywonka.utils.WorkerListUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class WorkerListViewModel @Inject constructor(
    private val getWorkerListUseCase: GetWorkerListUseCase,
    private val workerListUtils: WorkerListUtils
) : ViewModel() {

    val workersList = MutableLiveData<MutableList<WorkerBO>>()//Filtered List
    val isLoading = MutableLiveData<Boolean>()


    val professionList = MutableLiveData<List<String>>()//Filtered List


    private var completeWorkerList = listOf<WorkerBO>()
    private var isFilterApply = false
    private var profession: String? = null
    private var gender: String? = null


    fun requestPage() {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getWorkerListUseCase(workerListUtils.getWorkersLastRequestedPage() + 1)

            if (!result.isNullOrEmpty()) {
                completeWorkerList = result
                workerListUtils.setWorkersLastRequestedPage(workerListUtils.getWorkersLastRequestedPage() + 1)
                if(isFilterApply) {
                    filterWorkers()
                } else {
                    workersList.postValue(result.toMutableList())
                }
            }
            getWorkerProfessionList()
            isLoading.postValue(false)
        }
    }

    private fun getWorkerProfessionList() {
        val listProfessions = mutableListOf<String>()
        completeWorkerList.forEach {
            if (it.profession != null && !listProfessions.contains(it.profession)) {
                listProfessions.add(it.profession)
            }
        }
        professionList.postValue(listProfessions.toList())
    }

    fun filterWorkers() {
        viewModelScope.launch {
            isLoading.postValue(true)
            var result = mutableListOf<WorkerBO>()

            if (profession == null && gender == null) {
                result = completeWorkerList.toMutableList()
                isFilterApply = false
            } else {
                var filteredList = mutableListOf<WorkerBO>()

                if (profession != null && profession!!.isNotBlank()) {
                    completeWorkerList.forEach {
                        if (it.profession?.lowercase(Locale.getDefault())?.contains(profession!!.lowercase(Locale.getDefault())) == true) {
                            filteredList.add(it)
                        }
                    }
                } else {
                    filteredList = completeWorkerList.toMutableList()
                }

                if (gender != null) {
                    filteredList.forEach {
                        if (it.gender?.lowercase(Locale.getDefault())?.equals(gender!!.lowercase(Locale.getDefault())) == true) {
                            result.add(it)
                        }
                    }
                } else {
                    result = filteredList
                }
                isFilterApply = true
            }
            workersList.postValue(result)
            isLoading.postValue(false)
        }
    }

    fun setProfessionFilter(profession: String?) {this.profession=profession}
    fun setGenderFilter(gender: String?) {this.gender=gender}
    fun getFilterApply() = isFilterApply
}
