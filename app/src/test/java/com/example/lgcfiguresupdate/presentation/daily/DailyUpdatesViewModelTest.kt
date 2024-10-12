package com.example.lgcfiguresupdate.presentation.daily

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class DailyUpdatesViewModelTest {
    private lateinit var viewModel: DailyUpdatesViewModel

    @BeforeEach
    fun setUp() {
        viewModel = DailyUpdatesViewModel()
    }

    @ParameterizedTest
    @ValueSource(
        strings = ["a", "/", ".", "(", "/", "", " "]
    )
    fun `Passing NaN into verifyFieldInput returns false`(input: String) {
        viewModel.onTextChangeSales(input)

        assertThat(viewModel.salesExVAT.value).isEqualTo("")
    }

    @Test
    fun `calculateSalesExVAT function evaluates correctly`() {
        viewModel.onTextChangeSales("100")

        assertThat(viewModel.salesExVAT.value).isEqualTo("83")
    }

    @Test
    fun `calculateATV evaluates correctly`() {
        viewModel.salesExVAT.value = "1000"
        viewModel.onTextChangeTransactions("500")

        assertThat(viewModel.atv.value).isEqualTo("2")
    }


}