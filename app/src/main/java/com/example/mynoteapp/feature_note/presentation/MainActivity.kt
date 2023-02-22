package com.example.mynoteapp.feature_note.presentation

import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.updatePadding
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mynoteapp.feature_note.presentation.add_edit_note.AddEditNoteScreen
import com.example.mynoteapp.feature_note.presentation.notes.NotesScreen
import com.example.mynoteapp.feature_note.presentation.project.ProjectScreen
import com.example.mynoteapp.feature_note.presentation.util.Screen
import com.example.mynoteapp.ui.theme.MyApplicationTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            MyApplicationTheme {
                val systemUiController = rememberSystemUiController()
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = false
                    )
                }

                Surface(
                    color = MaterialTheme.colorScheme.background,
                ) {

                    DestinationsNavHost(navGraph = NavGraphs.root)
//                    val navController = rememberNavController()
//                    NavHost(
//                        navController = navController,
//                        startDestination = Screen.ProjectScreen.route,
//                        ) {
//                        composable(
//                            route = Screen.ProjectScreen.route
//                        ) {
////                            ProjectScreen(navController = navController)
//                        }
//
//                        composable(
//                            route = Screen.NotesScreen.route +
//                                    "?noteId={noteId}&noteParentId={noteParentId}",
//                            arguments = listOf(
//                                navArgument("noteId") {
//                                    type = NavType.IntType
//                                    defaultValue = -1
//                                },
//                                navArgument("noteParentId") {
//                                    type = NavType.IntType
//                                    defaultValue = -1
//                                }
//                            ),
//                        ) {
////                            NotesScreen(navController = navController)
//                        }
//                        composable(
//                            route = Screen.AddEditNote.route +
//                                    "?noteId={noteId}&noteColor={noteColor}", arguments = listOf(
//                                navArgument(name = "noteId") {
//                                    type = NavType.IntType
//                                    defaultValue = -1
//                                },
//                                navArgument(name = "noteColor") {
//                                    type = NavType.IntType
//                                    defaultValue = -1
//                                }
//                            )
//                        ) {
//                            val color = it.arguments?.getInt("noteColor") ?: -1
//                            AddEditNoteScreen(
//                                navController = navController,
//                                noteColor = color
//                            )
//                        }
//                    }
                }
            }
        }
    }
}