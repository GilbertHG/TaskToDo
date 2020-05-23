package com.gilberthg.tasktodo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gilberthg.tasktodo.db.task.Task
import com.gilberthg.tasktodo.db.task.TaskDao

@Database(entities = [Task::class], exportSchema = false, version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object{
        private const val DB_NAME = "TASK_DB"
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME).build()
                }
            }

            return instance
        }
    }

}