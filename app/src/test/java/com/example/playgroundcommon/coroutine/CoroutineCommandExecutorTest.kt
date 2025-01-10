@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.playgroundcommon.coroutine

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

import org.junit.After
import org.junit.Before
import org.junit.Test


class CoroutineCommandExecutorTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
         Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun requestAsync_returnsResponse() = runTest {
        val bleDevice = BleDeviceImpl()
        val commandExecutor = CoroutineCommandExecutor(myDispatcher = testDispatcher, bleDevice = bleDevice)

        var response: String? = null
        commandExecutor.requestAsync("https://example.com") {
            response = it
        }

        advanceUntilIdle()
        assertEquals("Synchronous response from https://example.com", response)
    }

    @Test
    fun requestAsync_removesDeferredAfterCompletion() = runTest {
        val bleDevice = BleDeviceImpl()
        val commandExecutor = CoroutineCommandExecutor(bleDevice)

        commandExecutor.requestAsync("https://example.com") {}
        commandExecutor.requestAsync("https://example.com") {}

        advanceUntilIdle()
        assertEquals(1, commandExecutor.myMap.size)
    }

    @Test
    fun requestAsync_reusesDeferredForSameUrl() = runTest {
        val bleDevice = BleDeviceImpl()
        val commandExecutor = CoroutineCommandExecutor(myDispatcher = testDispatcher, bleDevice = bleDevice)

        var response1: String? = null
        var response2: String? = null
        commandExecutor.requestAsync("https://example.com") {
            response1 = it
        }
        commandExecutor.requestAsync("https://example.com") {
            response2 = it
        }

        advanceUntilIdle()
        assertEquals("Synchronous response from https://example.com", response1)
        assertEquals("Synchronous response from https://example.com", response2)
    }
}