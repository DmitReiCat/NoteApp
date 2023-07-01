package com.example.mynoteapp.feature_note.presentation.notes

import android.graphics.Color
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynoteapp.feature_note.domain.model.InvalidNoteException
import com.example.mynoteapp.feature_note.domain.model.Note
import com.example.mynoteapp.feature_note.domain.use_case.NoteUseCases
import com.example.mynoteapp.feature_note.domain.util.NoteOrder
import com.example.mynoteapp.feature_note.presentation.destinations.NotesScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    //TODO(make database calls not from viewModelScope))

    private val navigationIdStack = ArrayDeque<Long>()

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

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

    private val _eventFlow = MutableSharedFlow<UiEvent>(replay = 1)
    val eventFlow = _eventFlow.asSharedFlow()
    private var notesJob: Job? = null

    private var currentNote: Note? = null
    private var recentlyDeletedNote: Note? = null

    val scope = viewModelScope + Dispatchers.Default


    init {
        val args = NotesScreenDestination.argsFrom(savedStateHandle)
        if (args.noteId == -1L) createAndLoadNote()
        else loadNote(args.noteId)
    }
    //TODO(make functions for each event)
    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.Order -> {
                if (state.value.noteOrder::class != event.noteOrder::class ||
                    state.value.noteOrder.orderType != event.noteOrder.orderType
                ) loadSubNotes(currentNote!!.id, event.noteOrder)
            }

            is NotesEvent.DeleteNote -> {
                scope.launch {
                    noteUseCases.deleteNote(event.note)
                    recentlyDeletedNote = event.note
                }
            }

            NotesEvent.RestoreNote -> {
                scope.launch {
                    noteUseCases.saveNote(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                }
            }

            NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSelectionVisible = !state.value.isOrderSelectionVisible
                )
            }

            is NotesEvent.ChangeNoteId -> {
                if (currentNote != null) saveCurrentNote()
                navigationIdStack.addLast(currentNote!!.id!!)
                _state.value = state.value.copy(
                    parentId = currentNote!!.id,
                    isTopLevel = false
                )
                loadNote(event.noteId)
            }

            NotesEvent.OnBackPressed -> {
                if (currentNote != null) saveCurrentNote()
                val nextNoteId = navigationIdStack.removeLast()
                _state.value = state.value.copy(
                    parentId = navigationIdStack.lastOrNull(),
                    isTopLevel = navigationIdStack.size == 0
                )
                loadNote(nextNoteId)
            }
            // TODO(WIP quick edit for notes)
            is NotesEvent.ToggleBottomSheet -> {
                // TODO(rewrite the logic to not mess up with fullscreen edit)
//                if (!state.value.openBottomSheet) {
//                    loadNote(event.noteId)
//                } else currentNoteId = null
//                _state.value = state.value.copy(
//                    openBottomSheet = !state.value.openBottomSheet
//                )
            }

            NotesEvent.CreateNewNote -> {
                saveCurrentNote()
                createAndLoadNote()
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

    private fun loadNote(noteId: Long) {
        scope.launch {
            noteUseCases.getNote(noteId)?.let { note ->
                currentNote = note
                _noteTitle.value = noteTitle.value.copy(
                    text = note.title,
                    isHintVisible = false,
                )
                _noteContent.value = noteContent.value.copy(
                    text = note.content,
                    isHintVisible = note.content.isBlank(),
                )
            }
        }
        loadSubNotes(noteId, NoteOrder.DEFAULT)
    }

    private fun saveCurrentNote() {
        scope.launch {
            try {
                currentNote?.copy(
                    title = noteTitle.value.text,
                    content = noteContent.value.text
                )?.let { noteUseCases.saveNote(it) }
            } catch (e: InvalidNoteException) {
                _eventFlow.emit(
                    UiEvent.ShowSnackBar(
                        message = e.message ?: "Unknown error, couldn't save note"
                    )
                )
            }
        }
    }

    private fun createAndLoadNote() {
        scope.launch {
            val createdId = noteUseCases.createNote(
                Note(
                    title = "",
                    content = "",
                    timestamp = System.currentTimeMillis(),
                    color = Color.WHITE,
                    parentId = currentNote?.id
                )
            )
            currentNote?.id?.let {
                navigationIdStack.addLast(it)
                _state.value = state.value.copy(
                    parentId = it,
                    isTopLevel = false
                )
            }
            loadNote(createdId)
            _eventFlow.emit(UiEvent.RequestTitleFocus)
        }
    }

    //TODO (For future fragmentation)
    private suspend fun getNewNoteId(): Long =
        withContext(scope.coroutineContext) {
            val noteId = noteUseCases.createNote(
                Note(
                    title = "",
                    content = "",
                    timestamp = System.currentTimeMillis(),
                    color = Color.WHITE,
                    parentId = currentNote?.id
                )
            )
            noteId
        }


    private fun Note.addToNavStack() {
        this.id?.let {
            navigationIdStack.addLast(it)
            _state.value = state.value.copy(
                parentId = it,
                isTopLevel = false
            )
        }
    }




    private fun loadSubNotes(noteId: Long?, noteOrder: NoteOrder) {
        notesJob?.cancel()
        notesJob = noteUseCases.getSubNotes(noteOrder, noteId = noteId)
            .onEach { notes ->
                _state.value = state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder,
                )
            }
            .launchIn(scope)
    }

    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
        object RequestTitleFocus : UiEvent()

    }
}