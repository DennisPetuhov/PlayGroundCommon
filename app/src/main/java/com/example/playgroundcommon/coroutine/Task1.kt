package com.example.playgroundcommon.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

// Интерфейс для выполнения асинхронных команд
interface CommandExecutor {
    fun requestAsync(url: String, onResponse: (String) -> Unit)
}

// Интерфейс для выполнения синхронных команд
interface BleDevice {
    fun requestSync(url: String): String
}

// Реализация CommandExecutor с использованием корутин и синхронизации
class CoroutineCommandExecutor(
    private val bleDevice: BleDevice,
    private val myDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val myScope:CoroutineScope = CoroutineScope(myDispatcher + SupervisorJob())
) : CommandExecutor {

    private val myMutex = Mutex()
    var myMap = mutableMapOf<String, Deferred<String>>()

    override fun requestAsync(url: String, onResponse: (String) -> Unit) {
        myScope.launch {
            // Синхронизация доступа к myMap с помощью Mutex
            val deferred = myMutex.withLock {
                // Проверяем, есть ли активный запрос с данным URL
                myMap[url] ?: async {
                    // Выполнение синхронного запроса через BleDevice
                    val result = bleDevice.requestSync(url)
                    result
                }.also {
                    myMap[url] = it
                }
            }

            // Ожидаем результат выполнения запроса
            val response = deferred.await()

            // Удаляем запрос из myMap после завершения
            myMutex.withLock {
                myMap.remove(url)
            }

            // Возвращаем результат на главный поток
            withContext(Dispatchers.Main) {
            onResponse(response)
            }
        }
    }
}

// Пример реализации BleDevice
class BleDeviceImpl : BleDevice {
    override fun requestSync(url: String): String {
        // Эмуляция выполнения синхронного запроса
        Thread.sleep(1000) // Имитация задержки в запросе
        return "Synchronous response from $url"
    }
}

// Основной класс для тестирования
fun main() {
    val bleDevice = BleDeviceImpl()
    val commandExecutor = CoroutineCommandExecutor(bleDevice)

    // Пример вызова requestAsync
    commandExecutor.requestAsync("https://example.com") { response ->
        println("Response received: $response")
    }

    // Даем время на выполнение асинхронного запроса
    Thread.sleep(2000) // В реальном приложении этого не требуется, здесь для демонстрации
}