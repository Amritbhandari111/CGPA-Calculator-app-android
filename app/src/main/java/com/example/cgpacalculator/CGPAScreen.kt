package com.example.cgpacalculator

// Import same as your original CGPA screen — no version issues
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
@Preview(showBackground = true)
@Composable
fun CGPA() {
    var subjectNames = remember { mutableStateListOf("", "") }
    var grades = remember { mutableStateListOf("", "") }
    var credits = remember { mutableStateListOf("", "") }
    var calculatedCGPA by remember { mutableStateOf(0.0) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
           /* item {
                Text(
                    text = "📚 CGPA Calculator",
                    fontSize = 25.sp,
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center,
                    color = Color(0xFFFFC107),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }*/

            itemsIndexed(grades) { index, grade ->
                SubjectRow(
                    subjectNumber = index + 1,
                    subjectName = subjectNames[index],
                    grade = grade,
                    credit = credits[index],
                    onNameChange = { subjectNames[index] = it },
                    onGradeChange = { grades[index] = it },
                    onCreditChange = { credits[index] = it },
                    onDelete = {
                        subjectNames.removeAt(index)
                        grades.removeAt(index)
                        credits.removeAt(index)
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            subjectNames.add("")
                            grades.add("")
                            credits.add("")
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }

                    Button(
                        onClick = {
                            if (grades.any { it.isBlank() } || credits.any { it.isBlank() } || subjectNames.any { it.isBlank() }) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Please fill all fields!")
                                }
                            } else {
                                isLoading = true
                                coroutineScope.launch {
                                    delay(1000)
                                    calculatedCGPA = calculateCGPA(grades, credits)
                                    isLoading = false
                                    showDialog = true
                                }
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722)),
                        modifier = Modifier
                            .weight(1.7f)
                            .height(50.dp)
                    ) {
                        Text("Calculate")
                    }

                    Button(
                        onClick = {
                            subjectNames.clear()
                            grades.clear()
                            credits.clear()
                            calculatedCGPA = 0.0
                            isLoading = false
                            repeat(2) {
                                subjectNames.add("")
                                grades.add("")
                                credits.add("")
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0)),
                        modifier = Modifier
                            .weight(1.3f)
                            .height(50.dp)
                    ) {
                        Text("Reset")
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }

            item {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(50.dp)
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally),
                        color = Color(0xFFFFC107),
                        strokeWidth = 6.dp
                    )
                } else {
                    Text(
                        text = "🎓 Your CGPA: %.2f".format(calculatedCGPA),
                        fontSize = 22.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF1E88E5), RoundedCornerShape(16.dp))
                            .padding(20.dp)
                    )
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("OK")
                }
            },
            title = { Text("🎓 CGPA Result") },
            text = {
                Column {
                    Text("Your CGPA is %.2f".format(calculatedCGPA), fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                   Text("Remark: ${getRemark(calculatedCGPA)}", fontSize = 16.sp)
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectRow(
    subjectNumber: Int,
    subjectName: String,
    grade: String,
    credit: String,
    onNameChange: (String) -> Unit,
    onGradeChange: (String) -> Unit,
    onCreditChange: (String) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF3949AB))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Subject $subjectNumber",
                    fontSize = 18.sp,
                    color = Color.White
                )
                IconButton(onClick = { onDelete() }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                value = subjectName,
                onValueChange = onNameChange,
                label = { Text("Subject Name ", color = Color.White, fontSize = 12.sp) },
                modifier = Modifier.fillMaxWidth(0.9f),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White,
                    cursorColor = Color.White,
                    focusedBorderColor = Color(0xFFFFC107),
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Color(0xFFFFC107),
                    unfocusedLabelColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = grade,
                onValueChange = onGradeChange,
                label = { Text("Enter Grade (10, 9...)", color = Color.White, fontSize = 12.sp) },
                modifier = Modifier.fillMaxWidth(0.9f),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White,
                    cursorColor = Color.White,
                    focusedBorderColor = Color(0xFFFFC107),
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Color(0xFFFFC107),
                    unfocusedLabelColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = credit,
                onValueChange = onCreditChange,
                label = { Text("Enter Credit", color = Color.White, fontSize = 12.sp) },
                modifier = Modifier.fillMaxWidth(0.9f),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White,
                    cursorColor = Color.White,
                    focusedBorderColor = Color(0xFFFFC107),
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Color(0xFFFFC107),
                    unfocusedLabelColor = Color.White
                )
            )
        }
    }
}

fun calculateCGPA(grades: List<String>, credits: List<String>): Double {
    var totalPoints = 0.0
    var totalCredits = 0.0

    for (i in grades.indices) {
        val gradeValue = grades[i].toDoubleOrNull() ?: 0.0
        val creditValue = credits[i].toDoubleOrNull() ?: 0.0

        totalPoints += gradeValue * creditValue
        totalCredits += creditValue
    }

    return if (totalCredits == 0.0) 0.0 else totalPoints / totalCredits
}

fun getRemark(cgpa: Double): String {
    return when {
        cgpa >= 9.0 -> "Excellent 🎉"
        cgpa >= 8.0 -> "Very Good 👍"
        cgpa >= 7.0 -> "Good 🙂"
        cgpa >= 6.0 -> "Average 😐"
        cgpa >= 5.0 -> "Poor 😕"
        else -> "Very Poor 😞"
    }
}
