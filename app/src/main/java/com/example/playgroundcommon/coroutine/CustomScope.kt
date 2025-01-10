package com.example.playgroundcommon.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MyCustomScope : CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job // Здесь контекст: IO-диспетчер + Job

    fun cancelScope() {
        job.cancel() // Отмена всех корутин
    }
}

fun main() {
    val myScope = MyCustomScope()
    myScope.launch {
        println("Корутина работает")
    }
}