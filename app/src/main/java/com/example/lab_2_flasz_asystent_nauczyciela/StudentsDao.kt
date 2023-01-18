package com.example.lab_2_flasz_asystent_nauczyciela

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StudentsDao {
    @Query("SELECT * FROM students_table")
    fun getAll(): MutableList<Students>

    @Insert
    suspend fun insert(students: Students)

    @Delete
    suspend fun delete(students: Students)

    @Query("DELETE FROM students_table")
    suspend fun deleteAll()
}