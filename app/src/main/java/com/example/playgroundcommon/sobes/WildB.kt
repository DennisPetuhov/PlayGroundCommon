package com.example.playgroundcommon.sobes


fun main() {
    val label = "\$Pb;1;PET(507;11)FrA"
//    parseLabel(label)

        val regex = """(${'$'}Pb);(\d)""".toRegex()
    val regex2 = "(^\\\$Pb)".toRegex()
    val input = "\$Pb"

    val secondMatch = regex2.find(input)!!

    val (prefix ) = secondMatch.destructured
    println(prefix)
}


//написать тесты, сравнивающие полученные структуры для следущих наклеек
//$Pb;1;PET(507;11)FrA
//$Pb;1;PET
//$Pb;1;PET(;)OBa


fun parseLabel(label: String):MyLabel? {
    if (!isValidLabel(label)) return null else{
        val parts = label.split(Regex("[;()]"))

        return returnMyObject(parts)
    }



}

fun isValidLabel(label: String): Boolean {
    val regex = Regex("^\\\$Pb;1;PET(\\(\\d*;\\d*\\))?[A-Za-z]{0,3}$")
    val a = regex.matchEntire(label)
    return regex.matches(label)
}

fun returnMyObject(label: List<String>): MyLabel {
    return if (label.size == 3) {
        MyLabel(
            prefix = label[0],
            version = label[1],
            type = label[2],
            office = null,
            block = null,
            crc = null,
            dependsOnVersion = calculateDependsOnVersion(label[1], label[2])
        )
    } else {
        MyLabel(
            prefix = label[0],
            version = label[1],
            type = label[2],
            office = label[3],
            block = label[4],
            crc = label[5],
            dependsOnVersion = calculateDependsOnVersion(label[1], label[2])
        )
    }
}
fun calculateDependsOnVersion(version: String, type: String): Int {
    val newVersion = version.toInt()
    return when (newVersion) {
        1 -> {
            type.sumOf { it.code }
        }

        2 -> {
            type.fold(1) { acc, c -> acc * c.code }

        }

        else -> {
            0
        }
    }
}


data class MyLabel(
    val prefix: String = "",
    val version: String = "",
    val type: String = "",
    val office: String? = null,
    val block: String? = null,
    val crc: String? = null,
    val dependsOnVersion: Int,
)