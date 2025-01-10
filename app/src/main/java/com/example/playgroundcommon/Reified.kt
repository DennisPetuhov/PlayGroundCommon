package com.example.playgroundcommon

inline fun <reified T> findFirst(items: List<Any>): T? {
    for (item in items) {
        if (item is T) {
            return item
        }
    }
    return null
}
inline fun <reified B> checkType(value: Any): Boolean { return value is B}

fun main() {
    val myList = listOf("a", 1, "b", 2, "c", 3)

    println(findFirst<Int>(myList))

}