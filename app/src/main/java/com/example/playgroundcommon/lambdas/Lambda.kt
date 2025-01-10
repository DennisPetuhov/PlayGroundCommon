package com.example.playgroundcommon.lambdas

class Lambda {
}

fun buildString(action: StringBuilder.() -> Unit): String {
    val stringBuilder = StringBuilder()
    stringBuilder.action()
    return stringBuilder.toString()
}

val result = buildString {
    append("Hello, ")
    append("world!")
}
val a = StringBuilder().apply {
    append("Hello, ")
    append("world!")
}.toString()
val b =  StringBuilder().let {

}
