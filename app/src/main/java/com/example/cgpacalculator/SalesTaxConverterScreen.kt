package com.example.cgpacalculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.compose.ui.unit.*






@OptIn(ExperimentalMaterial3Api::class)
@Composable
 fun ConverterScreen(viewModel: SalesTaxViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {


    val taxRate by viewModel.taxRate.collectAsState()
    val originalPrice by viewModel.originalPrice.collectAsState()
    val selectedCurrency by viewModel.selectedCurrency.collectAsState()

    val currencies = listOf("₹", "$", "€",  "₨", "₫", "₪", "₱",  "R", "₵", "₮")
    var expanded by remember { mutableStateOf(false) }

    val price = originalPrice.toDoubleOrNull() ?: 0.0
    val rate = taxRate.toDoubleOrNull() ?: 0.0
    val taxAmount = price * rate / 100
    val totalPrice = price + taxAmount

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Sales Tax Calculator",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                // Currency Dropdown
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = if (selectedCurrency.isEmpty()) "Choose currency" else selectedCurrency,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Currency") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        currencies.forEach { currency ->
                            DropdownMenuItem(
                                text = { Text(currency) },
                                onClick = {
                                    viewModel.onCurrencySelected(currency)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // Tax Rate Input
                OutlinedTextField(
                    value = taxRate,
                    onValueChange = { viewModel.onTaxRateChanged(it) },
                    label = { Text("Tax Rate (%)") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Original Price Input
                OutlinedTextField(
                    value = originalPrice,
                    onValueChange = { viewModel.onPriceChanged(it) },
                    label = { Text("Original Price") },
                    leadingIcon = {
                        if (selectedCurrency.isNotEmpty()) Text(selectedCurrency)
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Divider(thickness = 1.dp, color = Color.LightGray)

                if (selectedCurrency.isEmpty()) {
                    Text(
                        text = "Please select a currency to continue.",
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                } else {
                    ResultBox("Tax", if (price > 0 && rate > 0) "%.2f".format(taxAmount) else "", selectedCurrency)
                    ResultBox("Total Price", if (price > 0 && rate > 0) "%.2f".format(totalPrice) else "", selectedCurrency)
                }
            }
        }
    }
}



@Composable
fun ResultBox(label: String, value: String, symbol: String) {
    if (value.isEmpty()) return

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFEFF3FF)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Text(text = "$symbol$value", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ResultBox(label: String, value: String) {
    if (value.isEmpty()) return

    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFEFF3FF)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
