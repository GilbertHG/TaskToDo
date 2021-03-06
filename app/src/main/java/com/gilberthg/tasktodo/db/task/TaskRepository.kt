package com.gilberthg.tasktodo.db.task

import android.app.Application
import androidx.lifecycle.LiveData
import com.gilberthg.tasktodo.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TaskRepository(application: Application) {
    private val taskDao: TaskDao?
    private var tasks: LiveData<List<Task>>? = null

    init {
        val db = AppDatabase.getInstance(application.applicationContext)
        taskDao = db?.taskDao()
        tasks = taskDao?.getTasks()
    }

    fun getTasks(): LiveData<List<Task>>?{
        return tasks
    }

    fun insert(task: Task) = runBlocking{
        this.launch(Dispatchers.IO){
            taskDao?.insertTask(task)
        }
    }

    fun update(task: Task) = runBlocking{
        this.launch(Dispatchers.IO){
            taskDao?.updateTask(task)
        }
    }

    fun delete(task: Task) = runBlocking {
        this.launch(Dispatchers.IO){
            taskDao?.deleteTask(task)
        }
    }
}