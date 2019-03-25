package org.marco.myhealthcare.viewmodels

import androidx.lifecycle.ViewModel
import org.marco.myhealthcare.extensions.withIOContext
import org.marco.myhealthcare.repositories.accounts.AccountsRepository
import org.marco.myhealthcare.repositories.posologies.PosologiesRepository


class PosologiesViewModel(
    private val accountsRepository: AccountsRepository,
    private val posologiesRepository: PosologiesRepository
): ViewModel() {

    fun loadPosologies() {
        withIOContext {
            posologiesRepository.loadPosologies(accountsRepository.token!!, accountsRepository.patient!!.code)
        }
    }

}