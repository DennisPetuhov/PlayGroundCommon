package com.example.playgroundcommon
//Function that never returns (e.g., infinite loops, exceptions)
fun loopForever(): Nothing {
    while (true) {
        // infinite loop
    }
}
fun fail(message: String): Nothing {
    throw IllegalArgumentException(message)
}

//Eliminate the need for else in exhaustive when expressions
val value = 42
val result = when (value) {
    1 -> "one"
    2 -> "two"
    else -> fail("Unexpected value") // fail returns Nothing, ensuring exhaustiveness
}
//Since Nothing is a subtype of all types, it can be used to represent the absence of a value in
//a way that is compatible with all other types:

fun process(input: String?) {
    val result: String = input ?: return fail("Input is null")
    println(result)
}
//In Kotlin, Nothing? represents the bottom type for nullable types. It means a value that is either
//null or Nothing, which effectively just means null because Nothing has no instances:

val x: Nothing? = null
