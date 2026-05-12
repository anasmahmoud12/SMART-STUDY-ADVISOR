package com.example.frontend.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.frontend.model.AIErrorResponse
import com.example.frontend.model.AIRequest
import com.example.frontend.model.Message
import com.example.frontend.service.RetroFitInstance
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException


@Composable
fun ChatBox(navController: NavController, username: String) {
    val messages = remember { mutableStateListOf<Message>() }
    var message by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) {data ->
            Snackbar(
                snackbarData = data,
                containerColor = Color(0xFF50C878),
                contentColor = Color.White
            )
        } },
        modifier = Modifier.imePadding(),
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(20.dp)
                .imePadding()
                .navigationBarsPadding(),

            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.size(20.dp))
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "AI Chat",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
                Button(
                    onClick = {
                        navController.navigate("prolog/$username")
                    },
                    modifier = Modifier.align(Alignment.CenterEnd),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = Color.White
                    )
                ) {
                    Text("Prolog")
                }
            }
            LazyColumn(
                state = listState,
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

                        LaunchedEffect(messages.size) {
                            listState.animateScrollToItem(messages.size - 1)
                        }

                        Card(
                            modifier = Modifier.padding(4.dp),

                            colors = CardDefaults.cardColors(
                                containerColor =
                                    if (message.role == "ai") {
                                        Color(0xFF17C6E5)
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
                                scope.launch {
                                    try {
                                        val response =
                                            RetroFitInstance.api.chatAI(AIRequest(message))
                                        messages.add(Message("user", message))
                                        messages.add(Message("ai", response.response))
                                        message = ""
                                    } catch (e: HttpException) {
                                        val errorResponse = e.response()?.errorBody()?.string()
                                        val error = Gson().fromJson(
                                            errorResponse,
                                            AIErrorResponse::class.java
                                        )
                                        message = ""
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Error Sending Request to API\nTry Sending Again.")
                                        }
                                        println(error.message)
                                    }
                                }
                            } else {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Write Your Request")
                                }

                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.Black,
                            containerColor = Color.White
                        )
                    ) {
                        Text("Send")
                    }
                }
            }

        }


    }
}