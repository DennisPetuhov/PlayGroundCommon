package com.example.playgroundcommon


fun main() {
    val fn = outer() // fn = inner, так как функция outer возвращает функцию inner
    // вызываем внутреннюю функцию inner
    fn() // 6
    fn() // 7
    fn() // 8
}
fun outer(): ()->Unit{       // внешняя функция
    var n = 5         // некоторая переменная - лексическое окружение функции inner
    fun inner(){     // вложенная функция
        // действия с переменной n
        n++
        println(n)
    }
    return ::inner
}