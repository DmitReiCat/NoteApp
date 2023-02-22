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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mynoteapp.feature_note.presentation.add_edit_note.AddEditNoteEvent
import com.example.mynoteapp.feature_note.presentation.add_edit_note.components.TransparentHintTextField
import com.example.mynoteapp.feature_note.presentation.destinations.NotesScreenDestination
import com.example.mynoteapp.feature_note.presentation.notes.components.ContentTextField
import com.example.mynoteapp.feature_note.presentation.notes.components.NoteItem
import com.example.mynoteapp.feature_note.presentation.notes.components.OrderSection
import com.example.mynoteapp.feature_note.presentation.notes.components.TitleTextField
import com.example.mynoteapp.feature_note.presentation.util.Screen
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
data class NotesScreenNavArgs(
    val noteId: Int,
    val parentId: Int
)

@Destination(navArgsDelegate = NotesScreenNavArgs::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    navigator: DestinationsNavigator,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is NotesViewModel.UiEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

//    BackHandler() {
//        viewModel.onEvent(NotesEvent.OnBackPressed)
//    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
//                    viewModel.onEvent(NotesEvent.ChangeParentId(-1))
//                    navController.navigate(Screen.AddEditNote.route)
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
                Text(
                    text = "Your note    parent=${viewModel.state.value.parentId}",
                    style = MaterialTheme.typography.bodyLarge
                )
                IconButton(onClick = { viewModel.onEvent(NotesEvent.ToggleOrderSection) }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Sort"
                    )
                }
            }
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
                    .fillMaxHeight(0.2f)
            ) {

                val titleState = viewModel.noteTitle.value
                val contentState = viewModel.noteContent.value

                Column {
                    TitleTextField(
                        titleState = titleState,
                        onValueChange = { viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it)) },
                        onFocusChange = { viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it)) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ContentTextField(
                        contentState = contentState,
                        onValueChange = { viewModel.onEvent(AddEditNoteEvent.EnteredContent(it)) },
                        onFocusChange = {
                            viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(it))
                        }
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
                                       navigator.navigate(NotesScreenDestination(note.id!!, note.parentId!!))
//                                viewModel.onEvent(NotesEvent.ChangeParentId(note.id!!))
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

