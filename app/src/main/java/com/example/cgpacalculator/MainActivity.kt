// Top-level imports remain unchanged
package com.example.cgpacalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.cgpacalculator.ui.theme.CGPACalculatorTheme

// Add SalesTax item
sealed class BottomNavItem(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object CGPA : BottomNavItem("CGPA", Icons.Default.School)
    object Calculator : BottomNavItem("Calculator", Icons.Default.Calculate)
    object SalesTax : BottomNavItem("Sales Tax", Icons.Default.Money) // NEW
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CGPACalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFC2A9A9)
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var selectedItem by remember { mutableStateOf<BottomNavItem>(BottomNavItem.Calculator) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0x61FFFFFF),
                contentColor = Color.White
            ) {
                NavigationBarItem(
                    selected = selectedItem == BottomNavItem.CGPA,
                    onClick = { selectedItem = BottomNavItem.CGPA },
                    icon = { Icon(BottomNavItem.CGPA.icon, contentDescription = "CGPA") },
                    label = { Text("CGPA") }
                )
                NavigationBarItem(
                    selected = selectedItem == BottomNavItem.Calculator,
                    onClick = { selectedItem = BottomNavItem.Calculator },
                    icon = { Icon(BottomNavItem.Calculator.icon, contentDescription = "Calculator") },
                    label = { Text("Calculator") }
                )
                NavigationBarItem( // NEW: Sales Tax tab
                    selected = selectedItem == BottomNavItem.SalesTax,
                    onClick = { selectedItem = BottomNavItem.SalesTax },
                    icon = { Icon(BottomNavItem.SalesTax.icon, contentDescription = "Sales Tax") },
                    label = { Text("Sales Tax") }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedItem) {
                is BottomNavItem.CGPA -> CGPA()
                is BottomNavItem.Calculator -> SimpleCalculatorScreen()
                is BottomNavItem.SalesTax -> ConverterScreen() // NEW
            }
        }
    }
}
