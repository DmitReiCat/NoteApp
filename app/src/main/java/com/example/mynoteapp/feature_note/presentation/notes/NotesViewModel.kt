package com.example.mynoteapp.feature_note.presentation.notes

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynoteapp.feature_note.domain.model.InvalidNoteException
import com.example.mynoteapp.feature_note.domain.model.Note
import com.example.mynoteapp.feature_note.domain.use_case.NoteUseCases
import com.example.mynoteapp.feature_note.domain.util.NoteOrder
import com.example.mynoteapp.feature_note.domain.util.OrderType
import com.example.mynoteapp.feature_note.presentation.add_edit_note.AddEditNoteEvent
import com.example.mynoteapp.feature_note.presentation.add_edit_note.NoteTextFieldState
import com.example.mynoteapp.feature_note.presentation.destinations.NotesScreenDestination
import com.example.mynoteapp.ui.theme.RedOrange
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val parentStack = ArrayDeque<Int>()

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state
    private var recentlyDeletedNote: Note? = null

    private var getNotesJob: Job? = null

    private val _noteTitle = mutableStateOf(
        NoteTextFieldState(
            hint = "Enter title..."
        )
    )
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(
        NoteTextFieldState(
            hint = "Enter content..."
        )
    )
    val noteContent: State<NoteTextFieldState> = _noteContent

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null

    private var currentNote: Note? = null



    init {
        val args = NotesScreenDestination.argsFrom(savedStateHandle)
        if (args.noteId != -1) {
            viewModelScope.launch {
                noteUseCases.getNote(args.noteId)?.also { note ->
                    currentNoteId = note.id
                    currentNote = note
                    _noteTitle.value = noteTitle.value.copy(
                        text = note.title,
                        isHintVisible = false
                    )
                    _noteContent.value = noteContent.value.copy(
                        text = note.content,
                        isHintVisible = note.content.isBlank()
                    )
                }
                getNotes(NoteOrder.Date(OrderType.Descending))
            }
        }
        savedStateHandle.get<Int>("noteId")?.let { noteId ->

        }

    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.Order -> {
                if (state.value.noteOrder::class == event.noteOrder::class &&
                    state.value.noteOrder.orderType == event.noteOrder.orderType
                ) {
                    return
                }
                getNotes(event.noteOrder)
            }
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNote(event.note)
                    recentlyDeletedNote = event.note
                }
            }
            NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCases.addNote(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                }
            }
            NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSelectionVisible = !state.value.isOrderSelectionVisible
                )
            }
            is NotesEvent.ChangeParentId -> {
                if (currentNote != null) saveCurrentNote()
                parentStack.addLast(event.parentId)
                _state.value = state.value.copy(
                    parentId = event.parentId,
                    isTopLevel = false
                )
                currentNoteId = event.parentId
                currentNoteId?.let { loadEditableNote(it) } //TODO()
                getNotes(NoteOrder.Date(OrderType.Descending))
            }
            NotesEvent.OnBackPressed -> {
                if (currentNote != null) saveCurrentNote()
                currentNoteId = parentStack.removeLast()
                _state.value = state.value.copy(
                    parentId = parentStack.last(),
                    isTopLevel = parentStack.size == 1
                )
                currentNoteId?.let { loadEditableNote(it) }
                getNotes(NoteOrder.Date(OrderType.Descending))
            }
            is NotesEvent.ToggleBottomSheet -> {
                if (!state.value.openBottomSheet) {
                    loadEditableNote(event.noteId)
                } else currentNoteId = null
                _state.value = state.value.copy(
                    openBottomSheet = !state.value.openBottomSheet
                )
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.ChangeTitleFocus -> _noteTitle.value = noteTitle.value.copy(
                isHintVisible = !event.focusState.isFocused && noteTitle.value.text.isBlank()
            )
            is AddEditNoteEvent.EnteredTitle -> _noteTitle.value = noteTitle.value.copy(
                text = event.value
            )

            is AddEditNoteEvent.ChangeContentFocus -> _noteContent.value = noteContent.value.copy(
                isHintVisible = !event.focusState.isFocused && noteContent.value.text.isBlank()
            )
            is AddEditNoteEvent.EnteredContent -> _noteContent.value = noteContent.value.copy(
                text = event.value
            )

            AddEditNoteEvent.SaveNote -> {
                saveCurrentNote()
            }
        }
    }

    private fun loadEditableNote(noteId: Int) {
        if (noteId != -1) {
            viewModelScope.launch {
                noteUseCases.getNote(noteId)?.also { note ->
                    currentNoteId = note.id
                    currentNote = note
                    _noteTitle.value = noteTitle.value.copy(
                        text = note.title,
                        isHintVisible = false
                    )
                    _noteContent.value = noteContent.value.copy(
                        text = note.content,
                        isHintVisible = false
                    )
                }
            }
        } else {
            currentNoteId = null
            currentNote = null
            _noteTitle.value = noteTitle.value.copy(
                isHintVisible = true
            )
            _noteContent.value = noteContent.value.copy(
                isHintVisible = true
            )
        }
    }

    private fun saveCurrentNote() {
        viewModelScope.launch {
            try {
                currentNote?.copy(
                    title = noteTitle.value.text,
                    content = noteContent.value.text
                )?.let { noteUseCases.addNote(it) }
            } catch (e: InvalidNoteException) {
                _eventFlow.emit(
                    UiEvent.ShowSnackBar(
                        message = e.message ?: "Unknown error, couldn't save note"
                    )
                )
            }
        }
    }

    private fun addNewNote() {
        viewModelScope.launch {
            try {
                noteUseCases.addNote(
                    Note(
                        title = noteTitle.value.text,
                        content = noteContent.value.text,
                        timestamp = System.currentTimeMillis(),
                        color = RedOrange.toArgb(),
                        id = currentNoteId,
                        parentId = state.value.parentId
                    )
                )
            } catch (e: InvalidNoteException) {
                _eventFlow.emit(
                    UiEvent.ShowSnackBar(
                        message = e.message ?: "Unknown error, couldn't save note"
                    )
                )
            }
        }
    }


    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotes(noteOrder, parentId = currentNoteId)
            .onEach { notes ->
                Log.d("NOTE_APP", "got in vm: $notes")
                _state.value = state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder,
                )
            }
            .launchIn(viewModelScope)
    }

    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()

    }

}