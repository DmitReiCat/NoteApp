package com.example.mynoteapp.feature_note.domain.use_case

data class NoteUseCases(
    val getSubNotes: GetSubNotesInteractor,
    val deleteNote: DeleteNoteInteractor,
    val saveNote: SaveNoteInteractor,
    val getNote: GetNoteInteractor,
    val createNote: CreateNoteInteractor
) {

}