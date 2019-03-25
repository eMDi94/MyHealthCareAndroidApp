package org.marco.myhealthcare.services.accounts


import com.google.gson.annotations.SerializedName
import org.marco.myhealthcare.models.Patient


data class LoginRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
) {
    @SerializedName("login_type")
    val loginType = "PatientLogin"
}


data class LoginResponse(
    @SerializedName("user")
    val patient: Patient,
    @SerializedName("token")
    val token: String
)
