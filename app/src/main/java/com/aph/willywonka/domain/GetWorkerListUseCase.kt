package com.aph.willywonka.domain

import android.content.Context
import android.widget.Toast
import com.aph.willywonka.core.InternetConnection
import com.aph.willywonka.data.WorkerRepository
import com.aph.willywonka.data.database.entities.toBDO
import com.aph.willywonka.data.model.WorkerBO
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetWorkerListUseCase @Inject() constructor(
    @ApplicationContext private val context: Context,
    private val repository: WorkerRepository
) {
    suspend operator fun invoke(page: Int?):List<WorkerBO>{
        if(InternetConnection.checkConnection(context)) {//CHECK CONNECTION
            val workersList = repository.getWorkersFromApiByPage(page)
            return if (workersList.isNotEmpty()) {
                repository.insertWorkers(workersList.map { it.toBDO() })
                repository.getAllWorkersFromDatabase()
            } else {
                repository.getAllWorkersFromDatabase()
            }
        } else{
            val characters =repository.getAllWorkersFromDatabase()
            if(characters.isEmpty()){
                Toast.makeText(context,"No Internet & No data to recover\nCheck your internet connection and Swipe Down to refresh",
                    Toast.LENGTH_LONG).show()
            }
            return characters
        }
    }
}