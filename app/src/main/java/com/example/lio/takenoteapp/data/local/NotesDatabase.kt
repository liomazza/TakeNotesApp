package com.example.lio.takenoteapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.lio.takenoteapp.data.local.entities.LocallyDeletedNoteID
import com.example.lio.takenoteapp.data.local.entities.Note

@Database(
    entities = [Note::class, LocallyDeletedNoteID::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
}