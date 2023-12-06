package com.dicoding.todoapp.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.R
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.utils.DateConverter
import com.dicoding.todoapp.utils.TASK_ID
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.MainScope

class DetailTaskActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailTaskViewModel

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        //TODO 11 : Show detail task and implement delete action
        val factory = ViewModelFactory.getInstance(application, MainScope())
        viewModel = ViewModelProvider(this, factory).get(DetailTaskViewModel::class.java)

        // Get task id from intent extra
        val taskId = intent.getIntExtra(TASK_ID, -1)

        // Retrieve task details from database and display in UI
        viewModel.task.observe(this) { task ->
            if (task != null) {
                displayTaskDetails(task)
            }
        }

        // Delete task when delete button clicked
        val btnDelete = findViewById<FloatingActionButton>(R.id.btn_delete_task)
        btnDelete.setOnClickListener {
            viewModel.deleteTask()
            finish()
        }
    }

    val tvTitle = findViewById<TextView>(R.id.detail_ed_title)
    val tvDescription = findViewById<TextView>(R.id.detail_ed_description)
    val tvDueDate = findViewById<TextView>(R.id.detail_ed_due_date)
    private fun displayTaskDetails(task: Task) {
        tvTitle.text = task.title
        tvDescription.text = task.description
        tvDueDate.text = DateConverter.convertMillisToString(task.dueDate)
    }
}