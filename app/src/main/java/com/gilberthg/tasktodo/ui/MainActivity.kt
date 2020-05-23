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
                    1 -> showDetailDialog(task)
                    2 -> showUpdateDialog(task)
                    3 -> showDeleteDialog(task)
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

    }

    private fun showUpdateDialog(task: Task) {

    }

    private fun showDetailDialog(task: Task) {

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
            val note = view.input_detail_task.text.toString().trim()
            val date = view.input_due_date.text.toString().trim()
            val time = view.input_time.text.toString().trim()

            val remindMe = true

            if(title == "" || date == "" || time == ""){
                AlertDialog.Builder(this).setMessage(failAlertMessage).setCancelable(false)
                    .setPositiveButton("OK") { dialogInterface, _ ->
                        dialogInterface.cancel()
                    }.create().show()
            }else{
                val parsedDate = SimpleDateFormat("dd/MM/yy", Locale.US).parse(date) as Date
                val dueDate = parsedDate.toString("dd MMM yyy")

                val dateCreated = Commons.getCurrentDateTime().toString("dd MMM yyyy")

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

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
