package com.example.lab_2_flasz_asystent_nauczyciela

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "marks_table")
data class Marks(
    @PrimaryKey(autoGenerate = true) var _id: Int?,
    @ColumnInfo(name = "subject_name") var subjectName : String,
    @ColumnInfo(name = "album_number") var albumNumber : Int,
    @ColumnInfo(name = "mark_name") var markName : String,
    @ColumnInfo(name = "mark_value") var markValue : String
) {
    @Ignore
    constructor(subjectName: String, albumNumber: Int, markName: String, markValue: String) :
            this (null, subjectName, albumNumber, markName, markValue)

    override fun toString(): String {
        return " $subjectName \n$markValue"
    }
}