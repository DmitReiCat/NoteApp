package com.example.mynoteapp.feature_note.presentation.notes

import com.example.mynoteapp.feature_note.domain.model.Note
import com.example.mynoteapp.feature_note.domain.util.NoteOrder

data class NotesState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.DEFAULT,
    val isTopLevel: Boolean = true, //represents if navigationStack has children
    val isOrderSelectionVisible: Boolean = false, // TODO(will be replaced with sorting and filtering menus)
    val parentId: Long? = null, // TODO(delete as it is debug property)
    val openBottomSheet: Boolean = false //TODO(for future development)
)
