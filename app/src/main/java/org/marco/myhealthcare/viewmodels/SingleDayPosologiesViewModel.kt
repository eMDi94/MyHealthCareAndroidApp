package org.marco.myhealthcare.viewmodels

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import org.marco.myhealthcare.repositories.posologies.PosologiesRepository
import org.marco.myhealthcare.repositories.posologies.PosologiesRepositoryState
import org.marco.myhealthcare.repositories.posologies.posologiesFailure
import org.marco.myhealthcare.repositories.posologies.posologiesSuccess


class SingleDayPosologiesViewModel(day: String,
                                   posologiesRepository: PosologiesRepository): ViewModel() {

    val posologies = Transformations.map(posologiesRepository.posologiesResultLiveData) {
        when (it.code) {
            PosologiesRepositoryState.LOADING, PosologiesRepositoryState.FAILURE -> it
            PosologiesRepositoryState.SUCCESS -> {
                if (it.posologies == null) {
                    posologiesFailure()
                }
                else {
                    val p = it.posologies.filter {
                        it.dayOfTheWeek == day
                    }
                    posologiesSuccess(p)
                }
            }
        }
    }

}