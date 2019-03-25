package org.marco.myhealthcare.models

import com.google.gson.annotations.SerializedName
import java.util.*


data class Posology(
    @SerializedName("code")
    val code: UUID,
    @SerializedName("hour")
    val hour: String,
    @SerializedName("day_of_the_week")
    val dayOfTheWeek: String,
    @SerializedName("quantity")
    val quantity: String,
    @SerializedName("medicine")
    val medicine: String
)
