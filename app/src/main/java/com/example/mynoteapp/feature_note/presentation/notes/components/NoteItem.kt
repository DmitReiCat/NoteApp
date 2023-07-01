package com.example.mynoteapp.feature_note.presentation.notes.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DragIndicator
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.example.compose.AppTheme
import com.example.mynoteapp.feature_note.domain.model.Note

@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 10.dp,
    cutCornerSize: Dp = 30.dp,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    onDelete: () -> Unit

) {
    val iconSize = 24.dp

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius)),
        tonalElevation = 5.dp
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
            NoteIconButton(onClick = { /*TODO*/ }) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    tint = contentColor,
                    imageVector = Icons.Rounded.ExpandMore,
                    contentDescription = "Delete note"
                )
            }
//            Spacer(Modifier.width(iconSize / 2))
            Column(
                modifier = Modifier
//                    .background(Color.Green)
                    .weight(1f)
            ) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "// debug: id= ${note.id}",
                    style = MaterialTheme.typography.titleMedium,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

            }
//            Spacer(Modifier.width(iconSize / 2))
            NoteIconButton(onClick = onDelete) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    tint = contentColor,
                    imageVector = Icons.Rounded.RadioButtonUnchecked,
                    contentDescription = "Delete note"
                )
            }
        }
    }
}

@Composable
fun NoteIconButton(
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    val iconSize = 24.dp
    IconButton(
        onClick = onClick,
        modifier = Modifier
//                    .background(Color.Red)
//            .size(iconSize * 1.5f),
            .size(iconSize * 2.0f),
        content = content
    )
}

@Preview
@Composable
fun previewNoteItem() {
    AppTheme {
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
            modifier = Modifier.height(150.dp), // TODO(What the fuck)
            onDelete = { Unit }
        )
    }
}