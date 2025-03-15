@file:OptIn(DelicateCoroutinesApi::class)

package com.example.playgroundcommon.flow.customFlow

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

//count last three elements
fun Flow<Double>.avarageOfLastThree(): Flow<Double> {
    return flow {
        val numbers = mutableListOf<Double>()

        collect {
            if (numbers.size >= 3) {
                numbers.removeAt(0)
            }
            numbers.add(it)
        }
        emit(numbers.average())
    }
}

fun main() {
    runBlocking {
        flowOf(1, 2, 3, 4, 5)
            .map { it.toDouble() }
            .avarageOfLastThree()
            .collect {
                println(it)
                println("****")
            }
    }
}