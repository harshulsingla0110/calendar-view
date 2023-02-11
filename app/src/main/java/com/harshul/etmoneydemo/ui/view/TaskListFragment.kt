package com.harshul.etmoneydemo.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.harshul.etmoneydemo.data.models.Task
import com.harshul.etmoneydemo.databinding.FragmentTaskListBinding
import com.harshul.etmoneydemo.ui.adapter.TaskClickListener
import com.harshul.etmoneydemo.ui.adapter.TaskRecyclerViewAdapter
import com.harshul.etmoneydemo.ui.viewmodels.TaskViewModel

class TaskListFragment : Fragment(), TaskClickListener {

    private lateinit var binding: FragmentTaskListBinding
    private lateinit var mainViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity())[TaskViewModel::class.java]

        val recyclerViewAdapter = TaskRecyclerViewAdapter(listOf(), this)
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = recyclerViewAdapter
        }

        mainViewModel.getAllTasks().observe(requireActivity()) { list ->
            mainViewModel.month.observe(requireActivity()) { month ->
                val temp = list.filter { it.month == month }
                recyclerViewAdapter.setList(temp)
            }
        }
    }

    override fun deleteTask(task: Task) {
        mainViewModel.deleteTask(task)
        Toast.makeText(requireContext(), "Task Deleted Successfully", Toast.LENGTH_SHORT).show()
    }

}