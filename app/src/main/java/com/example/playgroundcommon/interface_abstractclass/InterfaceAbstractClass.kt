package com.example.playgroundcommon.interface_abstractclass


interface MyInterface {
    public val a: Int
        get() = 10

    fun myFunction()
}

 class MyClass : MyInterface {
    internal val  n =10
    public val c = 10
    public var b = a
    override fun myFunction() {
        println("My function implementation")
    }
}

fun main() {
    val myClass = MyClass()
    println(myClass.a) // Output: 10
    myClass.myFunction() // Output: My function implementation
}  // Output: My function implementation
