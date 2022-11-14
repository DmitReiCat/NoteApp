package com.example.mynoteapp.feature_note.domain.use_case

import com.example.mynoteapp.feature_note.domain.model.Note
import com.example.mynoteapp.feature_note.domain.repository.NoteRepository

class GetNoteInteractor(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(id: Int): Note? = repository.getNoteById(id)

}