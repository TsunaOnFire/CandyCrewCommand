package com.aph.willywonka.domain

import android.content.Context
import android.widget.Toast
import com.aph.willywonka.core.InternetConnection
import com.aph.willywonka.data.WorkerRepository
import com.aph.willywonka.data.database.entities.toBDO
import com.aph.willywonka.data.model.WorkerBO
import com.aph.willywonka.utils.isEmpty
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetWorkerByIdUseCase @Inject() constructor(
    @ApplicationContext private val context: Context,
    private val repository: WorkerRepository
) {
    suspend operator fun invoke(id: Int):WorkerBO?{
        if(InternetConnection.checkConnection(context)) {//CHECK CONNECTION
            val worker = repository.getWorkerFromApiById(id)
            return if (worker != null) {
                repository.updateWorker(worker)
                return worker
            } else {
                repository.getWorkerFromDatabaseById(id)
            }
        } else{
            val worker = repository.getWorkerFromDatabaseById(id)
            if(worker.isEmpty()){
                Toast.makeText(context,"No Internet & No data to recover\nCheck your internet connection and Swipe Down to refresh",
                    Toast.LENGTH_LONG).show()
            }
            return null
        }
    }
}