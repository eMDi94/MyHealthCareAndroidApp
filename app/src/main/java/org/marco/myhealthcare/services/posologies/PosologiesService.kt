package org.marco.myhealthcare.services.posologies

import kotlinx.coroutines.Deferred
import org.marco.myhealthcare.models.Posology
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import java.util.*

interface PosologiesService {

    @GET("api/medicines/posologies/{patientCode}/")
    fun loadPosologies(@Header("Authorization") authHeader: String,
                       @Path("patientCode") patientCode: UUID): Deferred<List<Posology>>

}
