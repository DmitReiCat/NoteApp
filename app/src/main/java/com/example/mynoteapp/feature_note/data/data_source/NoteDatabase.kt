package com.example.mynoteapp.feature_note.data.data_source

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mynoteapp.feature_note.domain.model.Note

@Database(
    version = 2,
    entities = [Note::class],
    autoMigrations = [
        AutoMigration (from = 1, to = 2)
    ],
)
abstract class NoteDatabase: RoomDatabase() {

    abstract val noteDao: NoteDao

    companion object {

        const val DATABASE_NAME = "notes_db"
    }
}