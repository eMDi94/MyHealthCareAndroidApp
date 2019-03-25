package org.marco.myhealthcare.extensions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun withIOContext(block: suspend () -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        block()
    }
}
