package com.example.cgpacalculator

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SalesTaxViewModel : ViewModel() {

    private val _taxRate = MutableStateFlow("")
    val taxRate: StateFlow<String> = _taxRate

    private val _originalPrice = MutableStateFlow("")
    val originalPrice: StateFlow<String> = _originalPrice

    private val _selectedCurrency = MutableStateFlow("")
    val selectedCurrency: StateFlow<String> = _selectedCurrency

    fun onTaxRateChanged(value: String) {
        _taxRate.value = value.filter { it.isDigit() || it == '.' }
    }

    fun onPriceChanged(value: String) {
        _originalPrice.value = value.filter { it.isDigit() || it == '.' }
    }

    fun onCurrencySelected(symbol: String) {
        _selectedCurrency.value = symbol
    }
}
