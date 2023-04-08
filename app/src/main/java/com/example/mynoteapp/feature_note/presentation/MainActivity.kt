package com.example.mynoteapp.feature_note.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.example.compose.AppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            AppTheme {
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