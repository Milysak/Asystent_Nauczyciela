package com.example.lab_2_flasz_asystent_nauczyciela

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "studentsSubjects_table")
data class StudentsSubjects(
    @PrimaryKey(autoGenerate = true) var _id: Int?,
    @ColumnInfo(name = "subject_name") var subjectName : String,
    @ColumnInfo(name = "album_number") var albumNumber : Int,
) {
    @Ignore
    constructor(subjectName: String, albumNumber: Int) :
            this (null, subjectName, albumNumber)
}