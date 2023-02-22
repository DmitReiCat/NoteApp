package com.example.mynoteapp.feature_note.presentation.notes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mynoteapp.feature_note.domain.model.Note
import com.example.mynoteapp.ui.theme.MyApplicationTheme

@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 10.dp,
    cutCornerSize: Dp = 30.dp,
    onDelete: () -> Unit
) {
    val iconSize = 24.dp

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(Color(note.color))
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
//                .height(64.dp)
                .padding(start = 0.dp, end = 4.dp, top = 6.dp, bottom = 6.dp)
//                .background(Color.Blue)
        ) {
            ListIconButton(onClick = { /*TODO*/ }) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    tint = MaterialTheme.colorScheme.background,
                    imageVector = Icons.Rounded.DragIndicator,
                    contentDescription = "Delete note"
                )
            }
            ListIconButton(onClick = { /*TODO*/ }) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    tint = MaterialTheme.colorScheme.background,
                    imageVector = Icons.Rounded.ExpandMore,
                    contentDescription = "Delete note"
                )
            }
            Spacer(Modifier.width(iconSize / 2))
            Column(
                modifier = Modifier
//                    .background(Color.Green)
                    .weight(1f)
            ) {
                Text(
                    text = note.title + " \n// debug: id= ${ note.id }",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

            }
            ListIconButton(onClick = onDelete) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    tint = MaterialTheme.colorScheme.background,
                    imageVector = Icons.Rounded.RadioButtonUnchecked,
                    contentDescription = "Delete note"
                )
            }
        }
    }
}

@Composable
fun ListIconButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    val iconSize = 24.dp
    IconButton(
        onClick = onClick,
        modifier = Modifier
//                    .background(Color.Red)
            .size(iconSize * 1.5f),
        content = content
    )
}

@Preview
@Composable
fun previewNoteItem() {
    MyApplicationTheme {
        NoteItem(
            note = Note(
                id = null,
                childCount = 2,
                childDoneCount = 1,
                color = Color.Gray.toArgb(),
                content = "My content",
                isDone = false,
                parentId = null,
                timestamp = 120,
                title = "My title"
            ),
            modifier = Modifier.height(150.dp)
        ) { /* do nothing */ }
    }
}