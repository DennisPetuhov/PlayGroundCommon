package com.example.playgroundcommon.generics

//дженерики лучше работают когда есть разделение коллекция на mutable и immutable

interface Animal
interface Mammal : Animal
interface Predator : Mammal

class Lion : Predator
class Giraffe : Mammal

fun restaurantForPredators(predators: ArrayList<out Predator>) {
    predators.forEach { println("feed $it") }
}

fun main() {
    val lions = ArrayList<Lion>()
    restaurantForPredators(lions) //error because of type mismatch -- we can make like this
//    fun restaurantForPredators(predators: ArrayList<Predator>){
//    pradators.add(Wolf())
//        predators.forEach { println("feed $it") }
//    }


}

interface MutableList<T> {
    fun add(element: T)
} //один тип вариантности  - Т в нутри функции

interface ReadOnlyList<T> {
    fun get(index: Int): T
}//один тип вариантности  - Т снаружи  функции

class MyList<T> : MutableList<T>, ReadOnlyList<T> {
    override fun add(element: T) {
        println("add $element")
    }

    override fun get(index: Int): T {
        return index as T
    }
}


val animals = MyList<Animal>()
val mammals: MyList<Mammal> = MyList()

val predators: MyList<Predator> = MyList()

//covariant типы сонаправлены  PREDATOR -> MAMMAL -> ANIMAL и производный тип от дженерика тоже сонаправлен
// ReadOnlyList<Predator> -> ReadOnlyList<Mammal> -> ReadOnlyList<Animal>
val readablePredators: ReadOnlyList<Predator> = predators
val readableMammals: ReadOnlyList<out Mammal> = predators// обязательство только получать из коллекции
val readableAnimals: ReadOnlyList<out Animal> = predators

//contravariant производный тип от дженеррика в обратную сторону присваиваеться PREDATOR -> MAMMAL -> ANIMAL
// ReadOnlyList<Predator> <- ReadOnlyList<Mammal> <- ReadOnlyList<Animal>
val mutablePredators: MutableList<in Predator> = animals
val mutableMammals: MutableList<in Mammal> = animals// обязательство только передавать в коллекции
val mutableAnimals: MutableList<in Animal> = animals

//ковариантность и контравариантность одновыременно
fun <T> ArrayList<out T>.copyTo(to: ArrayList<in T>) {
    forEach {
        to.add(it)
    }
}


