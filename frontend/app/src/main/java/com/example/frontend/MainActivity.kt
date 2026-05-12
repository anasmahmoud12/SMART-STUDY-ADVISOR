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
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.frontend.layouts.ChatBox
import com.example.frontend.layouts.Home
import com.example.frontend.layouts.Login
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
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "login"
            ) {

                composable("login") {
                    Login(navController)
                }

                composable("prolog/{username}") { backStackEntry ->
                    val username = backStackEntry.arguments?.getString("username")?: ""
                    Home(navController, username)
                }

                composable("chat/{username}") { backStackEntry ->
                    val username = backStackEntry.arguments?.getString("username")?: ""

                    ChatBox(navController, username)
                }

            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ChatBoxPreview() {
    FrontendTheme() {
        val navigationController = rememberNavController()
        ChatBox(navigationController, "student")
    }

}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    FrontendTheme() {
        val navigationController = rememberNavController()
        Login(navigationController)
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    FrontendTheme() {
        val navigationController = rememberNavController()
        Home(navigationController, "student")
    }
}