package com.example.lab_2_flasz_asystent_nauczyciela

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StudentsSubjectsDao {
    @Query("SELECT * FROM studentsSubjects_table")
    fun getAll(): MutableList<StudentsSubjects>

    @Insert
    suspend fun insert(studentsSubjects: StudentsSubjects)

    @Delete
    suspend fun delete(studentsSubjects: StudentsSubjects)

    @Query("DELETE FROM studentsSubjects_table")
    suspend fun deleteAll()
}