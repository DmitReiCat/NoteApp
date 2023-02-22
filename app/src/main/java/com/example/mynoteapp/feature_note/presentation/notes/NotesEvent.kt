package com.example.mynoteapp.feature_note.presentation.notes

import com.example.mynoteapp.feature_note.domain.model.Note
import com.example.mynoteapp.feature_note.domain.util.NoteOrder

sealed class NotesEvent {
    data class Order(val noteOrder: NoteOrder): NotesEvent()
    data class DeleteNote(val note: Note): NotesEvent()
    object RestoreNote: NotesEvent()
    object ToggleOrderSection: NotesEvent()
    data class ChangeParentId(val parentId: Int): NotesEvent()
    object OnBackPressed: NotesEvent()
    data class ToggleBottomSheet(val noteId: Int = -1): NotesEvent()
}
