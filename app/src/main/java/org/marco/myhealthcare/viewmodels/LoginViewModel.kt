package org.marco.myhealthcare.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import org.marco.myhealthcare.extensions.withIOContext
import org.marco.myhealthcare.repositories.accounts.AccountsRepository
import org.marco.myhealthcare.repositories.accounts.AccountsState

class LoginViewModel(private val accountsRepository: AccountsRepository): ViewModel() {

    val accountsStateLiveData: LiveData<AccountsState> = accountsRepository.accountsStateLiveData

    fun login(email: String, password: String) {
        withIOContext {
            accountsRepository.login(email, password)
        }
    }

    fun logout() {
        withIOContext {
            accountsRepository.logout()
        }
    }

    fun loadUser() {
        withIOContext {
            accountsRepository.loadUser()
        }
    }

}
