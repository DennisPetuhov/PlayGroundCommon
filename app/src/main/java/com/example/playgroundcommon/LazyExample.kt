package com.example.playgroundcommon

class LazyExample {
    // Ленивое свойство, которое инициализируется при первом доступе к нему
    val lazyValue: String by lazy {
        println("Инициализация lazyValue")
        "Hello, Lazy!"
    }

    // Еще одно ленивое свойство
    val anotherLazyValue: Int by lazy {
        println("Инициализация anotherLazyValue")
        42
    }
}

fun main() {
    // Создание объекта класса LazyExample
    val example = LazyExample()

//    println("До первого обращения к lazyValue")
//    println(example.lazyValue) // Инициализация происходит здесь
//    println("После первого обращения к lazyValue")
//    println(example.lazyValue) // Повторное использование уже инициализированного значения
//
//    println("До первого обращения к anotherLazyValue")
//    println(example.anotherLazyValue) // Инициализация происходит здесь
//    println("После первого обращения к anotherLazyValue")
//    println(example.anotherLazyValue) // Повторное использование уже инициализированного значения

    val cat: Cat by lazy {
        println("initialising Cat")
        Cat("Мурзик")
    }
    println(cat.name)

}

class Cat(val name: String) {
    init {
        println("Создан кот по имени $name")
    }

    val myInt = 5
}