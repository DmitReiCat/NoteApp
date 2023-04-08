package com.example.mynoteapp.feature_note.domain.repository

import com.example.mynoteapp.feature_note.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getSubNotes(parentId: Long?): Flow<List<Note>>

    suspend fun getNoteById(id: Long): Note?

    suspend fun insertNote(note: Note): Long

    suspend fun deleteNote(note: Note)
}