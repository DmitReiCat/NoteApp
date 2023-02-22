package com.example.mynoteapp.feature_note.presentation.notes.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Color
import com.example.mynoteapp.feature_note.presentation.add_edit_note.AddEditNoteEvent
import com.example.mynoteapp.feature_note.presentation.add_edit_note.NoteTextFieldState
import com.example.mynoteapp.feature_note.presentation.add_edit_note.components.TransparentHintTextField

@Composable
fun TitleTextField (
    titleState: NoteTextFieldState,
    onValueChange: (String) -> Unit,
    onFocusChange: (FocusState) -> Unit
) {
    TransparentHintTextField(
        text = titleState.text,
        hint = titleState.hint,
        onValueChange = onValueChange,
        onFocusChange = onFocusChange,
        isHintVisible = titleState.isHintVisible,
        isSingleLine = false,
        textStyle = MaterialTheme.typography.displaySmall.copy(color = Color.White)
    )
}