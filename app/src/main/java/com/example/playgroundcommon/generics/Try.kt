package com.example.playgroundcommon.generics

class Try {
}
//
open class MyAnimal
class MyCat : MyAnimal()
//
//fun feedAnimals(animals: List<Animal>) {
//    // Логика кормления животных
//}
//
//val cats: List<Cat> = listOf(Cat())
//// feedAnimals(cats) // Ошибка компиляции: List<Cat> не является List<Animal>

class Box<out T>(val value: T)

fun takeBox(box: Box<MyAnimal>) {
    // Логика работы с Box<Animal>
}


class Processor<in T> {
    fun process(item: T) {
        // Логика обработки
    }
}

val animalProcessor: Processor<MyAnimal> = Processor()
val catProcessor: Processor<MyCat> = animalProcessor  // Это работает, так как Processor<Animal> может обработать и Cat


fun main() {
    val catBox: Box<MyCat> = Box(MyCat())

    takeBox(catBox)  // Это работает, потому что Box<Cat> можно передать как Box<Animal>
}