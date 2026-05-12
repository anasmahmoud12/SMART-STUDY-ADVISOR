package com.example.frontend.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
    var mode by remember { mutableStateOf("Prolog") }
    var interests by remember { mutableStateOf(listOf<String>()) }

    var interestsExpanded by remember { mutableStateOf(false) }

    var selectedInterests by remember { mutableStateOf(setOf<String>()) }

    val difficulties = listOf("Elementary", "Intermediate", "Advanced")

    var chosenDifficulty by remember { mutableStateOf("Elementary") }

    var courses by remember { mutableStateOf(listOf<String>()) }
    var expanded by remember { mutableStateOf(true) }

    var diffExpanded by remember { mutableStateOf(false) }

    var selectedCourses by remember { mutableStateOf(setOf<String>()) }

    var results by remember { mutableStateOf(setOf<String>()) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {

        try {
            val metaData = RetroFitInstance.api.getMetaData()

            courses = metaData.data.courses
            interests = metaData.data.topics

        } catch (e: Exception) {
            println(e.message)
            println("ERROR HERE")
        }

    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(12.dp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            onClick = {
                navController.navigate("chat/$username")
            },

            modifier = Modifier
                .align(Alignment.End),

            shape = RoundedCornerShape(6.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text(text = "AI Mode", color = Color.Black)
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text = "$mode Query",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(20.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }

        ) {
            OutlinedTextField(
                value = selectedCourses.joinToString(),
                onValueChange = {},
                readOnly = true,

                label = { Text(text = "Completed Courses", color = Color.White) },

                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },

                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                courses.forEach { course ->

                    DropdownMenuItem(

                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = course in selectedCourses,
                                    onCheckedChange = null
                                )

                                Text(course)
                            }
                        },

                        onClick = {
                            selectedCourses =
                                if (course in selectedCourses) {
                                    selectedCourses - course
                                } else {
                                    selectedCourses + course
                                }
                        }
                    )

                }

            }

        }

        ExposedDropdownMenuBox(
            expanded = interestsExpanded,
            onExpandedChange = {
                interestsExpanded = !interestsExpanded
            }
        ) {

            OutlinedTextField(
                value = selectedInterests.joinToString(),
                onValueChange = {},
                readOnly = true,

                label = { Text(text = "Interest", color = Color.White) },

                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(interestsExpanded) },

                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = interestsExpanded,
                onDismissRequest = {
                    interestsExpanded = false
                }
            ) {

                interests.forEach { interest ->

                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = interest in selectedInterests,
                                    onCheckedChange = null
                                )

                                Text(interest)
                            }
                        },
                        onClick = {
                            selectedInterests =
                                if (interest in selectedInterests) {
                                    selectedInterests - interest
                                } else {
                                    selectedInterests + interest
                                }
                        }
                    )

                }


            }


        }

        ExposedDropdownMenuBox(
            expanded = diffExpanded,
            onExpandedChange = {
                diffExpanded = !diffExpanded
            }
        ) {

            OutlinedTextField(
                value = chosenDifficulty,
                readOnly = true,
                onValueChange = {},

                label = { Text(text = "Difficulty", color = Color.White) },

                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(interestsExpanded) },

                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = diffExpanded,
                onDismissRequest = {
                    diffExpanded = false
                }
            ) {
                difficulties.forEach { diff ->

                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = diff == chosenDifficulty,
                                    onCheckedChange = null
                                )

                                Text(diff)
                            }
                        },
                        onClick = {
                            chosenDifficulty = diff
                        }
                    )
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp)
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Recommendations", color = Color.White)

                Spacer(Modifier.height(20.dp))

                results.forEach { result ->
                    Text(result, color = Color.White)
                }
            }

        }

        Button(
            onClick = {
                val request = RecommendationRequest(
                    student_name = username,
                    difficulty = chosenDifficulty.lowercase(),
                    interests = selectedInterests.toList(),
                    finished_courses = selectedCourses.toList()
                )
                println(request)

                scope.launch {
                    try {
                        val response = RetroFitInstance.api.getRecommendation(request)
                        results = response.data.courses.toSet()
                        println(response)
                    } catch (e: Exception) {
                        println(e.message)
                    }
                }
            },


            shape = RoundedCornerShape(6.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text(text = "Make Request", color = Color.Black)
        }

    }


}
