package com.example.margumynas

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.margumynas.ui.CreateTaskApp
import com.example.margumynas.ui.TodoApp
import com.example.margumynas.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme{
                AppNavigation()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "todo_app") {
        composable("todo_app") {
            TodoApp(
                onCreateTaskClick = {
                    navController.navigate("create_task_app")
                }
            )
        }
        composable("create_task_app") {
            CreateTaskApp(
                onCreateTaskClick = {
                    navController.navigate("todo_app")
                },
                onNavigateBack = {
                    navController.navigate("todo_app")
                }
            )
        }
    }
}
