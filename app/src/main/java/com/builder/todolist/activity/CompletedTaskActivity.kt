package com.builder.todolist.activity

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.builder.todolist.R
import com.builder.todolist.adapter.TaskAdapter
import com.builder.todolist.database.TaskDatabase
import com.builder.todolist.model.TaskEntity
import kotlinx.android.synthetic.main.activity_completed_task.*

class CompletedTaskActivity : AppCompatActivity() {
    private lateinit var allListTask: List<TaskEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_task)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        completed_recycler_view.layoutManager = layoutManager

        SetTaskData().execute()

        btn_back_completed.setOnClickListener {
            finish()
        }

        complete_img_delete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_all_task))
                .setMessage(getString(R.string.are_you_sure_to_delete_all_completed_task))
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    DeleteAllData().execute()
                    SetTaskData().execute()
                }
                .setNegativeButton(getString(R.string.no), null)
                .show()
        }
    }

    @SuppressLint("StaticFieldLeak")
    internal inner class SetTaskData : AsyncTask<Void, Void, Unit>() {
        override fun doInBackground(vararg p0: Void?) {
            return getAllTask()
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)

            val adapter = TaskAdapter(this@CompletedTaskActivity, allListTask)
            completed_recycler_view.adapter = adapter

            if (allListTask.isEmpty()) {
                completed_recycler_view.visibility = View.GONE
                completed_no_task.visibility = View.VISIBLE
                complete_img_delete.visibility  = View.GONE
            } else {
                completed_recycler_view.visibility = View.VISIBLE
                completed_no_task.visibility = View.GONE
                complete_img_delete.visibility = View.VISIBLE
            }
        }

        override fun onPreExecute() {
            super.onPreExecute()
            completed_no_task.visibility = View.VISIBLE
            completed_recycler_view.visibility = View.GONE
            complete_img_delete.visibility = View.GONE
        }
    }

    @SuppressLint("StaticFieldLeak")
    internal inner class DeleteAllData : AsyncTask<Void, Void, Unit>() {
        override fun doInBackground(vararg p0: Void?) {
            return deleteAllTask()
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)

            val adapter = TaskAdapter(this@CompletedTaskActivity, allListTask)
            completed_recycler_view.adapter = adapter

            if (allListTask.isEmpty()) {
                completed_recycler_view.visibility = View.GONE
                completed_no_task.visibility = View.VISIBLE
                complete_img_delete.visibility  = View.GONE
            } else {
                completed_recycler_view.visibility = View.VISIBLE
                completed_no_task.visibility = View.GONE
                complete_img_delete.visibility = View.VISIBLE
            }
        }

        override fun onPreExecute() {
            super.onPreExecute()
            completed_no_task.visibility = View.VISIBLE
            completed_recycler_view.visibility = View.GONE
            complete_img_delete.visibility = View.GONE
        }
    }

    private fun getAllTask() {
        val database = Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java,
            "TaskDB"
        ).build()

        allListTask = database.taskDao().getFinishedTasks()
    }

    private fun deleteAllTask() {
        val database = Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java,
            "TaskDB"
        ).build()

        database.taskDao().deleteDoneTask(allListTask)
    }
}
