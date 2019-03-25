package org.marco.myhealthcare.repositories.accounts

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.marco.myhealthcare.models.Patient
import org.marco.myhealthcare.models.Token
import org.marco.myhealthcare.services.accounts.AccountsService
import org.marco.myhealthcare.services.accounts.LoginRequest


internal infix fun String.to(password: String): LoginRequest = LoginRequest(this, password)


enum class AccountsState {
    AUTH_LOADING,
    LOGIN_SUCCESS,
    LOGOUT_SUCCESS,
    LOGOUT_FAILURE,
    LOGIN_FAILURE,
    TOKEN_FAILURE
}


interface AccountsRepository {

    var token: Token?
    var patient: Patient?
    val accountsStateLiveData: LiveData<AccountsState>

    suspend fun login(email: String, password: String)
    suspend fun loadUser()
    suspend fun logout()

}


class AccountsRepositoryImpl(private val accountsService: AccountsService,
                             context: Context): AccountsRepository {

    companion object {
        val PREFERENCES_KEY = "MyHealthCare"
        val TOKEN_KEY = "TokenKey"
    }

    override var token: Token? = null
    override var patient: Patient? = null
    override val accountsStateLiveData: LiveData<AccountsState> = MutableLiveData()
    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE)
        val token = sharedPreferences.getString(TOKEN_KEY, null)
        if (token != null) {
            this.token = Token(token)
        }
    }

    override suspend fun login(email: String, password: String) {
        (accountsStateLiveData as MutableLiveData).postValue(AccountsState.AUTH_LOADING)
        try {
            val response = accountsService.login(email to password).await()
            token = Token(response.token)
            patient = response.patient
            sharedPreferences.edit {
                putString(TOKEN_KEY, token?.tokenString)
            }
            accountsStateLiveData.postValue(AccountsState.LOGIN_SUCCESS)
        } catch (t: Throwable) {
            accountsStateLiveData.postValue(AccountsState.LOGIN_FAILURE)
        }
    }

    override suspend fun loadUser() {
        token?.let {
            (accountsStateLiveData as MutableLiveData).postValue(AccountsState.AUTH_LOADING)
            try {
                patient = accountsService.loadUser(it.authHeader).await()
            } catch (t: Throwable) {
                accountsStateLiveData.postValue(AccountsState.TOKEN_FAILURE)
                return
            }
            accountsStateLiveData.postValue(AccountsState.LOGIN_SUCCESS)
        }
    }

    override suspend fun logout() {
        (accountsStateLiveData as MutableLiveData).postValue(AccountsState.AUTH_LOADING)
        try {
            token = null
            patient = null
            sharedPreferences.edit {
                remove(TOKEN_KEY)
            }
            accountsStateLiveData.postValue(AccountsState.LOGOUT_SUCCESS)
        } catch (t: Throwable) {
            accountsStateLiveData.postValue(AccountsState.LOGOUT_FAILURE)
        }
    }

}