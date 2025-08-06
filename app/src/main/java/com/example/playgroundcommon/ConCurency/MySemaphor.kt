package com.example.playgroundcommon.ConCurency

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class SharedResource {
    private val mutex = Mutex() // Our reentrant lock
    private var counter = 0

    // A function that needs exclusive access
    suspend fun increment() {
        mutex.withLock { // Acquires the lock, executes the block, and releases the lock
            // Critical section starts
            val currentValue = counter
            println("${Thread.currentThread().name}: Acquired lock, current value: $currentValue")
            delay(100) // Simulate some work
            counter = currentValue + 1
            println("${Thread.currentThread().name}: Incremented, new value: $counter")
            // Call another function that also needs the lock (demonstrating reentrancy)
            anotherProtectedAction()
            // Critical section ends
        }
        println("${Thread.currentThread().name}: Released lock")
    }

    // Another function that also requires the same lock
    suspend fun anotherProtectedAction() {
        mutex.withLock {
            // This coroutine already owns the mutex, so it can enter this block.
            println("${Thread.currentThread().name}: Inside anotherProtectedAction, counter is $counter")
            delay(50)
            // If this wasn't reentrant, and we tried to lock again, it might deadlock.
        }
    }

    suspend fun getValue(): Int {
        mutex.withLock {
            return counter
        }
    }
}

fun main() = runBlocking {
    val resource = SharedResource()

    val job1 = launch {
        repeat(3) {
            resource.increment()
            delay(50) // Allow other coroutines to try and acquire
        }
    }

    val job2 = launch {
        repeat(3) {
            resource.increment()
            delay(70) // Allow other coroutines to try and acquire
        }
    }

    job1.join()
    job2.join()

    println("Final counter value: ${resource.getValue()}")
}