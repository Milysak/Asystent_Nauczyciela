package com.example.lab_2_flasz_asystent_nauczyciela

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "students_table")
data class Students(
    @PrimaryKey(autoGenerate = true) var _id: Int?,
    @ColumnInfo(name = "name_surname") var nameSurname : String,
    @ColumnInfo(name = "album_number") var albumNumber : Int
) {
    @Ignore
    constructor(nameSurname: String, albumNumber: Int) : this (null, nameSurname, albumNumber)
}