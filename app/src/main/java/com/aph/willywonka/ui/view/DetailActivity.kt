package com.aph.willywonka.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.aph.willywonka.data.model.WorkerBO
import com.aph.willywonka.ui.materialDesing.AppTheme
import com.aph.willywonka.ui.view.composables.WorkerDetailsComposable
import com.aph.willywonka.ui.viewmodel.WorkerViewModel
import com.aph.willywonka.utils.isFullDownload
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : ComponentActivity() {

    private val workerViewModel : WorkerViewModel by viewModels()
    private lateinit var worker: WorkerBO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val worker = intent.extras?.getParcelable<WorkerBO>("worker")
        if (worker == null) {
            finish()
        } else {
            this.worker= worker
            workerViewModel.worker.value = worker
            if(!this.worker.isFullDownload()){
                this.worker.id?.let{ workerViewModel.requestWorker(it) }
            }
        }

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WorkerDetailsComposable(
                        viewModel = workerViewModel
                    ) {
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun WorkerDetailPreview() {
        AppTheme {
            WorkerDetailsComposable(
                viewModel = workerViewModel
            ) {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }
}