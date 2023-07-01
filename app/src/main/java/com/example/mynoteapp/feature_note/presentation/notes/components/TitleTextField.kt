package com.example.mynoteapp.feature_note.presentation.notes.components

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.Color
import com.example.mynoteapp.feature_note.presentation.notes.NoteTextFieldState

@Composable
fun TitleTextField(
    titleState: NoteTextFieldState,
    onValueChange: (String) -> Unit,
    onFocusChange: (FocusState) -> Unit,
    modifier: Modifier = Modifier
) {
    TransparentHintTextField(
        text = titleState.text,
        hint = titleState.hint,
        onValueChange = onValueChange,
        onFocusChange = onFocusChange,
        isHintVisible = titleState.isHintVisible,
        isSingleLine = true,
        textStyle = MaterialTheme.typography.displaySmall,
        modifier = modifier
    )
}