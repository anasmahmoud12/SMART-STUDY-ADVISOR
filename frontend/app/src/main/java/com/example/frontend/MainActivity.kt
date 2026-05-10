package com.example.frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.frontend.ui.theme.FrontendTheme
import androidx.compose.ui.graphics.Color
import com.example.frontend.model.AIRequest
import com.example.frontend.model.Message
import com.example.frontend.model.RecommendationRequest
import com.example.frontend.service.RetroFitInstance
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FrontendTheme {
                ChatBox()
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ChatBoxPreview() {
    FrontendTheme() {
        ChatBox()
    }

}

@Composable
fun ChatBox() {
    val messages = remember {
        mutableStateListOf<Message>(
            Message("ai", "hello"),
            Message("user", "hi")
        )
    }
    var message by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(20.dp),

        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("AI Chat", color = Color.White, style = MaterialTheme.typography.headlineMedium)
        LazyColumn (
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(messages) { message ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        if (message.role == "user") {
                            Arrangement.End
                        } else {
                            Arrangement.Start
                        }
                ) {
                    Card(
                        modifier = Modifier.padding(4.dp),

                        colors = CardDefaults.cardColors(
                            containerColor =
                                if (message.role == "ai") {
                                    Color.Blue
                                } else {
                                    Color.DarkGray
                                }
                        )
                    ) {

                        Text(
                            text = message.content,
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                    }


                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    placeholder = {
                        Text(
                            "Send Message",
                            color = Color.White
                        )
                    },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (message.replace(" ", "") != "") {
                            messages.add(Message("user", message))

                            scope.launch {
                                try {
                                    val response = RetroFitInstance.api.chatAI(AIRequest(message))
                                    messages.add(Message("ai", response.response))
                                    message = ""
                                } catch (e: Exception) {
                                    println(e.message)
                                }

                            }
                        }
                    }
                ) {
                    Text("Send")
                }
            }
        }

    }




}

@Composable
fun Login() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Login Page",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                println(username)
                println(password)
            },

            shape = RoundedCornerShape(6.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text("Login")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    FrontendTheme() {
        Login()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home() {
    var mode by remember { mutableStateOf("Prolog") }
    var interests by remember {mutableStateOf(listOf<String>())}

    var interestsExpanded by remember { mutableStateOf(false) }

    var selectedInterests by remember { mutableStateOf(setOf<String>()) }

    var difficulties = listOf<String>("Easy", "Medium", "Hard")

    var chosenDifficulty by remember { mutableStateOf("Easy") }

    var courses by remember {mutableStateOf(listOf<String>())}
    var expanded by remember {mutableStateOf(true)}

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
                mode = if (mode == "Prolog") {
                    "AI"
                } else {
                    "Prolog"
                }
            },

            modifier = Modifier
                .align(Alignment.End),

            shape = RoundedCornerShape(6.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
            ) {
            Text(text = "Switch Mode", color = Color.Black)
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
                courses.forEach{course ->

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

                interests.forEach{interest ->

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
                difficulties.forEach{diff ->

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

                results.forEach {result ->
                    Text("Result", color = Color.White)
                }
            }

        }

        Button(
            onClick = {
                var request = RecommendationRequest(
                    student_name = "anas",
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

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    FrontendTheme() {
        Home()
    }
}