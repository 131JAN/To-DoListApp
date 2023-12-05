package com.dicoding.todoapp.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.R
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.utils.TASK_ID
import kotlinx.android.synthetic.main.activity_task_detail.*

class DetailTaskActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailTaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        //TODO 11 : Show detail task and implement delete action
        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory).get(DetailTaskViewModel::class.java)

        // Get task id from intent extra
        val taskId = intent.getIntExtra(TASK_ID, -1)

        // Retrieve task details from database and display in UI
        viewModel.getTaskById(taskId).observe(this, { task ->
            if (task != null) {
                displayTaskDetails(task)
            }
        })

        // Delete task when delete button clicked
        btnDelete.setOnClickListener {
            viewModel.deleteTask(taskId)
            finish()
        }
    }

    private fun displayTaskDetails(task: Task) {
        tvTitle.text = task.title
        tvDescription.text = task.description
        tvDueDate.text = DateConverter.convertMillisToString(task.dueDateMillis)
    }
}