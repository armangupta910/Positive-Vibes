package com.example.positivevibes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "saved_quotes")
data class SavedQuote(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "quote") val quote: String,
    @ColumnInfo(name = "author") val author: String
)