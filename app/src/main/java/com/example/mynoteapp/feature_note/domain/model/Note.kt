package com.example.mynoteapp.feature_note.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mynoteapp.ui.theme.*
import java.lang.Exception
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
    val parentId: Int? = null,
    @ColumnInfo(name = "childCount", defaultValue = "0")
    val childCount: Int = 0,
    @ColumnInfo(name = "childDoneCount", defaultValue = "0")
    val childDoneCount: Int = 0,
    @ColumnInfo(name = "isDone")
    val isDone: Boolean? = false,
    @PrimaryKey @ColumnInfo(name = "id")
    val id: Int? = null
) {
    companion object {
        val noteColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }
}

class InvalidNoteException(message: String) : Exception(message)
