package com.gilberthg.tasktodo.ui

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gilberthg.tasktodo.R
import com.gilberthg.tasktodo.db.task.Task
import com.gilberthg.tasktodo.ui.utility.Commons
import com.gilberthg.tasktodo.ui.utility.ConfirmDialog
import com.gilberthg.tasktodo.ui.utility.FormDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.task_fragment.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskAdapter: TaskAdapter
    private val taskList: ArrayList<Task> = ArrayList<Task>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layoutManager = LinearLayoutManager(this)
        rv.layoutManager = layoutManager

        taskAdapter = TaskAdapter(this){task, _ ->
            val options = resources.getStringArray(R.array.option_item)
            Commons.showSelector(this, "Choose Action", options){_, i->
                when(i){
                    0 -> showDetailDialog(task)
                    1 -> showUpdateDialog(task)
                    2 -> showDeleteDialog(task)
                }
            }
        }

        rv.adapter = taskAdapter
        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        swipe_refresh_layout.setOnRefreshListener {
            refreshData()
        }

        button_add.setOnClickListener{
            showInsertDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        observeData()
    }

    private fun observeData(sortby: String = "dateCreated", keyword: String? = "") {
        taskViewModel.getTasks()?.observe(this, Observer {
            taskList.clear()
            setProgressbarVisibility(false)

            if(it.isEmpty()) setEmptyTextVisibility(true)
            else {
                taskList.addAll(it)
                setEmptyTextVisibility(false)
            }

            taskAdapter.setTaskList(it)
        })

    }

    private fun setEmptyTextVisibility(state: Boolean) {
        if (state) tv_empty.visibility = View.VISIBLE
        else tv_empty.visibility = View.GONE
    }

    private fun setProgressbarVisibility(state: Boolean) {
        if (state) progressbar.visibility = View.VISIBLE
        else progressbar.visibility = View.INVISIBLE
    }

    private fun refreshData(sortby: String = "dateCreated", keyword: String? = "") {
        setProgressbarVisibility(true)
        observeData(sortby, keyword)
        swipe_refresh_layout.isRefreshing = false
        setProgressbarVisibility(false)
    }

    private fun showDeleteDialog(task: Task) {
        val dialogTitle = "Delete"
        val dialogMessage = "Are you sure want to delete this task?"
        val toastMessage = "Data has been deleted successfully"

        ConfirmDialog(this, dialogTitle, dialogMessage) {
            taskViewModel.deleteTask(task)
            Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
        }.show()
    }

    private fun showUpdateDialog(task: Task) {
        val view = LayoutInflater.from(this).inflate(R.layout.task_fragment,null)
        view.input_due_date.setOnClickListener {
            Commons.showDatePickerDialog(this, view.input_due_date)
        }

        view.input_time.setOnClickListener {
            Commons.showTimePickerDialog(this, view.input_time)
        }

        view.input_title.setText(task.title)
        view.input_detail_task.setText(task.note)
        view.input_due_date.setText(task.dueDate)
        view.input_time.setText(task.dueTime)
        view.input_remind_me.isChecked = task.remindMe

        val dialogTitle = "Edit Task"
        val toastMessage = "Task has been updated successfully"
        val failAlertMessage = "Please fill all the required fields"

        FormDialog(this, dialogTitle, view){
            val title = view.input_title.text.toString().trim()
            val note = view.input_detail_task.text.toString()
            val date = view.input_due_date.text.toString().trim()
            val time = view.input_time.text.toString().trim()

            val dateCreated = task.dateCreated
            val remindMe = view.input_remind_me.isChecked

            if (title == "" || date == "" || time == "") {
                AlertDialog.Builder(this).setMessage(failAlertMessage).setCancelable(false)
                    .setPositiveButton("OK") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }.create().show()
            }else{
                val parsedDate = SimpleDateFormat("dd/MM/yy", Locale.US).parse(date) as Date
                val dueDate = Commons.formatDate(parsedDate, "dd/MM/yy")

                val currentDate = Commons.getCurrentDateTime()
                val dateUpdated =Commons.formatDate(currentDate, "dd/MM/yy HH:mm:ss")

                task.title = title
                task.note = note
                task.dateCreated = dateCreated
                task.dateUpdated = dateUpdated
                task.dueDate = dueDate
                task.dueTime = time
                task.remindMe = remindMe

                taskViewModel.updateTask(task)
                Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
            }
        }.show()
    }

    private fun showDetailDialog(task: Task) {
        val title = "Title: ${task.title}"
        val dueDate = "Due date: ${task.dueDate}, ${task.dueTime}"
        val note = "Note: ${task.note}"
        val dateCreated = "Date created: ${task.dateCreated}"
        val dateUpdated = "Date updated: ${task.dateUpdated}"
        val strReminder = if(task.remindMe) "Enabled" else "Disabled"
        val remindMe = "Reminder: $strReminder"

        val strMessage = "$title\n$dueDate\n$note\n\n$dateCreated\n$dateUpdated\n$remindMe"

        AlertDialog.Builder(this).setMessage(strMessage).setCancelable(false)
            .setPositiveButton("OK") { dialogInterface, _ ->
                dialogInterface.cancel()
            }.create().show()
    }

    private fun showInsertDialog() {
        val view = LayoutInflater.from(this).inflate(R.layout.task_fragment,null)

        view.input_due_date.setOnClickListener{
            Commons.showDatePickerDialog(this, view.input_due_date)
        }
        view.input_time.setOnClickListener {
            Commons.showTimePickerDialog(this, view.input_time)
        }

        val dialogTitle = "Add Task"
        val toastMessage = "Task has been added successfully"
        val failAlertMessage = "Please fill all required fields"

        FormDialog(this,dialogTitle,view){
            val title = view.input_title.text.toString().trim()
            val note = view.input_detail_task.text.toString()
            val date = view.input_due_date.text.toString().trim()
            val time = view.input_time.text.toString().trim()

            val remindMe = view.input_remind_me.isChecked

            if(title == "" || date == "" || time == ""){
                AlertDialog.Builder(this).setMessage(failAlertMessage).setCancelable(false)
                    .setPositiveButton("OK") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }.create().show()
            }else{
                val parsedDate = SimpleDateFormat("dd/MM/yy", Locale.US).parse(date) as Date
                val dueDate = Commons.formatDate(parsedDate, "dd/MM/yy")

                val currentDate = Commons.getCurrentDateTime()
                val dateCreated =Commons.formatDate(currentDate, "dd/MM/yy HH:mm:ss")

                val task = Task(
                    title = title,
                    note = note,
                    dateCreated = dateCreated,
                    dateUpdated = dateCreated,
                    dueDate = dueDate,
                    dueTime = time,
                    remindMe = remindMe
                )

                taskViewModel.insertTask(task)
                Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
            }
        }.show()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
