package com.example.lgcfiguresupdate.presentation.daily

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class DailyUpdatesViewModel() : ViewModel() {
    val salesIncVAT = mutableStateOf("")
    val numberOfTransactions = mutableStateOf("")

    fun onTextChange(newText: String) {
        salesIncVAT.value = newText
    }

    fun resetValues() {
        salesIncVAT.value = ""
    }
}