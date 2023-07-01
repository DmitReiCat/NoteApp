package com.example.mynoteapp.feature_note.presentation.notes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mynoteapp.feature_note.presentation.notes.NoteTextFieldState

@Composable
fun ContentTextField(
    contentState: NoteTextFieldState,
    onValueChange: (String) -> Unit,
    onFocusChange: (FocusState) -> Unit,
    cornerRadius: Dp = 10.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(cornerRadius))
        ,

    ) {
        Box (modifier.padding(cornerRadius)) {
            TransparentHintTextField(
                text = contentState.text,
                hint = contentState.hint,
                onValueChange = onValueChange,
                onFocusChange = onFocusChange,
                isHintVisible = contentState.isHintVisible,
                textStyle = MaterialTheme.typography.bodyMedium,
                modifier = modifier,
            )
        }
    }
}