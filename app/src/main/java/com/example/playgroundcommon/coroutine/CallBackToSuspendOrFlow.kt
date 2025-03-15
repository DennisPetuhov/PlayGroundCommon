//package com.example.playgroundcommon.coroutine
//
//import kotlinx.coroutines.channels.awaitClose
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.callbackFlow
//import kotlinx.coroutines.suspendCancellableCoroutine
//import kotlin.coroutines.resumeWithException
//
//class CallBackToSuspendOrFlow {
//}
//
//suspend fun fetchDataAsSuspend(): String = suspendCancellableCoroutine { continuation ->
//    fetchData { result, error ->
//        if (error != null) {
//            continuation.resumeWithException(error) // Если ошибка, завершаем с исключением
//        } else {
//            continuation.resume(result) // Возвращаем результат
//        }
//    }
//
//    // Обработка отмены
//    continuation.invokeOnCancellation {
//        println("Operation was cancelled") // Логирование отмены
//    }
//}
//
//fun fetchData(function: Any) {
//    TODO("Not yet implemented")
//}
//
//
//suspend fun main() {
//    try {
//        val data = fetchDataAsSuspend()
//        println("Result: $data")
//    } catch (e: Throwable) {
//        println("Error: ${e.message}")
//    }
//}
//fun fetchDataAsFlow(): Flow<String> = callbackFlow {
//    fetchData { result, error ->
//        if (error != null) {
//            close(error) // Закрываем Flow с ошибкой
//        } else {
//            trySend(result) // Отправляем результат
//        }
//    }
//
//    // Обработка закрытия
//    awaitClose {
//        println("Flow was closed")
//    }
//}
//
//fun fetchData(callback: (String?, Throwable?) -> Unit) {
//    // Имитируем асинхронную операцию (например, запрос на сервер)
//    Thread {
//        try {
//            Thread.sleep(1000) // Задержка 1 секунда
//            val result = "Data from server" // Успешный результат
//            callback(result, null) // Возвращаем результат через callback
//        } catch (e: Exception) {
//            callback(null, e) // Возвращаем ошибку через callback
//        }
//    }.start()
//}
//
//fun fetchDataAsFlow(): Flow<String> = callbackFlow {
//    fetchData { result, error ->
//        if (error != null) {
//            close(error) // Закрываем Flow с ошибкой
//        } else {
//            trySend(result ?: "Unknown") // Отправляем результат
//        }
//    }
//
//    // Обработка закрытия
//    awaitClose {
//        println("Flow was closed") // Логирование при завершении
//    }
//}