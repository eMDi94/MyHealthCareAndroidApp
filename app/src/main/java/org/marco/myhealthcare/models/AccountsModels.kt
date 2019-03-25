package org.marco.myhealthcare.models

import com.google.gson.annotations.SerializedName
import java.util.*


data class Patient(
    @SerializedName("code")
    val code: UUID,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String
)


data class Token(
    val tokenString: String
) {
    val authHeader: String
        get() = "Token ${tokenString}"
}
