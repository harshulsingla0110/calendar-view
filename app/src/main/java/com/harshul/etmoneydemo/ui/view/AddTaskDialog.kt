package com.harshul.etmoneydemo.ui.view

import android.app.ActionBar
import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.view.Display
import android.view.View
import android.view.Window
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.harshul.etmoneydemo.R
import com.harshul.etmoneydemo.data.models.Task
import com.harshul.etmoneydemo.databinding.FragmentAddTaskBinding
import com.harshul.etmoneydemo.ui.viewmodels.TaskViewModel
import com.harshul.etmoneydemo.utils.Util
import java.util.*

class AddTaskDialog : DialogFragment() {

    private val mainViewModel by lazy { ViewModelProvider(requireActivity())[TaskViewModel::class.java] }
    private var month: Int = 0
    private var toUpdate: Boolean = false
    private val calendar: Calendar = Calendar.getInstance()
    private lateinit var updateTask: Task

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogAddNewTask = Dialog(requireContext())
        dialogAddNewTask.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogAddNewTask.setCanceledOnTouchOutside(false)

        val dialogBinding = FragmentAddTaskBinding.inflate(layoutInflater)
        dialogAddNewTask.setContentView(dialogBinding.root)

        val day = mainViewModel.day.value
        if (day != null) {
            month = day.date.monthValue
            calendar.clear()
            calendar.set(day.date.year, day.date.monthValue - 1, day.date.dayOfMonth)
            dialogBinding.etTodoDate.setText(Util.formatDate(calendar.time, "dd MMMM yyyy"))
        } else {
            dialogBinding.datePicker.visibility = View.VISIBLE
            dialogBinding.datePicker.minDate = Calendar.getInstance().time.time
            month = calendar.time.month + 1
            calendar.clear()
            calendar.set(
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            )
            dialogBinding.etTodoDate.setText(Util.formatDate(calendar.time, "dd MMMM yyyy"))

            dialogBinding.datePicker.setOnDateChangedListener { _: DatePicker,
                                                                year: Int, month: Int, dayOfMonth: Int ->
                this.month = month + 1
                calendar.clear()
                calendar.set(year, month, dayOfMonth)
                dialogBinding.etTodoDate.setText(Util.formatDate(calendar.time, "dd MMMM yyyy"))
                getTaskDetails(dialogBinding.etTodo, dialogBinding.labelHeading)
            }
        }
        getTaskDetails(dialogBinding.etTodo, dialogBinding.labelHeading)

        dialogBinding.buttonYes.setOnClickListener {
            val taskTitle = dialogBinding.etTodo.text.toString().trim()
            if (TextUtils.isEmpty(taskTitle)) {
                Toast.makeText(requireContext(), "Enter Task Title", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (toUpdate) {
                updateTask.task = taskTitle
                mainViewModel.updateTask(updateTask)
                Toast.makeText(requireContext(), "Task Updated Successfully", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val task = Task(taskTitle, calendar.time, month)
                mainViewModel.insertTask(task)
                Toast.makeText(requireContext(), "Task Added Successfully", Toast.LENGTH_SHORT)
                    .show()
            }

            mainViewModel.setDay(null)
            dialogAddNewTask.dismiss()
        }

        dialogBinding.buttonCancel.setOnClickListener {
            dialogAddNewTask.dismiss()
            mainViewModel.setDay(null)
        }

        dialogAddNewTask.show()
        val display: Display = requireActivity().windowManager.defaultDisplay
        val width: Int = display.width - 100
        val window: Window? = dialogAddNewTask.window
        window?.setLayout(width, ActionBar.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        return dialogAddNewTask
    }

    private fun getTaskDetails(textView: EditText, textViewHeading: TextView) {
        mainViewModel.getTask(calendar.time).observe(this) { list ->
            if (list.isNotEmpty()) {
                textViewHeading.text = getString(R.string.update_task)
                textView.setText(list[0].task)
                updateTask = list[0]
                toUpdate = true
            } else {
                textViewHeading.text = getString(R.string.add_task)
                textView.setText("")
                toUpdate = false
            }
        }
    }

}