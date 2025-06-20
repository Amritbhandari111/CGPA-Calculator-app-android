package com.example.cgpacalculator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Intent
import androidx.compose.material3.Divider
import androidx.compose.ui.platform.LocalContext
@Preview(showBackground = true)
@Composable
fun SimpleCalculatorScreen(viewModel: CalculatorViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val expression by viewModel.expression.collectAsState()
    val result by viewModel.result.collectAsState()
    val history by viewModel.history.collectAsState(initial = emptyList())
    val context = LocalContext.current


    var showHistory by remember { mutableStateOf(false) }

    val buttonRows = listOf(
        listOf("C", "⌫", "%", "÷"),
        listOf("7", "8", "9", "×"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("0", ".", "=")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(12.dp)
    ) {
        // Toggle History


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "View History",
                color = Color.Cyan,
                fontSize = 16.sp,
                modifier = Modifier
                    .clickable {
                        context.startActivity(Intent(context, CalculatorHistoryActivity::class.java))
                    }
                    .padding(end = 8.dp)
            )
        }


        // Calculator Display
        Box(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomEnd
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = expression.ifBlank { "0" },
                    fontSize = 48.sp,
                    color = Color.White,
                    textAlign = TextAlign.End
                )
                if (result.isNotBlank()) {
                    Text(
                        text = "= $result",
                        fontSize = 28.sp,
                        color = Color.LightGray,
                        textAlign = TextAlign.End
                    )
                }
            }
        }

        // Divider before history
        if (showHistory && history.isNotEmpty()) {
            Divider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // History List
        if (showHistory && history.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1E1E1E))
                    .padding(8.dp)
                    .weight(2f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Calculation History",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                history.reversed().forEach {
                    Text(
                        text = it,
                        color = Color.LightGray,
                        fontSize = 16.sp
                    )
                }
            }
        }

        // Buttons Section
        Column(
            modifier = Modifier
                .weight(if (showHistory) 3f else 5f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            buttonRows.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    row.forEach { label ->
                        val isZero = label == "0"
                        val buttonModifier = if (isZero) {
                            Modifier
                                .weight(2f)
                                .aspectRatio(2f)
                        } else {
                            Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                        }

                        CalculatorButton(
                            symbol = label,
                            modifier = buttonModifier,
                            onClick = {
                                viewModel.onButtonClick(label)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(
    symbol: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val backgroundColor = when (symbol) {
        "C", "⌫", "%" -> Color(0xC6F5F5F5)
        "÷", "×", "-", "+", "=" -> Color(0xE2FF8F00)
        else -> Color(0xFF333333)
    }

    val contentColor = if (symbol in listOf("C", "⌫", "%")) Color.Black else Color.White

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable { onClick() }
    ) {
        Text(
            text = symbol,
            fontSize = 28.sp,
            color = contentColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}


