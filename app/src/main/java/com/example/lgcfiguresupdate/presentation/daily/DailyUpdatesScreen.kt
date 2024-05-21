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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

var atvExVAT = ""
var salesExVAT = ""
var numberOfTransactions = ""
var messageFlag = ""
@Composable
fun DailyUpdatesScreen() {
    val viewModel: DailyUpdatesViewModel = viewModel()
    val sales = viewModel.salesIncVAT.value
    Column {
        SalesUpdate(sales = sales, onTextChange = { newValue -> viewModel.onTextChange(newValue)})
        CustomerNumberUpdate()
        RadioSelector()
        Row {
            SubmitButton()
            ResetButton { viewModel.resetValues() }
        }
    }
}

@Composable
fun ResetButton(onReset: () -> Unit) {
    Button(onClick =
        onReset
    ) {
        Image(imageVector = Icons.Default.Refresh, contentDescription = "Refresh icon")
    }
}

@Composable
fun SubmitButton() {
    val context = LocalContext.current
    Button(onClick = {
        if (atvExVAT !== "" && numberOfTransactions !== "")
            whatsAppResultShare(context, generateMessage())
    }) {
        Text(text = "Share")
    }
}


@Composable
fun SalesUpdate(sales: String, onTextChange: (String) -> Unit) {
    OutlinedTextField(
        value = sales,
        onValueChange = onTextChange
//            if (it.matches(Regex("^\\d*\$"))) {text = it}
        ,
        singleLine = true,
        label = { Text("Sales") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
    Row {
        Text(text = "Result")
        Result(calculateExVAT(sales))
    }
}

@Composable
fun CustomerNumberUpdate() {
    var text by remember { mutableStateOf("") }
    Text(text = "Number of Customers")
    OutlinedTextField(
        value = text,
        onValueChange = {text = it},
        singleLine = true,
        label = {Text("ATV")},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
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
        messageFlag = if (selectedOption == "Update") "Update"
            else "Final"
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
    if (result !== ""){
        salesExVAT = (result.toDouble() / 1.2).roundToInt().toString()
        return salesExVAT
    }
    return result
}
fun calculateATV(text: String) : String {
    numberOfTransactions = text
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.CEILING
    if (text.isNotEmpty()){
        atvExVAT = df.format(salesExVAT.toDouble()/text.toDouble()).toString()
    }
    return atvExVAT
}

private fun whatsAppResultShare(context: Context, message: String) {
    Intent(Intent.ACTION_SEND).also {
        it.setPackage("com.whatsapp")
        it.putExtra(Intent.EXTRA_TEXT, message)
        it.type = "text/plain"
        try {
            context.startActivity(it)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }
}

private fun generateMessage(): String {
    return if (messageFlag == "Update")
        "Update: £$salesExVAT, Trans: $numberOfTransactions, ATV: £$atvExVAT"
    else
        "Final: £$salesExVAT, Trans: $numberOfTransactions, ATV: £$atvExVAT"
}