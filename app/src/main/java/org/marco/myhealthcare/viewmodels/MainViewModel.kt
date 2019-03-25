package org.marco.myhealthcare.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.marco.myhealthcare.extensions.withIOContext
import org.marco.myhealthcare.repositories.accounts.AccountsRepository
import org.marco.myhealthcare.repositories.accounts.AccountsState
import org.marco.myhealthcare.repositories.posologies.PosologiesRepository


class MainViewModel(private val accountsRepository: AccountsRepository,
                    private val posologiesRepository: PosologiesRepository): ViewModel() {

    val accountsState: LiveData<AccountsState>
        get() = accountsRepository.accountsStateLiveData

    fun loadPosologies() {
        withIOContext {
            posologiesRepository.loadPosologies(accountsRepository.token!!, accountsRepository.patient!!.code)
        }
    }

    fun logout() {
        withIOContext {
            accountsRepository.logout()
        }
    }

}