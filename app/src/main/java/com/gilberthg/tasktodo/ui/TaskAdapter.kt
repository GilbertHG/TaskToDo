package com.gilberthg.tasktodo.ui

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gilberthg.tasktodo.db.task.Task

class TaskAdapter(private val context: Context?,private val listener:(Task,Int)->Unit):RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    private var taskList = listOf<Task>()
    fun setTaskList(taskList: List<Task>){
        this.taskList = taskList
        notifyDataSetChanged()
        Log.d("cek",taskList.toString())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    class TaskViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }
}