package com.example.mynoteapp.feature_note.domain.use_case

import com.example.mynoteapp.feature_note.domain.model.Note
import com.example.mynoteapp.feature_note.domain.repository.NoteRepository
import com.example.mynoteapp.feature_note.domain.util.NoteOrder
import com.example.mynoteapp.feature_note.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotesInteractor(
    private val repository: NoteRepository
) {

    operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
        parentId: Int?
    ): Flow<List<Note>> {
        return repository.getNotes(parentId).map { notes ->
            when (noteOrder.orderType) {
                is OrderType.Ascending -> {
                    when (noteOrder) {
                        is NoteOrder.Color -> notes.sortedBy { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedBy { it.timestamp }
                        is NoteOrder.Title -> notes.sortedBy { it.color }
                    }
                }
                is OrderType.Descending -> {
                    when (noteOrder) {
                        is NoteOrder.Color -> notes.sortedByDescending { it.title.lowercase() }
                        is NoteOrder.Date -> notes.sortedByDescending { it.timestamp }
                        is NoteOrder.Title -> notes.sortedByDescending { it.color }
                    }
                }
            }
        }
    }
}