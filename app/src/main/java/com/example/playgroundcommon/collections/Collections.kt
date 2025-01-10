package com.example.playgroundcommon.collections

import java.util.LinkedList

// List
val arrayList = arrayListOf(1, 2, 3)
val linkedList = java.util.LinkedList(listOf(1, 2, 3))
val vector = java.util.Vector(listOf(1, 2, 3))
val stack = java.util.Stack<Int>().apply { push(1); push(2) }

// Queue
val priorityQueue = java.util.PriorityQueue<Int>().apply { add(1); add(2) }
val deque = ArrayDeque<Int>().apply { addFirst(1); addLast(2) }
val arrayDeque = ArrayDeque<Int>().apply { addLast(1); addLast(2) }

// Set
val hashSet = hashSetOf(1, 2, 3)
val linkedHashSet = linkedSetOf(1, 2, 3)
val treeSet = java.util.TreeSet<Int>().apply { add(1); add(2) }

// Map val hashMap = hashMapOf("key1" to 1, "key2" to 2)
// val linkedHashMap = linkedMapOf("key1" to 1, "key2" to 2)
// val treeMap = java.util.TreeMap<String, Int>().apply { put("key1", 1); put("key2", 2) }
val a = listOf(1, 2, 3)
val b = arrayListOf(1, 2, 3)
val c = LinkedList(listOf(1, 2, 3))

fun functorial(n: Int): Int {
    var result = 0
    if (n == 0 || n == 1) return 1
    for (i in 1..n) {
        result = result * n - 1
    }
    return result
}

fun recursionFactorial(n: Int): Int {
    if (n == 0 || n == 1) return 1
    return n * recursionFactorial(n - 1)
}

//1  1 2 3 5 8 13 21 34 55 89 144 233 377 610 987 1597
fun fibonachi(n: Int): Int {
    if (n == 0 || n == 1) return 1
    var result = 0
    var a = 0
    var b = 1

    for (i in 1..n) {
        result = a + b
        a = b
        b = result

    }
    return result
}
fun recursiveFibonachi(n: Int): Int {
    return if (n<=1) n else recursionFactorial(n - 1) + recursionFactorial(n - 2)
}