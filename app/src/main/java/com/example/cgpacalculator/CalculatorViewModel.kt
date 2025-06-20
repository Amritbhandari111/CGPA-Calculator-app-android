package com.example.cgpacalculator

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import net.objecthunter.exp4j.ExpressionBuilder

class CalculatorViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = CalculatorDataStore(application)


    private val _expression = MutableStateFlow("")
    val expression: StateFlow<String> = _expression

    private val _result = MutableStateFlow("")
    val result: StateFlow<String> = _result

    private val _history = MutableStateFlow<List<String>>(emptyList())
    val history: StateFlow<List<String>> = _history

    init {
        viewModelScope.launch {
            dataStore.getLastCalculation().collect {
                _expression.value = it.first
                _result.value = it.second
            }
        }
        viewModelScope.launch {
            dataStore.getHistory().collect {
                _history.value = it
            }
        }
    }

    fun onButtonClick(input: String) {
        when (input) {
            "C" -> {
                _expression.value = ""
                _result.value = ""
                saveCalculation("", "")
            }

            "=" -> {
                try {
                    val res = evaluateExpression(_expression.value)
                    _result.value = res
                    saveCalculation(_expression.value, res)
                    saveToHistory(_expression.value, res)
                } catch (e: Exception) {
                    _result.value = "Error"
                }
            }

            "⌫" -> _expression.value = _expression.value.dropLast(1)

            else -> _expression.value += input
        }
    }

    private fun evaluateExpression(expression: String): String {
        return try {
            val sanitized = expression.replace("×", "*").replace("÷", "/")
            val result = ExpressionBuilder(sanitized).build().evaluate()
            if (result % 1 == 0.0) result.toInt().toString() else result.toString()
        } catch (e: Exception) {
            "Error"
        }
    }

    private fun saveCalculation(expr: String, result: String) {
        viewModelScope.launch {
            dataStore.saveCalculation(expr, result)
        }
    }

    private fun saveToHistory(expr: String, result: String) {
        viewModelScope.launch {
            dataStore.saveToHistory(expr, result)
        }
    }
    fun clearHistory() {
        viewModelScope.launch {
            dataStore.clearHistory()
        }
    }
}
