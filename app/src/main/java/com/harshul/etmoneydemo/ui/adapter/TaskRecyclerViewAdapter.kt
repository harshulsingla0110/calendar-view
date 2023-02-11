package com.harshul.etmoneydemo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harshul.etmoneydemo.data.models.Task
import com.harshul.etmoneydemo.databinding.TaskItemBinding
import com.harshul.etmoneydemo.utils.Util

class TaskRecyclerViewAdapter(
    private var taskList: List<Task>,
    private val listener: TaskClickListener
) :
    RecyclerView.Adapter<TaskRecyclerViewAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: TaskItemBinding) : RecyclerView.ViewHolder(binding.root)

    fun setList(newList: List<Task>) {
        taskList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val task = taskList[position]

        with(holder) {
            binding.tvTask.text = task.task
            binding.tvDate.text = Util.formatDate(task.date, "E, dd MMMM yy")

            binding.buttonDelete.setOnClickListener { listener.deleteTask(task) }
        }
    }
}

interface TaskClickListener {
    fun deleteTask(task: Task)
}