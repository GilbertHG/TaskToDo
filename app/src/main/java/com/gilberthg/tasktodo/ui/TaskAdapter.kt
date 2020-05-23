package com.gilberthg.tasktodo.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gilberthg.tasktodo.R
import com.gilberthg.tasktodo.db.task.Task
import kotlinx.android.synthetic.main.item_task.view.*

class TaskAdapter(private val context: Context?,private val listener:(Task,Int)->Unit):RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    private var taskList = listOf<Task>()
    fun setTaskList(taskList: List<Task>){
        this.taskList = taskList
        notifyDataSetChanged()
        Log.d("cek",taskList.toString())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder =
        TaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_task,parent,false))


    override fun getItemCount(): Int = taskList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int){
        if(context!=null){
            holder.bindItem(taskList[position],listener)
        }
    }

    class TaskViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindItem(task: Task, listener: (Task, Int) -> Unit){
            val date = if(task.dateUpdated != task.dateCreated) "Updated at ${task.dateUpdated}" else "Created at ${task.dateCreated}"
            val dueDate = "Due ${task.dueDate},${task.dueTime}"

            itemView.tv_title.text = task.title
            itemView.tv_detail.text = task.note
            itemView.tv_due_date.text = dueDate
            itemView.tv_created_date.text = date

            itemView.setOnClickListener{
                listener(task, layoutPosition)
            }
        }
    }
}