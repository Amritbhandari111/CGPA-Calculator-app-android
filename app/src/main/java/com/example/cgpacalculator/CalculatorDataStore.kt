// CalculatorDataStore.kt
package com.example.cgpacalculator

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("calculator_prefs")

class CalculatorDataStore(private val context: Context) {
    private val EXPRESSION_KEY = stringPreferencesKey("expression")
    private val RESULT_KEY = stringPreferencesKey("result")
    private val HISTORY_KEY = stringSetPreferencesKey("history")


    suspend fun saveCalculation(expression: String, result: String) {
        context.dataStore.edit { prefs ->
            prefs[EXPRESSION_KEY] = expression
            prefs[RESULT_KEY] = result
        }
    }

    suspend fun saveToHistory(expression: String, result: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[HISTORY_KEY]?.toMutableSet() ?: mutableSetOf()
            current.add("$expression = $result")
            prefs[HISTORY_KEY] = current
        }
    }
    suspend fun clearHistory() {
        context.dataStore.edit { prefs ->
            prefs[HISTORY_KEY] = emptySet()
        }
    }


    fun getLastCalculation() = context.dataStore.data.map { prefs ->
        val expr = prefs[EXPRESSION_KEY] ?: ""
        val result = prefs[RESULT_KEY] ?: ""
        expr to result
    }

    fun getHistory() = context.dataStore.data.map { prefs ->
        prefs[HISTORY_KEY]?.toList() ?: emptyList()
    }
}
