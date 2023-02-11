package com.harshul.etmoneydemo.ui.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.harshul.etmoneydemo.R
import com.harshul.etmoneydemo.databinding.CalendarDayLayoutBinding
import com.harshul.etmoneydemo.databinding.FragmentCalendarViewBinding
import com.harshul.etmoneydemo.ui.viewmodels.TaskViewModel
import com.kizitonwose.calendar.core.*
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.time.*
import java.time.format.TextStyle
import java.util.*

class CalendarViewFragment : Fragment() {

    private lateinit var binding: FragmentCalendarViewBinding
    private lateinit var mainViewModel: TaskViewModel
    private val monthCalendarView: CalendarView get() = binding.exOneCalendar
    private val selectedDates = mutableSetOf<LocalDate>()
    private val today = LocalDate.now()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarViewBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity())[TaskViewModel::class.java]

        binding.buttonCalendar.setOnClickListener {
            if (binding.groupCalender.isVisible) {
                binding.groupCalender.visibility = View.GONE
                binding.buttonCalendar.text = getString(R.string.show_calendar)
            } else {
                binding.groupCalender.visibility = View.VISIBLE
                binding.buttonCalendar.text = getString(R.string.hide_calendar)
            }
        }

        binding.ivPrevMonth.setOnClickListener {
            monthCalendarView.findFirstVisibleMonth()?.let {
                monthCalendarView.smoothScrollToMonth(it.yearMonth.previousMonth)
            }
        }
        binding.ivNextMonth.setOnClickListener {
            monthCalendarView.findFirstVisibleMonth()?.let {
                monthCalendarView.smoothScrollToMonth(it.yearMonth.nextMonth)
            }
        }

        binding.buttonAddTask.setOnClickListener {
            AddTaskDialog().show(
                parentFragmentManager,
                "ADD_TASK_DIALOG"
            )
        }


        val daysOfWeek = daysOfWeek()
        binding.titlesContainer.root.children.map { it as TextView }
            .forEachIndexed { index, textView ->
                val dayOfWeek = daysOfWeek[index]
                val title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                textView.text = title
            }

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(0)  // Adjust as needed
        val endMonth = currentMonth.plusMonths(100)  // Adjust as needed
        setupMonthCalendar(startMonth, endMonth, currentMonth, daysOfWeek)
    }

    private fun setupMonthCalendar(
        startMonth: YearMonth,
        endMonth: YearMonth,
        currentMonth: YearMonth,
        daysOfWeek: List<DayOfWeek>,
    ) {
        class DayViewContainer(view: View) : ViewContainer(view) {
            // Will be set when this container is bound. See the dayBinder.
            lateinit var day: CalendarDay
            val textView = CalendarDayLayoutBinding.bind(view).exOneDayText

            init {
                view.setOnClickListener {
                    if (day.position == DayPosition.MonthDate) {
                        val selectedDate = Calendar.getInstance()
                        selectedDate.set(
                            day.date.year,
                            day.date.monthValue - 1,
                            day.date.dayOfMonth
                        )
                        if (selectedDate.before(Calendar.getInstance())) {
                            Toast.makeText(
                                requireContext(),
                                "Cannot add task to previous date",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            mainViewModel.setDay(day)
                            AddTaskDialog().show(parentFragmentManager, "ADD_TASK_DIALOG")
                        }
                    }
                }
            }
        }

        monthCalendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                bindDate(data.date, container.textView, data.position == DayPosition.MonthDate)
            }
        }

        monthCalendarView.monthScrollListener = { updateTitle() }
        monthCalendarView.setup(startMonth, endMonth, firstDayOfWeekFromLocale())
        monthCalendarView.scrollToMonth(currentMonth)
    }

    private fun bindDate(date: LocalDate, textView: TextView, isSelectable: Boolean) {
        textView.text = date.dayOfMonth.toString()
        if (isSelectable) {
            when {
                selectedDates.contains(date) -> {
                    textView.setTextColorRes(R.color.white)
                    textView.setBackgroundResource(R.drawable.example_1_selected_bg)
                }
                today == date -> {
                    textView.setTextColorRes(R.color.black)
                    textView.setBackgroundResource(R.drawable.example_1_today_bg)
                }
                else -> {
                    textView.setTextColorRes(R.color.black)
                    textView.background = null
                }
            }
        } else {
            textView.setTextColorRes(R.color.example_1_white_light)
            textView.background = null
        }
    }

    private fun updateTitle() {
        val month = monthCalendarView.findFirstVisibleMonth()?.yearMonth ?: return
        binding.exOneYearText.text = month.year.toString()
        binding.exOneMonthText.text = month.month.displayText(short = false)

        mainViewModel.setMonth(month.month.value)

        mainViewModel.getAllTasks().observe(requireActivity()) { list ->
            mainViewModel.month.observe(requireActivity()) { month ->
                selectedDates.clear()
                val temp = list.filter { it.month == month }
                temp.forEach {
                    selectedDates.add(
                        it.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    )
                }
                monthCalendarView.notifyCalendarChanged()
            }
        }
    }

    fun Month.displayText(short: Boolean = true): String {
        val style = if (short) TextStyle.SHORT else TextStyle.FULL
        return getDisplayName(style, Locale.ENGLISH)
    }

    internal fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

    internal fun TextView.setTextColorRes(@ColorRes color: Int) =
        setTextColor(context.getColorCompat(color))

}