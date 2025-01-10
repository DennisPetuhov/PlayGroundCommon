package com.example.playgroundcommon.reflection

import kotlin.reflect.full.memberProperties
//https://kt.academy/article/ak-reflection
fun displayPropertiesAsList(value: Any) {
    value::class.memberProperties
        .sortedBy { it.name }
        .map { p -> " * ${p.name}: ${p.call(value)}" }
        .forEach(::println)
}

class Person(
    val name: String,
    val surname: String,
    val children: Int,
    val female: Boolean,
)

class Dog(
    val name: String,
    val age: Int,
)

enum class DogBreed {
    HUSKY, LABRADOR, PUG, BORDER_COLLIE
}

fun printABC() {
    println("ABC")
}
fun double(i: Int): Int = i * 2

class Complex(val real: Double, val imaginary: Double) {
    fun plus(number: Number): Complex = Complex(
        real = real + number.toDouble(),
        imaginary = imaginary
    )
}
val a = "a"
fun Complex.double(): Complex =
    Complex(real * 2, imaginary * 2)

fun Complex?.isNullOrZero(): Boolean =
    this == null || (this.real == 0.0 && this.imaginary == 0.0)

class Box<T>(var value: T) {
    fun get(): T = value
}

fun <T> Box<T>.set(value: T) {
    this.value = value
}

fun add(i: Int, j: Int) = i + j

fun main() {
//    val granny = Person("Esmeralda", "Weatherwax", 0, true)
//    displayPropertiesAsList(granny)
    // * children: 0
    // * female: true
    // * name: Esmeralda
    // * surname: Weatherwax

//    val cookie = Dog("Cookie", 1)
//    displayPropertiesAsList(cookie)
    // * age: 1
    // * name: Cookie

//    displayPropertiesAsList(DogBreed.BORDER_COLLIE)
    // * name: BORDER_COLLIE
    // * ordinal: 3
//    val f1: KFunction0<Unit> = ::printABC
//    val f2: KFunction1<Int, Int> = ::double
//    val f3: KFunction2<Complex, Number, Complex> = Complex::plus
//    val f4: KFunction1<Complex, Complex> = Complex::double
//    val f5: KFunction1<Complex?, Boolean> = Complex?::isNullOrZero
//    val f6: KFunction1<Box<Int>, Int> = Box<Int>::get
//    val f7: KFunction2<Box<String>, String, Unit>=Box<String>::set
//    Using function references where a function type is expected is not "real" reflection because,
//    under the hood, Kotlin transforms these references to lambda expressions.
    val f1= ::printABC
    val f2 = ::double
    val f3 = Complex::plus
    val f4 = Complex::double
    val f5 = Complex?::isNullOrZero
    val f6 = Box<Int>::get
    val f7 = Box<String>::set

    val f =::add
    f.invoke(1, 2)
    f.call(1, 2)
}