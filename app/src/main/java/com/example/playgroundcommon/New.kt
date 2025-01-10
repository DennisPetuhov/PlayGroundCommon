package com.example.playgroundcommon

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Объявление CompositionLocal
data class User(val name: String, val age: Int)

val LocalUser = compositionLocalOf<User> {
    error("No user provided")
}

@Composable
fun ParentComponent() {
    val user = User("Alice", 30)

    // Предоставление значения User в CompositionLocalProvider
    CompositionLocalProvider(LocalUser provides user) {
        ChildComponent()
    }
}

@Composable
fun ChildComponent() {
    // Потребление значения User из CompositionLocal
    val user = LocalUser.current

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Name: ${user.name}")
        Text("Age: ${user.age}")
    }
}

@Composable
fun AnotherComponent() {
    // Потребление значения User из CompositionLocal
    val user = LocalUser.current

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Welcome, ${user.name}")
        Text("Your age is ${user.age}")
    }
}

@Preview
@Composable
fun PreviewMyApp() {
    ParentComponent()
}