package com.example.frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.frontend.layouts.ChatBox
import com.example.frontend.layouts.Home
import com.example.frontend.layouts.Login
import com.example.frontend.ui.theme.FrontendTheme

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