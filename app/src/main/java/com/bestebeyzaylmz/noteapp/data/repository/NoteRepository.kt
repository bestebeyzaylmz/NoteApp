package com.bestebeyzaylmz.noteapp.data.repository

import com.bestebeyzaylmz.noteapp.data.database.NoteDatabaseDAO
import com.bestebeyzaylmz.noteapp.data.database.NoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

//viewmodel ile db arasındaki bağlantıyı sağlar
class NoteRepository @Inject constructor(
    private val noteDatabaseDAO: NoteDatabaseDAO
) {
    fun getAllNotes() = noteDatabaseDAO.getAllNotes()
        .flowOn(Dispatchers.IO) //IO: arkaplanda yapıldgını belirtir.
        .conflate() //son değeri alır

    suspend fun getNoteItem(selectedNoteId: String) = noteDatabaseDAO.getNoteItem(selectedNoteId)
    suspend fun addNoteItem(addNote: NoteEntity) = noteDatabaseDAO.insertNoteItem(addNote)
    suspend fun searchNotes(searchText: String) = noteDatabaseDAO.searchNotes(searchText)
    suspend fun updateNoteItem(updateNote: NoteEntity) = noteDatabaseDAO.updateNoteItem(updateNote)
    suspend fun deleteAllNotes() = noteDatabaseDAO.deleteAllNotes()
    suspend fun deleteNoteItem(deleteNote: NoteEntity) = noteDatabaseDAO.deleteNoteItem(deleteNote)
}