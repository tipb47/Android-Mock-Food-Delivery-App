package ttbrowne.c323.finalandroidproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import java.text.SimpleDateFormat
import java.util.*

class CalendarView : Fragment() {

    private val viewModel: AppViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar_view, container, false)
        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)
        val tvAmountSpent = view.findViewById<TextView>(R.id.tvAmountSpent)

        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayString = dateFormatter.format(Calendar.getInstance().time)

        viewModel.orders.observe(viewLifecycleOwner) { orders ->
            // initially display todays date
            setTotalAmountForDate(tvAmountSpent, orders, todayString)

            calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(year, month, dayOfMonth)
                val selectedDateString = dateFormatter.format(calendar.time)

                // update total amount for the selected date
                setTotalAmountForDate(tvAmountSpent, orders, selectedDateString)
            }
        }

        return view
    }

    private fun setTotalAmountForDate(tvAmountSpent: TextView, orders: List<Order>, dateString: String) {
        val totalAmount = orders.filter { it.date == dateString }
            .sumOf { it.price.toDoubleOrNull() ?: 0.0 }
        tvAmountSpent.text = "Amount Spent: $${totalAmount.toInt()}"
    }
}