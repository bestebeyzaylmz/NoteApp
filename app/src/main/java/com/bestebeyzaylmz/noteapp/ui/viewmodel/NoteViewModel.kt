package com.bestebeyzaylmz.noteapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bestebeyzaylmz.noteapp.data.database.NoteEntity
import com.bestebeyzaylmz.noteapp.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val noteRepository: NoteRepository
) : ViewModel() {

    private val noteList = MutableStateFlow<List<NoteEntity>>(emptyList())
    val noteListState = noteList.asStateFlow()

    init {
        getAllNotes()
    }

    fun getAllNotes() = viewModelScope.launch(Dispatchers.IO) {
        noteRepository.getAllNotes().distinctUntilChanged().collect {
            noteList.value = it
        }
    }

    fun addNote(noteEntity: NoteEntity) = viewModelScope.launch {
        noteRepository.addNoteItem(noteEntity)
    }


    //viewModelScope async çağrım yapar
    fun searchNote(noteText: String) = viewModelScope.launch {
        noteList.value = noteRepository.searchNotes(noteText)
    }


    fun updateNote(noteEntity: NoteEntity) =
        viewModelScope.launch { noteRepository.updateNoteItem(noteEntity) }

    fun removeNote(noteEntity: NoteEntity) =
        viewModelScope.launch { noteRepository.deleteNoteItem(noteEntity) }

    fun removeAllNotes() = viewModelScope.launch { noteRepository.deleteAllNotes() }
}