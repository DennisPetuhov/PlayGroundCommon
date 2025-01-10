package com.example.playgroundcommon.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class LivelockExample {
    private val mutex1 = Mutex()
    private val mutex2 = Mutex()

    suspend fun method1() {
        while (true) {
            mutex1.withLock {
                if (mutex2.isLocked) {
                    println("Method 1: Detected mutex2 is locked, releasing mutex1 and retrying...")
                    return@withLock
                }
                println("Method 1: Locked mutex1")
                mutex2.withLock {
                    println("Method 1: Locked mutex2")
                }
            }
        }
    }

    suspend fun method2() {
        while (true) {
            mutex2.withLock {
                if (mutex1.isLocked) {
                    println("Method 2: Detected mutex1 is locked, releasing mutex2 and retrying...")
                    return@withLock
                }
                println("Method 2: Locked mutex2")
                mutex1.withLock {
                    println("Method 2: Locked mutex1")
                }
            }
        }
    }
}

fun main() = runBlocking {
    val example = LivelockExample()

    val job1 = launch(Dispatchers.Default) {
        example.method1()
    }

    val job2 = launch(Dispatchers.Default) {
        example.method2()
    }

    delay(1000) // Даем корутинам время для работы в лайвлоке
    job1.cancel()
    job2.cancel()
    println("Finished!")
}