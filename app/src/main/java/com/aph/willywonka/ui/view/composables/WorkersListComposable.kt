package com.aph.willywonka.ui.view.composables

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aph.willywonka.data.model.WorkerBO
import com.aph.willywonka.ui.materialDesing.AppTheme
import com.aph.willywonka.ui.viewmodel.WorkerListViewModel
import com.aph.willywonka.utils.AphToolbox
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ListWorkers(
    viewModel: WorkerListViewModel,
    clickWorkerAction: (WorkerBO) -> Unit,
    modifier: Modifier = Modifier
) {
    val refreshScope = rememberCoroutineScope()

    fun refresh() = refreshScope.launch {
        viewModel.requestPage()
    }

    val observerWorkerList by remember { viewModel.workersList }
    val isLoading by remember { viewModel.isLoading }

    ListWorkers(
        isLoading = isLoading,
        workersList = observerWorkerList,
        refreshAction = { refresh() },
        clickWorkerAction = { clickWorkerAction(it) },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ListWorkers(
    isLoading: Boolean,
    workersList: List<WorkerBO>,
    refreshAction: () -> Unit,
    clickWorkerAction: (WorkerBO) -> Unit,
    modifier: Modifier = Modifier
) {



    val state = rememberPullRefreshState(
        refreshing =  isLoading,
        onRefresh = { refreshAction() }
    )

    Box(modifier.pullRefresh(state)) {
        LazyColumn(modifier.fillMaxSize()) {
            items(
                items = workersList,
                key = { worker -> worker.id ?: 0 }
            ) { worker ->
                WorkerItemListComposable(worker){clickWorkerAction(it)}
            }
        }
        PullRefreshIndicator(
            isLoading,
            state,
            modifier.align(alignment = Alignment.TopCenter)
        )
    }
}

//#region Previews

@Preview(showBackground = true)
@Composable
private fun ListWorkersPreview() {

    var loading by remember { mutableStateOf(false) }

    val list = remember { mutableStateListOf<WorkerBO>() }

    if (list.isEmpty()) {
        list.addAll(List(10) { index -> AphToolbox.DummyWorker(index + 1) })
    }

    val refreshScope = rememberCoroutineScope()

    fun Add() = refreshScope.launch {
        if(!loading) {
            loading = true
            delay(2000)
            list.add(AphToolbox.DummyWorker(list.size + 1))
            loading = false
        }
    }
    val context = LocalContext.current
    ListWorkers(
        isLoading = loading,
        workersList = list,
        refreshAction = { Add() },
        clickWorkerAction = { Toast.makeText(context, it.first_name+" "+it.last_name, Toast.LENGTH_SHORT).show() },
    )
}

@Preview
@Composable
private fun MainActivity2Preview() {
    var filterVisibility by remember { mutableStateOf(true) }

    val list = remember { mutableStateListOf<WorkerBO>() }
    val progress = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    if (list.isEmpty()) {
        list.addAll(List(10) { index -> AphToolbox.DummyWorker(index + 1) })
    }

    fun ShowHideFilter() = coroutineScope.launch {
        when (progress.value) {
            0f -> {
                filterVisibility = true
                progress.animateTo(1f)
            };
            1f -> {
                filterVisibility = false
                progress.animateTo(0f)
            };
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
                        ShowHideFilter()
                    }
                },
                content = { padding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        color = MaterialTheme.colorScheme.background
                    ){
                        ListWorkersPreview()

                        AnimatedVisibility(
                            visible = filterVisibility,
                            enter = slideInVertically(
                                // Enters by sliding down from offset -fullHeight to 0.
                                initialOffsetY = { fullHeight -> -fullHeight }
                            ),
                            exit = slideOutVertically(
                                // Exits by sliding up from offset 0 to -fullHeight.
                                targetOffsetY = { fullHeight -> -fullHeight }
                            )
                        ) {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shadowElevation = 4.dp
                            ) {
                                WorkerFilterComposable() { _, _ ->
                                    ShowHideFilter()
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}

//#endregion