package com.example.mynoteapp.feature_note.presentation.notes.components

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import com.example.mynoteapp.feature_note.presentation.add_edit_note.NoteTextFieldState
import com.example.mynoteapp.feature_note.presentation.add_edit_note.components.TransparentHintTextField

@Composable
fun ContentTextField(
    contentState: NoteTextFieldState,
    onValueChange: (String) -> Unit,
    onFocusChange: (FocusState) -> Unit
) {
    TransparentHintTextField(
        text = contentState.text,
        hint = contentState.hint,
        onValueChange = onValueChange,
        onFocusChange = onFocusChange,
        isHintVisible = contentState.isHintVisible,
        textStyle = MaterialTheme.typography.bodySmall,
        modifier = Modifier.fillMaxHeight()
    )
}