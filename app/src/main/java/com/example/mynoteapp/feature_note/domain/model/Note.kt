package com.example.mynoteapp.feature_note.domain.model

import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long,
    @ColumnInfo(name = "color")
    val color: Int,

    @ColumnInfo(name = "parentId")
    val parentId: Long? = null,
    @ColumnInfo(name = "childCount", defaultValue = "0")
    val childCount: Int = 0,
    @ColumnInfo(name = "childDoneCount", defaultValue = "0")
    val childDoneCount: Int = 0,
    @ColumnInfo(name = "isDone")
    val isDone: Boolean? = false,
    @PrimaryKey @ColumnInfo(name = "id")
    val id: Long? = null
) {
    companion object {
        //TODO(remove )
        val noteColors = listOf<Color>()

        fun getEmptyNote() = Note(
            title = "",
            content = "",
            timestamp = System.currentTimeMillis(),
            color = Color.WHITE
        )
    }
}

class InvalidNoteException(message: String) : Exception(message)
