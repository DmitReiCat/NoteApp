package com.example.mynoteapp.feature_note.domain.use_case

import com.example.mynoteapp.feature_note.domain.model.Note
import com.example.mynoteapp.feature_note.domain.repository.NoteRepository

/**
 * Creates empty entry in database and returns the id
 */
class CreateNoteInteractor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note): Long {
        return repository.insertNote(note)
    }
}