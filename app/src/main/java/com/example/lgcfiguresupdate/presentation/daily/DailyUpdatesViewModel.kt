package com.example.lgcfiguresupdate.presentation.daily

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

class DailyUpdatesViewModel() : ViewModel() {
    val salesIncVAT = mutableStateOf("")
    val numberOfTransactions = mutableStateOf("")
    var salesExVAT = mutableStateOf("")
    var ATV = mutableStateOf("")


    fun onTextChangeSales(newText: String) {
        if (verifyFieldInput(newText)) {
            salesIncVAT.value = newText
            calculateSalesExVAT(newText)
        }
    }

    fun onTextChangeTransactions(newText: String) {
        if (verifyFieldInput(newText)) {
            numberOfTransactions.value = newText
            calculateATV(newText)
        }
    }

    private fun calculateSalesExVAT(salesIncVAT: String) {
        if (salesIncVAT != "") {
            salesExVAT.value = (salesIncVAT.toDouble() / 1.2).roundToInt().toString()
        } else salesExVAT.value = ""
    }

    private fun calculateATV(transactions: String) {
        // Maybe there is more graceful way to do the formatting?
        if (salesExVAT.value != "" && transactions != "") {
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            ATV.value = df.format((salesExVAT.value.toDouble() / transactions.toDouble())).toString()
        } else ATV.value = ""
    }

    private fun verifyFieldInput(text: String) = text.matches(Regex("^\\d*\$"))

    fun resetValues() {
        salesIncVAT.value = ""
        numberOfTransactions.value = ""
        salesExVAT.value = ""
    }

}