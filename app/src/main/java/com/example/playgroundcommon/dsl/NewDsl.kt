package com.example.playgroundcommon.dsl

fun buildTypes(block: BuildTypes.() -> Unit): BuildTypes {   return BuildTypes().apply(block)}

class BuildTypes {
    private val types = mutableListOf<BuildType>()

    fun release(block: BuildType.() -> Unit) {
        types.add(BuildType("release").apply(block))
    }

    override fun toString(): String {
        return types.joinToString("\n") { it.toString() }
    }
}

class BuildType(private val name: String) {
    var minifyEnabled: Boolean = false
    private val proguardFiles = mutableListOf<String>()

    fun proguardFiles(vararg files: String) {
        proguardFiles.addAll(files)
    }

    fun getDefaultProguardFile(name: String): String {
        return "DefaultProguardFile($name)"
    }

    override fun toString(): String {
        return "$name:\n  minifyEnabled = $minifyEnabled\n  proguardFiles = ${proguardFiles.joinToString(", ")}"
    }
}
fun main() {
    val config = buildTypes {
        release {
            minifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    println(config)
}