package com.gilberthg.tasktodo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gilberthg.tasktodo.R
import com.gilberthg.tasktodo.db.task.Task
import com.gilberthg.tasktodo.ui.utility.Commons
import kotlinx.android.synthetic.main.activity_main.*

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

    private fun refreshData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun showDeleteDialog(task: Task) {

    }

    private fun showUpdateDialog(task: Task) {

    }

    private fun showDetailDialog(task: Task) {

    }

    private fun showInsertDialog() {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
