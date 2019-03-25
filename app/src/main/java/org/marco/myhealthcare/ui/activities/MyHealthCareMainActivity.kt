package org.marco.myhealthcare.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.transaction
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.marco.myhealthcare.R
import org.marco.myhealthcare.repositories.accounts.AccountsState
import org.marco.myhealthcare.ui.fragments.PosologiesFragment
import org.marco.myhealthcare.viewmodels.MainViewModel


class MyHealthCareMainActivity: AppCompatActivity() {

    companion object {
        fun newIntent(context: Context) = Intent(context, MyHealthCareMainActivity::class.java)
    }

    private val mainViewModel: MainViewModel by viewModel("MainViewModel")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        supportFragmentManager.transaction {
            replace(fragment_frame.id, PosologiesFragment())
        }
        mainViewModel.accountsState.observe(this, Observer {
            when (it) {
                AccountsState.LOGOUT_SUCCESS -> {
                    startActivity(LoginActivity.newIntent(applicationContext))
                    finish()
                }
                AccountsState.LOGOUT_FAILURE -> Toast.makeText(this, R.string.logout_failure, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_item_logout -> mainViewModel.logout()
            R.id.menu_item_force_reload -> mainViewModel.loadPosologies()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

}
