package com.aph.willywonka.utils

import android.content.SharedPreferences

interface WorkerListUtils {
    fun getWorkersLastRequestedPage(): Int
    fun setWorkersLastRequestedPage(page: Int)
}

class WorkerListUtilsImpl (private val sharedPreferences: SharedPreferences) : WorkerListUtils {

    companion object {
        private const val LAST_WORKER_REQUESTED_PAGE_KEY = "LAST_WORKER_REQUESTED_PAGE_KEY"
        private const val DEFAULT_VALUE = 0
        const val WORKER_LIST_UTILS_NAME = "WORKER_LIST_UTILS_NAME"
    }

    override fun getWorkersLastRequestedPage(): Int =
        sharedPreferences.get(LAST_WORKER_REQUESTED_PAGE_KEY, DEFAULT_VALUE)


    override fun setWorkersLastRequestedPage(page: Int) {
        sharedPreferences.put(LAST_WORKER_REQUESTED_PAGE_KEY, page)
    }
}