package com.gilberthg.tasktodo.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gilberthg.tasktodo.R
import com.gilberthg.tasktodo.db.task.Task
import com.gilberthg.tasktodo.ui.utility.Commons
import kotlinx.android.synthetic.main.item_task.view.*
import java.text.SimpleDateFormat
import java.util.*

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
            val parsedDateCreated = SimpleDateFormat("dd/MM/yy", Locale.US).parse(task.dateCreated) as Date
            val dateCreated = Commons.formatDate(parsedDateCreated, "dd MMM yyyy")

            val parsedDateUpdated = SimpleDateFormat("dd/MM/yy", Locale.US).parse(task.dateCreated) as Date
            val dateUpdated = Commons.formatDate(parsedDateUpdated, "dd MMM yyyy")

            val date = if (task.dateUpdated != task.dateCreated) "Updated at $dateUpdated" else "Created at $dateCreated"

            val parsedDate = SimpleDateFormat("dd/MM/yy", Locale.US).parse(task.dueDate) as Date
            val dueDate = Commons.formatDate(parsedDate, "dd MMM yyyy")

            val dueDateTime = "Due ${dueDate}, ${task.dueTime}"

            itemView.tv_title.text = task.title
            itemView.tv_detail.text = task.note
            itemView.tv_due_date.text = dueDateTime 
            itemView.tv_created_date.text = date

            itemView.setOnClickListener{
                listener(task, layoutPosition)
            }
        }
    }
}