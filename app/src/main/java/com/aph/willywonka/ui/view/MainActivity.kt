package com.aph.willywonka.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.aph.willywonka.data.model.WorkerBO
import com.aph.willywonka.ui.materialDesing.AppTheme
import com.aph.willywonka.ui.view.composables.AppBarComposable
import com.aph.willywonka.ui.view.composables.ListWorkers
import com.aph.willywonka.ui.view.composables.WorkerFilterComposable
import com.aph.willywonka.ui.viewmodel.WorkerListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val workerListViewModel : WorkerListViewModel by viewModels()

    private fun onItemSelected(worker: WorkerBO){
        val intent = Intent(this, DetailActivity::class.java)
        val b = Bundle()
        b.putParcelable("worker", worker)
        intent.putExtras(b)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var filterVisibility by remember { mutableStateOf(false) }

            val progress = remember { Animatable(0f) }
            val coroutineScope = rememberCoroutineScope()

            fun showHideFilter() = coroutineScope.launch {
                when (progress.value) {
                    0f -> {
                        filterVisibility = true
                        progress.animateTo(1f)
                    }
                    1f -> {
                        filterVisibility = false
                        progress.animateTo(0f)
                    }
                    else -> {}
                }
            }

            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            AppBarComposable(progress.value,
                                Modifier
                                    .shadow(6.dp)
                                    .background(MaterialTheme.colorScheme.background)
                            ) {
                                showHideFilter()
                            }
                        },
                        content = { padding ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(padding),
                                color = MaterialTheme.colorScheme.background
                            ){
                                ListWorkers(
                                    viewModel = workerListViewModel,
                                    clickWorkerAction = { onItemSelected(it) }
                                )

                                AnimatedVisibility(
                                    visible = filterVisibility,
                                    enter = slideInVertically(initialOffsetY = { fullHeight -> -fullHeight }),
                                    exit = slideOutVertically(targetOffsetY = { fullHeight -> -fullHeight })
                                ) {
                                    Surface(
                                        modifier = Modifier.fillMaxWidth(),
                                        shadowElevation = 4.dp
                                    ) {
                                        WorkerFilterComposable(
                                            Modifier,
                                            workerListViewModel.getGenderFilter(),
                                            workerListViewModel.getProfessionFilter()
                                        ) { inputProfession, selectedGenderKey ->
                                            workerListViewModel.setProfessionFilter(inputProfession)
                                            workerListViewModel.setGenderFilter(selectedGenderKey)
                                            workerListViewModel.filterWorkers()
                                            showHideFilter()
                                        }
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        workerListViewModel.requestPage()
    }
}