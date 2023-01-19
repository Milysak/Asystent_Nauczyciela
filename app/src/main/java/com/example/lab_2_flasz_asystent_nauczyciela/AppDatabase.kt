package com.example.lab_2_flasz_asystent_nauczyciela

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Students :: class, Subjects :: class, StudentsSubjects :: class, Marks :: class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun studentsDao() : StudentsDao
    abstract fun subjectsDao() : SubjectsDao
    abstract fun studentsSubjectsDao() : StudentsSubjectsDao
    abstract fun marksDao() : MarksDao

    companion object {

        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getDatabase(context : Context): AppDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}