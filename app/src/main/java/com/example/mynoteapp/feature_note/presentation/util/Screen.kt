package com.example.mynoteapp.feature_note.presentation.util

sealed class Screen(val route: String) {
    object ProjectScreen: Screen("project_screen")
    object NotesScreen: Screen("notes_screen")
    object AddEditNote: Screen("add_edit_note_screen")
}
