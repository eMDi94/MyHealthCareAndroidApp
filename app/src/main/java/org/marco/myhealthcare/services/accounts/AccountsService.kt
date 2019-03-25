package org.marco.myhealthcare.services.accounts

import kotlinx.coroutines.Deferred
import org.marco.myhealthcare.models.Patient
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


interface AccountsService {

    @GET("api/accounts/retrieve-user/")
    fun loadUser(@Header("Authorization") authHeader: String): Deferred<Patient>

    @POST("api/accounts/login/")
    fun login(@Body loginRequest: LoginRequest): Deferred<LoginResponse>

    @POST("api/accounts/logout/")
    fun logout(@Header("Authorization") authHeader: String): Deferred<Void>

}