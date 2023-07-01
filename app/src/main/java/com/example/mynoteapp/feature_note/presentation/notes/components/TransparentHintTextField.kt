package com.example.mynoteapp.feature_note.presentation.notes.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.example.mynoteapp.feature_note.presentation.notes.components.modifiers.clearFocusOnKeyboardDismiss

@Composable
fun TransparentHintTextField(
    text: String,
    hint: String,
    modifier: Modifier = Modifier,
    isHintVisible: Boolean = true,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle,
    isSingleLine: Boolean = false,
    onFocusChange: (FocusState) -> Unit,
) {
    Box(
        modifier = modifier
    ) {
            BasicTextField(
                keyboardOptions = KeyboardOptions(KeyboardCapitalization.Sentences),
                cursorBrush = SolidColor(LocalContentColor.current),
                value = text,
                onValueChange = onValueChange,
                singleLine = isSingleLine,
                textStyle = textStyle.copy(color = LocalContentColor.current),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { onFocusChange(it) }
                    .clearFocusOnKeyboardDismiss() // TODO(make smooth disappear of Selection Handle)
            )
            if (isHintVisible) {
                Text(text = hint, style = textStyle)
            }
    }
}
