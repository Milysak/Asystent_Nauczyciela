package com.example.lab_2_flasz_asystent_nauczyciela

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MarksDao {
    @Query("SELECT * FROM marks_table")
    fun getAll(): MutableList<Marks>

    @Insert
    suspend fun insert(marks: Marks)

    @Delete
    suspend fun delete(marks: Marks)

    @Query("DELETE FROM marks_table")
    suspend fun deleteAll()
}