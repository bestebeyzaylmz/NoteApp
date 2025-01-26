package com.bestebeyzaylmz.noteapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDatabaseDAO {
    @Query("SELECT * FROM notes_table")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes_table WHERE note_id = :selectedNoteId")
    suspend fun getNoteItem(selectedNoteId: String): NoteEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNoteItem(newNote: NoteEntity)

    @Query("SELECT * FROM notes_table WHERE note_title LIKE '%' || :searchText || '%' OR note_subtitle LIKE '%' || :searchText")
    suspend fun searchNotes(searchText: String): List<NoteEntity>

    @Update
    suspend fun updateNoteItem(note: NoteEntity)

    @Query("DELETE FROM notes_table")
    suspend fun deleteAllNotes()

    @Delete()
    suspend fun deleteNoteItem(note: NoteEntity)
}