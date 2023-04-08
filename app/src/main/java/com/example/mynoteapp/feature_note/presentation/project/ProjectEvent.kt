package com.example.mynoteapp.feature_note.presentation.project

import com.example.mynoteapp.feature_note.domain.model.Note
import com.example.mynoteapp.feature_note.domain.util.NoteOrder

sealed class ProjectEvent {
    data class Order(val noteOrder: NoteOrder): ProjectEvent()
    data class DeleteNote(val note: Note): ProjectEvent()
    object RestoreNote: ProjectEvent()
    object ToggleOrderSection: ProjectEvent()
}
