package org.marco.myhealthcare.models.deserializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.marco.myhealthcare.models.Patient
import java.lang.reflect.Type
import java.util.*


class PatientDeserializer: JsonDeserializer<Patient> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Patient {
        val jsonObject = json.asJsonObject
        return Patient(
            UUID.fromString(jsonObject["code"].asString),
            jsonObject["first_name"].asString,
            jsonObject["last_name"].asString
        )
    }

}