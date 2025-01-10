//package com.example.playgroundcommon.gradle
//
//plugins {
//    // Плагин для работы с Kotlin
//    kotlin("jvm") version "1.8.0" // Версия плагина Kotlin
//}
//
//group = "com.example"
//version = "1.0-SNAPSHOT"
//
//repositories {
//    // Используем репозиторий Maven Central для зависимостей
//    mavenCentral()
//}
//
//dependencies {
//    // Зависимость на Kotlin стандартную библиотеку
//    implementation(kotlin("stdlib"))
//
//    // JUnit для тестирования
//    testImplementation("junit:junit:4.13.2")
//
//    // SLF4J для логирования
//    implementation("org.slf4j:slf4j-api:1.7.30")
//    implementation("ch.qos.logback:logback-classic:1.2.3")
//}
//
//tasks.test {
//    // Используем JUnit платформу для тестов
//    useJUnit()
//}
//
//tasks.jar {
//    manifest {
//        // Указываем главный класс для выполнения приложения
//        attributes["Main-Class"] = "com.example.MainKt" // В Kotlin главные классы имеют суффикс Kt
//    }
//}
//
//// Обеспечиваем, чтобы jar содержал все зависимости
//tasks.withType<Jar> {
//    from(sourceSets.main.get().output)
//
//    manifest {
//        attributes["Main-Class"] = "com.example.MainKt"
//    }
//
//    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
//
//    val dependencies = configurations
//        .runtimeClasspath
//        .get()
//        .map { if (it.isDirectory) it else zipTree(it) }
//    from(dependencies)
//}