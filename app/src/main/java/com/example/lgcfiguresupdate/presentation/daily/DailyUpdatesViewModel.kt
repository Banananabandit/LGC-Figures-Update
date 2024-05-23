package com.example.lgcfiguresupdate.presentation.daily

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class DailyUpdatesViewModel() : ViewModel() {
    val salesIncVAT = mutableStateOf("")
    val numberOfTransactions = mutableStateOf("")


    fun onTextChangeSales(newText: String) {
        if (verifyFieldInput(newText))
        salesIncVAT.value = newText
    }

    fun onTextChangeTransactions(newText: String) {
        if (verifyFieldInput(newText))
            numberOfTransactions.value = newText
    }

    private fun verifyFieldInput(text: String) = text.matches(Regex("^\\d*\$"))

    fun resetValues() {
        salesIncVAT.value = ""
        numberOfTransactions.value = ""
    }
}