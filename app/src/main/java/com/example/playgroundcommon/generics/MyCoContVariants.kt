package com.example.playgroundcommon.generics

// Базовый класс Message
open class FirstMessage(val text: String)

// Подкласс EmailMessage
open class SecondMessage(text: String) : FirstMessage(text)

// Подкласс SmsMessage
class ThirdMessage(text: String) : SecondMessage(text)

interface ProducerOUT<out T> {
    fun produce(): T
}

class ThirdOut : ProducerOUT<ThirdMessage> {
    override fun produce(): ThirdMessage {
        return ThirdMessage("This is THIRD")
    }
}

interface ConsumerIN<in T> {
    fun consume(item: T)
}

class FirstConsumer : ConsumerIN<FirstMessage> {
    override fun consume(item: FirstMessage) {
        println("Consuming message: ${item.text}")
    }
}


fun main() {
    val thirdOut: ProducerOUT<ThirdMessage> = ThirdOut()
    val firstMessageProducerOUT: ProducerOUT<FirstMessage> = thirdOut
    val message = firstMessageProducerOUT.produce()
    println("Produced message: ${message.text}")


    val firstFirstConsumer: ConsumerIN<FirstMessage> = FirstConsumer()
    val emailConsumer: ConsumerIN<ThirdMessage> = firstFirstConsumer
    emailConsumer.consume(ThirdMessage("Hello from Third"))

}

// Интерфейс Messenger, который принимает тип T, ограниченный классом Message
interface Messenger<T : FirstMessage> {
    fun sendMessage(message: T)
}


// Реализация Messenger для EmailMessage
class EmailMessenger : Messenger<SecondMessage> {
    override fun sendMessage(message: SecondMessage) {
        println("Sending email: ${message.text}")
    }
}

// Реализация Messenger для SmsMessage
class SmsMessenger : Messenger<ThirdMessage> {
    override fun sendMessage(message: ThirdMessage) {
        println("Sending SMS: ${message.text}")
    }
}