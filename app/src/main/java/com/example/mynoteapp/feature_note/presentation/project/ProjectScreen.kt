package com.example.mynoteapp.feature_note.presentation.project

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.mynoteapp.feature_note.presentation.destinations.NotesScreenDestination
import com.example.mynoteapp.feature_note.presentation.notes.components.NoteItem
import com.example.mynoteapp.feature_note.presentation.project.components.OrderSection
import com.example.mynoteapp.feature_note.presentation.util.Screen
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
@RootNavGraph(start = true)
@Destination
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreen(
    navigator: DestinationsNavigator,
    viewModel: ProjectViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
//    val sheetState = rememberSheetState() // todo(BottomSheet)

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
//                    viewModel.onEvent(NotesEvent.ChangeParentId(-1))
                    navigator.navigate(NotesScreenDestination(noteId = -1, parentId = -1))
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
                IconButton(onClick = { viewModel.onEvent(ProjectEvent.ToggleOrderSection) }) {
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
                        viewModel.onEvent(ProjectEvent.Order(it))
                    }
                )
            }

            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f)
            ) {

                    Text(
                        text = "PROJECT TITLE",
                        style = MaterialTheme.typography.displayLarge,
                    )

            }

            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.notes) { note ->
                    NoteItem(
                        note = note,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                       navigator.navigate(NotesScreenDestination(noteId = note.id ?: -1, parentId = note.parentId ?: -1))
//                                navController.navigate(Screen.NotesScreen.route +
//                                        "?noteId=${note.id}")
//                                    .also { Log.d("NOTE_APP", Screen.NotesScreen.route +
//                                        "?noteId=${note.id}") }
                            },
                        onDelete = {
                            viewModel.onEvent(ProjectEvent.DeleteNote(note))
                            scope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = "Note deleted",
                                    actionLabel = "Undo"
                                )
                                if (result == SnackbarResult.ActionPerformed) {
                                    viewModel.onEvent(ProjectEvent.RestoreNote)
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
//            if (state.openBottomSheet) {
//                val titleState = viewModel.noteTitle.value
//                val contentState = viewModel.noteContent.value
//
//                LaunchedEffect(key1 = true) {
//                    viewModel.eventFlow.collectLatest { event ->
//                        when (event) {
//                            is NotesViewModel.UiEvent.ShowSnackBar -> {
//                                snackbarHostState.showSnackbar(
//                                    message = event.message
//                                )
//                            }
//                        }
//                    }
//                }

//                ModalBottomSheet( //TODO(make it not lag)
//                    onDismissRequest = {
////                        viewModel.onEvent(NotesEvent.ToggleBottomSheet)
//                        viewModel.onEvent(NotesEvent.ToggleBottomSheet())
//                    },
//                    sheetState = sheetState,
//                    shape = BottomSheetDefaults.ExpandedShape
//                ) {
//                    TitleTextField(
//                        titleState = titleState,
//                        onValueChange = { viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it)) },
//                        onFocusChange = { viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it)) }
//                    )
//                    Spacer(modifier = Modifier.height(16.dp))
//                    ContentTextField(
//                        contentState = contentState,
//                        onValueChange = { viewModel.onEvent(AddEditNoteEvent.EnteredContent(it)) },
//                        onFocusChange = { viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(it)) }
//                    )
////                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
////                        Button(
////                            // Note: If you provide logic outside of onDismissRequest to remove the sheet,
////                            // you must additionally handle intended state cleanup, if any.
////                            onClick = {
////                                scope.launch { sheetState.hide() }.invokeOnCompletion {
////                                    if (!sheetState.isVisible) {
//////                                        viewModel.onEvent(NotesEvent.ToggleBottomSheet)
////                                        viewModel.onEvent(NotesEvent.ToggleBottomSheet())
////                                    }
////                                }
////                            }
////                        ) {
////                            Text("Hide Bottom Sheet")
////                        }
////                    }
////                    LazyColumn {
////                        items(50) {
////                            ListItem(
////                                headlineText = { Text("Item $it") },
////                                leadingContent = {
////                                    Icon(
////                                        Icons.Default.Favorite,
////                                        contentDescription = "Localized description"
////                                    )
////                                }
////                            )
////                        }
////                    }
//                }
//            }
        }
    }
}