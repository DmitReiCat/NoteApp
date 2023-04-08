package com.example.mynoteapp.feature_note.presentation.notes

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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

            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f) // TODO(dont need this modifier for future design)
            ) {
                Column {
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
                }
            }

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

