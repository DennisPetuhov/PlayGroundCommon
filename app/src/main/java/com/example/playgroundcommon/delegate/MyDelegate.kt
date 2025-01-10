package com.example.playgroundcommon.delegate

import kotlin.properties.Delegates
import kotlin.reflect.KProperty

val lazyValue: String by lazy {
    println("Computed!")
    "Hello, Kotlin"
}

//fun main() {
//    println(lazyValue)  // Вывод: Computed! Hello, Kotlin
//    println(lazyValue)  // Вывод: Hello, Kotlin (без повторного вычисления)
//}

var observedValue: String by Delegates.observable("Initial Value") { property, oldValue, newValue ->
    println("Value changed from $oldValue to $newValue")
}

//fun main() {
//    observedValue = "New Value"  // Вывод: Value changed from Initial Value to New Value
//}

class CustomDelegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return "Value from CustomDelegate"
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        println("Setting new value: $value")
    }
}

class MyClass {
    var customProperty: String by CustomDelegate()
}

//fun main() {
//    val myClass = MyClass()
//    println(myClass.customProperty)  // Вывод: Value from CustomDelegate
//    myClass.customProperty = "New Value"  // Вывод: Setting new value: New Value
//}