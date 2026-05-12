package com.example.frontend.layouts

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay


@Composable
fun Login(navController: NavController) {
    var username by remember { mutableStateOf("") }

    // screen animation parameters
    var displayedText by remember { mutableStateOf("") }

    var showCursor by remember { mutableStateOf(true) }

    val fullText = "Hello, enter your name to begin"

    LaunchedEffect(Unit) {
        for (i in fullText.indices) {
            displayedText = fullText.substring(0, i + 1)
            delay(70)
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            showCursor = !showCursor
            delay(500)
        }
    }

    // background color infinite transition params
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val animatedColor by infiniteTransition.animateColor(
        initialValue = Color(0xFF4D0511),
        targetValue = Color(0xFFFF0000),
        animationSpec = infiniteRepeatable(

            animation = tween(
                durationMillis = 3000
            ),

            repeatMode = RepeatMode.Reverse
        ),
        label = ""

    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F172A),
                        animatedColor
                    )
                )
            )
            .padding(24.dp)
            .imePadding(),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = displayedText + if (showCursor) "|" else "",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 18.sp
            ),
            value = username,
            onValueChange = { username = it },
            label = {
                Text(
                    "Username", color = Color.White
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(0x25FFFFFF),
                unfocusedContainerColor = Color.Transparent,
                cursorColor = Color.Black

            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                navController.navigate("chat/$username")
            },

            shape = RoundedCornerShape(6.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            modifier = Modifier.shadow(
                elevation = 20.dp,
                shape = RectangleShape
            )
        ) {
            Text("Login")
        }
    }
}