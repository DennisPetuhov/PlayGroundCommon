package com.example.playgroundcommon

open class Parent(val primarilyConstructorValue: Unit = println("Primary constructor of Parent")) {
    // Instance fields
    val instanceFieldParent: Int

    // Lazy initialization
    val lazyFieldParent: Int by lazy {
        println("Lazy initialization of Parent's field")
        30
    }

    // Companion object (static fields and methods)
    companion object {


        init {
            println("Static block of Parent")
        }
        val staticFieldParent: Int = initializeStaticFieldParent()

        private fun initializeStaticFieldParent(): Int {
            println("Initializing static field of Parent")
            return 10
        }
    }

    // Init block
    init {
        instanceFieldParent = 20
        println("Init block of Parent: instanceFieldParent = $instanceFieldParent")
    }

//    // Constructor
//    constructor() {
//        println("Constructor of Parent")
//    }
}

class Child// Constructor
    () : Parent() {
    // Instance fields
    val instanceFieldChild: Int

    // Lazy initialization
    val lazyFieldChild: Int by lazy {
        println("Lazy initialization of Child's field")
        40
    }

    // Companion object (static fields and methods)
    companion object {
        val staticFieldChild: Int = initializeStaticFieldChild()

        init {
            println("Static block of Child")
        }

        private fun initializeStaticFieldChild(): Int {
            println("Initializing static field of Child")
            return 15
        }
    }

    // Init block
    init {
        instanceFieldChild = 25
        println("Init block of Child: instanceFieldChild = $instanceFieldChild")
    }

    init {
        println("Constructor of Child")
    }
}

fun main() {
    println("Creating Child object...")
    val child = Child()

    println("Accessing lazy fields...")
    println("lazyFieldParent: ${child.lazyFieldParent}")
    println("lazyFieldChild: ${child.lazyFieldChild}")
}