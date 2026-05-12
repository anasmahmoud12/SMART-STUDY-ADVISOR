package com.example.frontend.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.frontend.model.RecommendationRequest
import com.example.frontend.service.RetroFitInstance
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavController, username: String) {
    var interests by remember { mutableStateOf(listOf<String>()) }
    var courses by remember { mutableStateOf(listOf<String>()) }
    var selectedInterests by remember { mutableStateOf(setOf<String>()) }
    var selectedCourses by remember { mutableStateOf(setOf<String>()) }
    var chosenDifficulty by remember { mutableStateOf("Easy") }
    var results by remember { mutableStateOf(setOf<String>()) }

    var courseExp by remember { mutableStateOf(false) }
    var interestExp by remember { mutableStateOf(false) }
    var diffExp by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            val metaData = RetroFitInstance.api.getMetaData()
            courses = metaData.data.courses
            interests = metaData.data.topics
        } catch (e: Exception) { println(e.message) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextButton(
            onClick = { navController.navigate("chat/$username") },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Switch to AI Chat →", color = Color(0xFF17C6E5))
        }

        Spacer(Modifier.height(10.dp))
        Text("Prolog Query", style = MaterialTheme.typography.headlineMedium, color = Color.White)
        Spacer(Modifier.height(24.dp))

        // Courses Dropdown
        DropdownField("Completed Courses", selectedCourses.joinToString(", "), courseExp, { courseExp = it }) {
            courses.forEach { course ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = course in selectedCourses, onCheckedChange = null)
                            Text(course)
                        }
                    },
                    onClick = {
                        selectedCourses = if (course in selectedCourses) selectedCourses - course else selectedCourses + course
                    }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Interests Dropdown
        DropdownField("Interests", selectedInterests.joinToString(", "), interestExp, { interestExp = it }) {
            interests.forEach { interest ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = interest in selectedInterests, onCheckedChange = null)
                            Text(interest)
                        }
                    },
                    onClick = {
                        selectedInterests = if (interest in selectedInterests) selectedInterests - interest else selectedInterests + interest
                    }
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Difficulty Dropdown
        DropdownField("Difficulty", chosenDifficulty, diffExp, { diffExp = it }) {
            listOf("Easy", "Medium", "Hard").forEach { diff ->
                DropdownMenuItem(
                    text = { Text(diff) },
                    onClick = { chosenDifficulty = diff; diffExp = false }
                )
            }
        }

        Spacer(Modifier.height(30.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Recommendations", color = Color(0xFF17C6E5), style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.height(12.dp))
                if (results.isEmpty()) {
                    Text("Select options and click Make Request", color = Color.Gray)
                } else {
                    results.forEach { course ->
                        Text("• $course", color = Color.White, modifier = Modifier.padding(vertical = 2.dp))
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                val request = RecommendationRequest(username, chosenDifficulty.lowercase(), selectedInterests.toList(), selectedCourses.toList())
                scope.launch {
                    try {
                        val response = RetroFitInstance.api.getRecommendation(request)
                        results = response.data.courses.toSet()
                    } catch (e: Exception) { println(e.message) }
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Make Request")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(label: String, value: String, expanded: Boolean, onExpandedChange: (Boolean) -> Unit, content: @Composable ColumnScope.() -> Unit) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onExpandedChange
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            label = { Text(label, color = Color.Gray) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFF17C6E5),
                unfocusedBorderColor = Color.DarkGray
            )
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { onExpandedChange(false) }, content = content)
    }
}