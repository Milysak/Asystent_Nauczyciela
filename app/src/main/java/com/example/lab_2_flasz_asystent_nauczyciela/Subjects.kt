package com.example.lab_2_flasz_asystent_nauczyciela

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "subjects_table")
data class Subjects(
    @PrimaryKey(autoGenerate = true) var _id: Int?,
    @ColumnInfo(name = "subject_name") var subjectName : String,
    @ColumnInfo(name = "day") var day : String,
    @ColumnInfo(name = "hour") var hour : String
) {
    @Ignore
    constructor(subjectName: String, day: String, hour: String) : this (null, subjectName, day, hour)
}