package com.example.playgroundcommon.coroutine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class DeadLockExampleMutex {
    private val mutex1 = Mutex()
    private val mutex2 = Mutex()

    suspend fun method1() {
        mutex1.withLock {
            println("Method 1: Locked mutex1")
            delay(100) // Имитация работы

            mutex2.withLock {
                println("Method 1: Locked mutex2")
            }
        }
    }

    suspend fun method2() {
        mutex2.withLock {
            println("Method 2: Locked mutex2")
            delay(100) // Имитация работы

            mutex1.withLock {
                println("Method 2: Locked mutex1")
            }
        }
    }
}

fun main() = runBlocking {
//    val example = DeadLockExampleMutex()
//
//    val job1 = launch(Dispatchers.Default) {
//        example.method1()
//    }
//
//    val job2 = launch(Dispatchers.Default) {
//        example.method2()
//    }
//
//    joinAll(job1, job2)
}