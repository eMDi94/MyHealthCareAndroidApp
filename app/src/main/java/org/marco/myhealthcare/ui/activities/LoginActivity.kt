package org.marco.myhealthcare.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.ext.android.inject
import org.marco.myhealthcare.R
import org.marco.myhealthcare.repositories.accounts.AccountsState
import org.marco.myhealthcare.ui.dialogs.LoadingDialog
import org.marco.myhealthcare.viewmodels.LoginViewModel

class LoginActivity : AppCompatActivity() {

    companion object {
        private val DIALOG_TAG = "org.marco.myhealthcare.ui.activities.LoginActivity.DIALOG_TAG"

        fun newIntent(context: Context): Intent {
            val intent = Intent(context, LoginActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            return intent
        }
    }

    private val loginViewModel: LoginViewModel by inject("LoginViewModel")
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loadingDialog = LoadingDialog()
        loginViewModel.accountsStateLiveData.observe(this, Observer {
            when (it) {
                AccountsState.AUTH_LOADING -> loadingDialog.show(supportFragmentManager, DIALOG_TAG)
                AccountsState.TOKEN_FAILURE -> loadingDialog.dismiss()
                AccountsState.LOGIN_FAILURE -> {
                    loadingDialog.dismiss()
                    Toast.makeText(this, R.string.auth_failure, Toast.LENGTH_LONG).show()
                }
                AccountsState.LOGIN_SUCCESS -> {
                    loadingDialog.dismiss()
                    startActivity(MyHealthCareMainActivity.newIntent(this))
                }
                else -> {}
            }
        })
        login_button.setOnClickListener {
            loginButtonClick(it)
        }
    }

    override fun onResume() {
        super.onResume()
        loginViewModel.loadUser()
    }

    private fun loginButtonClick(v: View) {
        val email = email_text.text.toString()
        val password = password_text.text.toString()
        loginViewModel.login(email, password)
    }
}
