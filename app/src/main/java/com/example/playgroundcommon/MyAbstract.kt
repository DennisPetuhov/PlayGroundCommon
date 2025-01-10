package com.example.playgroundcommon

abstract class MyAbstract : MyInterface {

    fun foo() {
        println("foo")
    }

    override fun myFunction2() {
        println("Abstract myFunction2")
    }

    abstract fun bar()
}


class MyRealClass() : MyAbstract() {
    override fun bar() {
        TODO("Not yet implemented")
    }

    override fun myFunction() {
        TODO("Not yet implemented")
    }

}


interface MyInterface {
    fun myFunction()
    fun myFunction2() {
        println("myFunction2")
    }

    fun myFunction3() {

    }
}
