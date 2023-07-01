package com.example.mynoteapp.feature_note.presentation.notes

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mynoteapp.feature_note.presentation.notes.components.ContentTextField
import com.example.mynoteapp.feature_note.presentation.notes.components.NoteItem
import com.example.mynoteapp.feature_note.presentation.notes.components.OrderSection
import com.example.mynoteapp.feature_note.presentation.notes.components.TitleTextField
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class NotesScreenNavArgs(
    val noteId: Long,
    // TODO(Later will be project ID)
    val parentId: Long
)

@Destination(navArgsDelegate = NotesScreenNavArgs::class)
@Composable
fun NotesScreen(
    navigator: DestinationsNavigator,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val focusManager = LocalFocusManager.current
    val titleFocusRequester = remember { FocusRequester() }

    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value


    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is NotesViewModel.UiEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }

                NotesViewModel.UiEvent.RequestTitleFocus -> {
                    titleFocusRequester.requestFocus()
                }
            }
        }
    }


    BackHandler {
        focusManager.clearFocus()
        if (!viewModel.state.value.isTopLevel) viewModel.onEvent(NotesEvent.OnBackPressed)
        else viewModel.onEvent(AddEditNoteEvent.SaveNote).also { navigator.navigateUp() }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    focusManager.clearFocus()
                    viewModel.onEvent(NotesEvent.CreateNewNote)
                },
                shape = MaterialTheme.shapes.large,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Note")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                //TODO(Delete debug property)
                Text(
                    text = "Your note    parent=${viewModel.state.value.parentId}",
                    style = MaterialTheme.typography.bodyLarge
                )
                //TODO(Will be redone in the future as sort and filter menus)
                IconButton(onClick = { viewModel.onEvent(NotesEvent.ToggleOrderSection) }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Sort"
                    )
                }
            }

            //TODO(Will be redone in the future as sort and filter menus)
            AnimatedVisibility(
                visible = state.isOrderSelectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                OrderSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    noteOrder = state.noteOrder,
                    onOrderChange = {
                        viewModel.onEvent(NotesEvent.Order(it))
                    }
                )
            }

            TitleTextField(
                titleState = titleState,
                onValueChange = { viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it)) },
                onFocusChange = { viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it)) },
                modifier = Modifier.focusRequester(titleFocusRequester)
            )
            Spacer(modifier = Modifier.height(16.dp))
            ContentTextField(
                contentState = contentState,
                onValueChange = { viewModel.onEvent(AddEditNoteEvent.EnteredContent(it)) },
                onFocusChange = { viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(it)) },
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.notes) { note ->
                    NoteItem(
                        note = note,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                focusManager.clearFocus()
                                viewModel.onEvent(NotesEvent.ChangeNoteId(note.id!!))
                            },
                        onDelete = {
                            viewModel.onEvent(NotesEvent.DeleteNote(note))
                            scope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = "Note deleted",
                                    actionLabel = "Undo"
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    viewModel.onEvent(NotesEvent.RestoreNote)
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

