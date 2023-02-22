package com.example.mynoteapp.feature_note.presentation.notes

import com.example.mynoteapp.feature_note.domain.model.Note
import com.example.mynoteapp.feature_note.domain.util.NoteOrder
import com.example.mynoteapp.feature_note.domain.util.OrderType

data class NotesState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSelectionVisible: Boolean = false,
    val parentId: Int? = null,
    val isTopLevel: Boolean = true,
    val openBottomSheet: Boolean = false
)
