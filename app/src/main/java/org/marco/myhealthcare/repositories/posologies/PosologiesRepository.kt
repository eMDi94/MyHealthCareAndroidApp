package org.marco.myhealthcare.repositories.posologies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.marco.myhealthcare.models.Posology
import org.marco.myhealthcare.models.Token
import org.marco.myhealthcare.services.posologies.PosologiesService
import java.util.*


enum class PosologiesRepositoryState {
    LOADING,
    FAILURE,
    SUCCESS
}


data class PosologiesResult(
    val code: PosologiesRepositoryState,
    val posologies: List<Posology>? = null
)


fun posologiesLoading() = PosologiesResult(PosologiesRepositoryState.LOADING)

fun posologiesFailure() = PosologiesResult(PosologiesRepositoryState.FAILURE)

fun posologiesSuccess(posologies: List<Posology>) = PosologiesResult(PosologiesRepositoryState.SUCCESS, posologies)


interface PosologiesRepository {

    val posologiesResultLiveData: LiveData<PosologiesResult>

    suspend fun loadPosologies(token: Token, code: UUID)

}


class PosologiesRepositoryImpl(private val posologiesService: PosologiesService): PosologiesRepository {

    override val posologiesResultLiveData: LiveData<PosologiesResult> = MutableLiveData()

    override suspend fun loadPosologies(token: Token, code: UUID) {
        (posologiesResultLiveData as MutableLiveData).postValue(posologiesLoading())
        try {
            val posologies = posologiesService.loadPosologies(token.authHeader, code).await()
            posologiesResultLiveData.postValue(posologiesSuccess(posologies))
        } catch (t: Throwable) {
            posologiesResultLiveData.postValue(posologiesFailure())
        }
    }
}
