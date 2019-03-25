package org.marco.myhealthcare

import android.app.Application
import org.koin.android.ext.android.startKoin
import org.marco.myhealthcare.dependencies.httpModule
import org.marco.myhealthcare.dependencies.gsonModule
import org.marco.myhealthcare.dependencies.repositoriesModule
import org.marco.myhealthcare.dependencies.viewModelsModule

class MyHealthCareApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(gsonModule,httpModule, repositoriesModule, viewModelsModule))
    }

}