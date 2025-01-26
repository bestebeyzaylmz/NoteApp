package com.bestebeyzaylmz.noteapp.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

@Entity(tableName = "notes_table")
data class NoteEntity(
    @PrimaryKey
    @ColumnInfo("note_id")
    val NoteId: String = UUID.randomUUID().toString(),
    @ColumnInfo("note_title")
    val NoteTitle: String,
    @ColumnInfo("note_subtitle")
    val NoteSubtitle: String,
    @ColumnInfo("note_description")
    val NoteDescription: String,
    @ColumnInfo("note_image")
    val NoteImagePath: String?,
    @ColumnInfo("note_date")
    val NoteEntryDate: String = currentEntryNoteDate()
)

fun currentEntryNoteDate(): String {
    val calender = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return dateFormat.format(calender.time)
}
