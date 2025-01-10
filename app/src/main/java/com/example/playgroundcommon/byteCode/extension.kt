package com.example.playgroundcommon.byteCode

import java.util.Locale

fun String.toCapitalCase(): String {
    return this.split(" ").joinToString("") { it.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()
        ) else it.toString()
    } }
}