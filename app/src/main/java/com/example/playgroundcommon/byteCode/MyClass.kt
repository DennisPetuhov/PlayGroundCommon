package com.example.playgroundcommon.byteCode

class MyClass {
    constructor( name: String){}
    constructor( name: String, age: Int): this(name) {}
    fun bar() {
        println("Hello from bar")
    }
 }

fun main() {
    val myPerson = Person()
    myPerson.sayHello()
}