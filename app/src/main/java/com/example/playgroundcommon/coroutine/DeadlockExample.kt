package com.example.playgroundcommon.coroutine

import kotlinx.coroutines.*

class DeadlockExample {
    private val lock1 = Any()
    private val lock2 = Any()

    @Synchronized
    fun method1() {
        synchronized(lock1) {
            println("Method 1")
        }
    }

    @Synchronized
    fun method2() {
        synchronized(lock2) {
            println("Method 2")
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
fun main() = runBlocking {
    val example = DeadlockExample()
    val firstThreadContext = newSingleThreadContext("MyFirstThread")
    val secondThreadContext = newSingleThreadContext("MySecondThread")

    val job1 = launch(firstThreadContext) {
        example.method1()
    }

    val job2 = launch(secondThreadContext) {
        example.method2()
    }

    joinAll(job1, job2)
}