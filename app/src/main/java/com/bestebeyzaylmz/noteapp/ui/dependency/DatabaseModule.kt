package com.bestebeyzaylmz.noteapp.ui.dependency

import android.content.Context
import androidx.room.Room
import com.bestebeyzaylmz.noteapp.data.database.NoteDatabase
import com.bestebeyzaylmz.noteapp.data.database.NoteDatabaseDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context): NoteDatabase {
        return Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            "note_database"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Singleton
    @Provides
    fun proviideNoteDao(noteDatabase: NoteDatabase): NoteDatabaseDAO {
        return noteDatabase.noteDAO()
    }
}