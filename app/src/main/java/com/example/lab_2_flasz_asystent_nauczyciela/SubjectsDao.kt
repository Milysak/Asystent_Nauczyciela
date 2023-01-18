package com.example.lab_2_flasz_asystent_nauczyciela

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SubjectsDao {
    @Query("SELECT * FROM subjects_table")
    fun getAll(): MutableList<Subjects>

    @Insert
    suspend fun insert(subjects: Subjects)

    @Delete
    suspend fun delete(subjects: Subjects)

    @Query("DELETE FROM subjects_table")
    suspend fun deleteAll()
}