package com.example.mynoteapp.feature_note.domain.use_case

import com.example.mynoteapp.feature_note.domain.model.InvalidNoteException
import com.example.mynoteapp.feature_note.domain.model.Note
import com.example.mynoteapp.feature_note.domain.repository.NoteRepository

class SaveNoteInteractor(
    private val repository: NoteRepository
) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if (note.title.isBlank())
            throw InvalidNoteException("Title of the note cannot be empty.")
        else repository.insertNote(note)
    }
}