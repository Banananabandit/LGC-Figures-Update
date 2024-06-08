package com.example.lgcfiguresupdate.presentation.daily

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun DailyUpdatesScreen() {
    val viewModel: DailyUpdatesViewModel = viewModel()
    val sales = viewModel.salesIncVAT.value
    val salesExVAT = viewModel.salesExVAT.value
    val transactions = viewModel.numberOfTransactions.value
    val atv = viewModel.atv.value
    val localFocusManager: FocusManager = LocalFocusManager.current

    Column(modifier = Modifier.padding(16.dp)) {
        SalesUpdate(
            sales = sales,
            onTextChange = { newValue ->
                viewModel.onTextChangeSales(newValue)
            },
            salesExVAT = salesExVAT,
            modifier = Modifier,
            focusManager = localFocusManager
        )
        Spacer(modifier = Modifier.padding(16.dp))
        CustomerNumberUpdate(
            customers = transactions,
            onTextChange = { newValue ->
                viewModel.onTextChangeTransactions(newValue)
            },
            transactions = atv,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.padding(16.dp))
        RadioSelector{ message ->
            viewModel.setMessageFlag(message)
        }
        Spacer(modifier = Modifier.padding(16.dp))
        Row {
            SubmitButton { context ->
                viewModel.whatsAppResultShare(context)}
            ResetButton { viewModel.resetValues() }
        }
    }
}



@Composable
fun SalesUpdate(
    sales: String,
    onTextChange: (String) -> Unit,
    salesExVAT: String,
    modifier: Modifier,
    focusManager: FocusManager
) {
    OutlinedTextField(
        value = sales,
        onValueChange = onTextChange,
        singleLine = true,
        label = { Text("Sales") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down )}),
        modifier = modifier.fillMaxWidth()
    )
    Row(modifier = modifier.padding(top = 8.dp)) {
        Text(
            text = "Result: ",
            modifier = modifier.padding(start = 16.dp),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = salesExVAT,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CustomerNumberUpdate(
    customers: String,
    onTextChange: (String) -> Unit,
    transactions: String,
    modifier: Modifier
) {
    OutlinedTextField(
        value = customers,
        onValueChange = onTextChange,
        singleLine = true,
        label = {Text("Number of Customers")},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier.fillMaxWidth()
    )
    Row(modifier = modifier.padding(top = 8.dp)) {
        Text(
            text = "ATV: ",
            modifier = modifier.padding(start = 16.dp),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = transactions,
            fontWeight = FontWeight.Bold
        )
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
                    fontWeight = FontWeight.Bold,
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
fun SubmitButton(shareToWA: (Context) -> Unit) {
    val context = LocalContext.current
    Button(onClick = {
        shareToWA(context)
    }) {
        Text(text = "Share")
    }
}

@Composable
fun ResetButton(onReset: () -> Unit) {
    Button(onClick = onReset) {
        Image(imageVector = Icons.Default.Refresh, contentDescription = "Refresh icon")
    }
}
