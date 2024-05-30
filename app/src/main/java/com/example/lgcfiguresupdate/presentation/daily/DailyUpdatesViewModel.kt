package com.example.lgcfiguresupdate.presentation.daily

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.roundToInt

class DailyUpdatesViewModel() : ViewModel() {
    val salesIncVAT = mutableStateOf("")
    val numberOfTransactions = mutableStateOf("")
    var salesExVAT = mutableStateOf("")
    var atv = mutableStateOf("")

    private var messageFlag = mutableStateOf("")

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
            atv.value = df.format((salesExVAT.value.toDouble() / transactions.toDouble())).toString()
        } else atv.value = ""
    }

    fun setMessageFlag(message: String) {
        messageFlag.value = message
    }
    private fun generateMessage(): String {
        return if (messageFlag.value == "Update")
            "Update: £${salesExVAT.value}, Trans: ${numberOfTransactions.value}, ATV: £${atv.value}"
        else
            "Final: £${salesExVAT.value}, Trans: ${numberOfTransactions.value}, ATV: £${atv.value}"
    }

    fun whatsAppResultShare(context: Context) {
        Intent(Intent.ACTION_SEND).also {
            it.setPackage("com.whatsapp")
            it.putExtra(Intent.EXTRA_TEXT, generateMessage())
            it.type = "text/plain"
            try {
                context.startActivity(it)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        }
    }


    private fun verifyFieldInput(text: String) = text.matches(Regex("^\\d*\$"))

    fun resetValues() {
        salesIncVAT.value = ""
        numberOfTransactions.value = ""
        salesExVAT.value = ""
    }

}