package org.marco.myhealthcare.extensions

import kotlin.reflect.KProperty

class NonNullSingleValue<T> {

    private var value: T? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T =
        value ?: throw NullPointerException()


    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = if (this.value == null) value
        else throw IllegalArgumentException()
    }

}