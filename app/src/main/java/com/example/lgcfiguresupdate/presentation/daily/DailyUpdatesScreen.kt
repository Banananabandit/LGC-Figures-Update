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
@Composable
fun DailyUpdatesScreen() {
    val viewModel: DailyUpdatesViewModel = viewModel()
    val sales = viewModel.salesIncVAT.value
    val salesExVAT = viewModel.salesExVAT.value
    val transactions = viewModel.numberOfTransactions.value
    val atv = viewModel.atv.value
    val messageFlag = viewModel.messageFlag.value

    Column {
        SalesUpdate(
            sales = sales,
            onTextChange = { newValue ->
                viewModel.onTextChangeSales(newValue)
            },
            salesExVAT = salesExVAT
        )
        CustomerNumberUpdate(
            customers = transactions,
            onTextChange = { newValue ->
                viewModel.onTextChangeTransactions(newValue)
            },
            transactions = atv
        )
        RadioSelector{ message ->
            viewModel.setMessageFlag(message)
        }
        Row {
//            SubmitButton()
            ResetButton { viewModel.resetValues() }
        }
    }
}

@Composable
fun ResetButton(onReset: () -> Unit) {
    Button(onClick = onReset) {
        Image(imageVector = Icons.Default.Refresh, contentDescription = "Refresh icon")
    }
}

//@Composable
//fun SubmitButton() {
//    val context = LocalContext.current
//    Button(onClick = {
//            whatsAppResultShare(context, generateMessage())
//    }) {
//        Text(text = "Share")
//    }
//}


@Composable
fun SalesUpdate(
    sales: String,
    onTextChange: (String) -> Unit,
    salesExVAT: String

) {
    OutlinedTextField(
        value = sales,
        onValueChange = onTextChange,
        singleLine = true,
        label = { Text("Sales") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
    Row {
        Text(text = "Result: ")
        Text(text = salesExVAT)
    }
}

@Composable
fun CustomerNumberUpdate(
    customers: String,
    onTextChange: (String) -> Unit,
    transactions: String
) {
    Text(text = "Number of Customers")
    OutlinedTextField(
        value = customers,
        onValueChange = onTextChange,
        singleLine = true,
        label = {Text("ATV")},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
    Row {
        Text(text = "ATV")
        Text(text = transactions)
    }
}

@Composable
fun RadioSelector(setMessageFlag: (String) -> Unit) {
    val options = listOf(
        "Update",
        "Final",
    )

    var selectedOption by remember {
        mutableStateOf("")
    }
    val onSelectionChange = { text: String ->
        selectedOption = text
        val messageFlag = if (selectedOption == "Update") "Update"
            else "Final"
        setMessageFlag(messageFlag)
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

//private fun generateMessage(): String {
//    return if (messageFlag == "Update")
//        "Update: £$salesExVAT, Trans: $numberOfTransactions, ATV: £$atvExVAT"
//    else
//        "Final: £$salesExVAT, Trans: $numberOfTransactions, ATV: £$atvExVAT"
//}