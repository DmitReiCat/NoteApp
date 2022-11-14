package com.example.mynoteapp.feature_note.domain.use_case

data class NoteUseCases(
    val getNotes: GetNotesInteractor,
    val deleteNote: DeleteNoteInteractor,
    val addNote: AddNoteInteractor,
    val getNote: GetNoteInteractor
) {

}