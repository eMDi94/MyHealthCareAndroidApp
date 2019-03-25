package org.marco.myhealthcare.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import kotlinx.android.synthetic.main.fragment_posologies.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.marco.myhealthcare.R
import org.marco.myhealthcare.ui.dialogs.LoadingDialog
import org.marco.myhealthcare.viewmodels.PosologiesViewModel

class PosologiesFragment: Fragment() {

    companion object {
        private val LOADING_TAG = "org.marco.myhealthcare.ui.fragments.PosologiesFragment.LOADING_TAG"
        private val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    }

    private val loadingDialog: LoadingDialog = LoadingDialog()
    private val posologiesViewModel: PosologiesViewModel by viewModel("PosologiesViewModel")
    private lateinit var adapter: FragmentStatePagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_posologies, container, false)
        adapter = object : FragmentStatePagerAdapter(fragmentManager!!) {
            override fun getItem(position: Int) =
                SingleDayPosologiesFragment.newSinglePosologiesFragment(days[position])

            override fun getCount() = 7
        }
        v.single_day_posologies_pager.adapter = adapter
        return v
    }

    override fun onResume() {
        super.onResume()
        posologiesViewModel.loadPosologies()
    }

}