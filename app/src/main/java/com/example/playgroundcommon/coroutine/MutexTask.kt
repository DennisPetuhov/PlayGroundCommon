package com.example.playgroundcommon.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class MutexTask {
}

interface CommandExecutor1 {
    fun requestAsync(url: String, onResponse: (String) -> Unit)
}

interface BleDevice1 {
    //    @Synchronized
    fun requestSync(url: String): String
}

class CoroutineCommandExecutor1(val bleDevice: BleDevice, responseFromScreen:String) : CommandExecutor {
    val myScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    val myMutex = Mutex()
    val myMap = mutableMapOf<String, Deferred<String>>()
    override fun requestAsync(url: String, onResponse: (String) -> Unit) {

        myScope.launch {
            myMutex.withLock {
            val resul=    bleDevice.requestSync(url)


            }
        }

    }
}