package com.example.lgcfiguresupdate.presentation.daily

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

var salesExVAT = ""
var updateMessage = ""

@Composable
fun DailyUpdatesScreen() {
    Column {
        SalesUpdate()
        CustomerNumberUpdate()
        RadioSelector()
        Row {
            SubmitButton()
            ResetButton()
        }
    }
}

@Composable
fun ResetButton() {
    Button(onClick = { /*TODO*/ }) {
        Image(imageVector = Icons.Default.Refresh, contentDescription = "Refresh icon")
    }
}

@Composable
fun SubmitButton() {
    val context = LocalContext.current
    Button(onClick = {
        whatsAppResultShare(context)
    }) {
        Text(text = "Share")
    }
}


@Composable
fun SalesUpdate() {
    var text by rememberSaveable { mutableStateOf("") }
    var isError by rememberSaveable { mutableStateOf(false) }
    val errorMessage = "Too many numbers!"
    val charLimit = 6

    fun validate(text: String) {
        isError = text.length > charLimit
    }

    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            validate(text) },
        singleLine = true,
        label = { Text(if (isError) "Sales*" else "Sales")},
        supportingText = {
                         Text(
                             modifier = Modifier.fillMaxWidth(),
                             text = "Limit: ${text.length}/$charLimit",
                             textAlign = TextAlign.End)
        },
        isError = isError,
        keyboardActions = KeyboardActions{validate(text)},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.semantics {
            if (isError) error(errorMessage)
        }.fillMaxWidth()
    )
    Row {
        Text(text = "Result")
        Result(calculateExVAT(text))
    }
}

@Composable
fun CustomerNumberUpdate() {
    var text by remember { mutableStateOf("") }
    Text(text = "Number of Customers")
    OutlinedTextField(
        value = text,
        onValueChange = {text = it})
    Row {
        Text(text = "ATV")
        Result(calculateATV(text))
    }
}

@Composable
fun RadioSelector() {
    val options = listOf(
        "Update",
        "Final",
    )
    var selectedOption by remember {
        mutableStateOf("")
    }
    val onSelectionChange = { text: String ->
        selectedOption = text
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        options.forEach { text ->
            Row(
                modifier = Modifier
                    .padding(
                        all = 8.dp,
                    )
                    .weight(0.5f)
            ) {
                Text(
                    text = text,
                    style = typography.bodyMedium.merge(),
                    color = Color.White,
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(
                                size = 6.dp,
                            )

                        )
                        .clickable {
                            onSelectionChange(text)
                        }
                        .background(
                            if (text == selectedOption) {
                                Color.Magenta
                            } else {
                                Color.LightGray
                            }
                        )
                        .padding(
                            vertical = 12.dp,
                            horizontal = 16.dp,
                        )
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun Result(result: String) {
    Text(text = result)
}

fun calculateExVAT(result: String): String {
    var result1 = result
    if (result.isNotEmpty()){
        salesExVAT = (result.toDouble() / 1.2).roundToInt().toString()
        return salesExVAT
    }
    result1 = ""
    return result1
}
fun calculateATV(text: String) : String {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.CEILING
    if (text.isNotEmpty()){
        return df.format(salesExVAT.toDouble()/text.toDouble()).toString()
    }
    return text
}

private fun whatsAppResultShare(context: Context) {
    Intent(Intent.ACTION_SEND).also {
        it.setPackage("com.whatsapp")
        it.putExtra(Intent.EXTRA_TEXT, "THIS IS A TEST")
        it.type = "text/plain"
        try {
            context.startActivity(it)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }
}
