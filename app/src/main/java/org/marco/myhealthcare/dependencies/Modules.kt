package org.marco.myhealthcare.dependencies

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.experimental.builder.viewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.marco.myhealthcare.models.Patient
import org.marco.myhealthcare.models.deserializers.PatientDeserializer
import org.marco.myhealthcare.repositories.accounts.AccountsRepository
import org.marco.myhealthcare.repositories.accounts.AccountsRepositoryImpl
import org.marco.myhealthcare.repositories.posologies.PosologiesRepository
import org.marco.myhealthcare.repositories.posologies.PosologiesRepositoryImpl
import org.marco.myhealthcare.services.accounts.AccountsService
import org.marco.myhealthcare.services.posologies.PosologiesService
import org.marco.myhealthcare.viewmodels.LoginViewModel
import org.marco.myhealthcare.viewmodels.MainViewModel
import org.marco.myhealthcare.viewmodels.PosologiesViewModel
import org.marco.myhealthcare.viewmodels.SingleDayPosologiesViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val gsonModule = module {
    single("GsonInstance") {
        GsonBuilder()
            .registerTypeAdapter(Patient::class.java, PatientDeserializer())
            .create()
    }
}


val httpModule = module {
    single("RetrofitInstance") {
        val gson = get<Gson>("GsonInstance")
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/")
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    single("AccountsService") {
        val retrofit = get<Retrofit>("RetrofitInstance")
        retrofit.create(AccountsService::class.java)
    }
    single("PosologiesService") {
        val retrofit = get<Retrofit>("RetrofitInstance")
        retrofit.create(PosologiesService::class.java)
    }
}


val repositoriesModule = module {
    single("AccountsRepository") {
        AccountsRepositoryImpl(get("AccountsService"), androidContext()) as AccountsRepository
    }
    single("PosologiesRepository") {
        PosologiesRepositoryImpl(get("PosologiesService")) as PosologiesRepository
    }
}


val viewModelsModule = module {
    viewModel<LoginViewModel>("LoginViewModel")
    viewModel<PosologiesViewModel>("PosologiesViewModel")
    viewModel<MainViewModel>("MainViewModel")
    viewModel("SingleDayPosologiesViewModel") {
        (day: String) -> SingleDayPosologiesViewModel(day, get("PosologiesRepository"))
    }
}
