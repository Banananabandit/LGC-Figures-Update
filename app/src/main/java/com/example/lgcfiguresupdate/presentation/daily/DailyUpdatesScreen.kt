package com.example.lgcfiguresupdate.presentation.daily

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlin.math.roundToInt

@Composable
fun DailyUpdatesScreen() {
    SalesUpdate()
    CustomerNumberUpdate()
}

@Composable
fun SalesUpdate() {
    var text by remember { mutableStateOf("") }
    Text(text = "Sales")
    TextField(
        value = text,
        onValueChange = {text = it})
    Row {
        Text(text = "Result")
        Result(calculateExVAT(text))
    }
}

@Composable
fun CustomerNumberUpdate() {
    TODO("Not yet implemented")
}

@Composable
fun Result(result: String) {
    Text(text = result)
}

fun calculateExVAT(result: String): String {
    if (result.isEmpty()){
        return (result.toDouble() / 1.2).roundToInt().toString()
    }
}
