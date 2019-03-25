package org.marco.myhealthcare.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_single_day.*
import kotlinx.android.synthetic.main.fragment_single_day.view.*
import kotlinx.android.synthetic.main.holder_posology.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.marco.myhealthcare.R
import org.marco.myhealthcare.models.Posology
import org.marco.myhealthcare.repositories.posologies.PosologiesRepositoryState
import org.marco.myhealthcare.viewmodels.SingleDayPosologiesViewModel

class SingleDayPosologiesFragment: Fragment() {

    companion object {

        private val DAY_KEY = "org.marco.myhealthcare.ui.fragments.SingleDayPosologiesFragment.DAY_KEY"

        fun newSinglePosologiesFragment(day: String): SingleDayPosologiesFragment {
            val fragment = SingleDayPosologiesFragment()
            val args = Bundle()
            args.putString(DAY_KEY, day)
            fragment.arguments = args
            return fragment
        }
    }

    inner class PosologyViewHolder(v: View): RecyclerView.ViewHolder(v) {

        var posology: Posology? = null
            set(value) {
                itemView.hour_text.text = value?.hour
                itemView.quantity_text.text = value?.quantity
                itemView.medicine_text.text = value?.medicine
                field = value
            }
    }

    inner class PosologiesAdapter: RecyclerView.Adapter<PosologyViewHolder>() {

        lateinit var posologies: List<Posology>

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosologyViewHolder {
            val view = LayoutInflater.from(activity).inflate(R.layout.holder_posology, parent, false)
            return PosologyViewHolder(view)
        }

        override fun getItemCount() = posologies.size

        override fun onBindViewHolder(holder: PosologyViewHolder, position: Int) {
            holder.posology = posologies[position]
        }

    }

    private lateinit var day: String
    private val singleDayPosologiesViewModel: SingleDayPosologiesViewModel
            by viewModel("SingleDayPosologiesViewModel") {
                parametersOf(day)
            }
    private lateinit var adapter: PosologiesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater.inflate(R.layout.fragment_single_day, container, false)
        v.posologies_rec_view.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        return v
    }


    override fun onResume() {
        super.onResume()
        arguments?.let {
            day = it.getString(DAY_KEY, "")
        }
        val italianDay = engDayToItalianDay(day)
        day_text_view.text = italianDay
        singleDayPosologiesViewModel.posologies.observe(this, Observer {
            when (it.code) {
                PosologiesRepositoryState.LOADING -> {
                    adapter.posologies = listOf()
                    adapter.notifyDataSetChanged()
                }
                PosologiesRepositoryState.FAILURE -> {
                    adapter.posologies = listOf()
                    adapter.notifyDataSetChanged()
                    Toast.makeText(context, R.string.loading_failure, Toast.LENGTH_LONG).show()
                }
                PosologiesRepositoryState.SUCCESS -> {
                    try {
                        // dummy for causing the exception
                        adapter.posologies
                    } catch (t: UninitializedPropertyAccessException) {
                        adapter = PosologiesAdapter()
                        posologies_rec_view.adapter = adapter
                    } finally {
                        adapter.posologies = it.posologies!!
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    private fun engDayToItalianDay(day: String) =
            when (day) {
                "Monday" -> "Lunedì"
                "Tuesday" -> "Martedì"
                "Wednesday" -> "Mercoledì"
                "Thursday" -> "Giovedì"
                "Friday" -> "Venerdì"
                "Saturday" -> "Sabato"
                "Sunday" -> "Domenica"
                else -> ""
            }

}