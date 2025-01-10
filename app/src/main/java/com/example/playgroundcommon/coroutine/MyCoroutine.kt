@file:OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)

package com.example.playgroundcommon.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

var counter = 0

fun main() = runBlocking {
    val lock = Any()
    val singleThreadContext = newSingleThreadContext("MySingleThread")
    // Запускаем 1000 корутин
    repeat(10000) {
        launch(singleThreadContext) {
            synchronized(lock) {
                counter++
            }
        }
    }

    delay(1000) // Ждем завершения всех корутин
    println("Counter: $counter")
}



//val mutex = Mutex()
//
//fun main() = runBlocking {
//    // Запускаем 1000 корутин
//    repeat(1000) {
//        launch(Dispatchers.Default) {
//            mutex.withLock {
//                counter++
//            }
//        }
//    }
//
//    delay(1000) // Ждем завершения всех корутин
//    println("Counter: $counter")
//}