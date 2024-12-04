package org.qstuff.edc_app.domain.entity

import androidx.annotation.Keep

@Keep
data class ApiError(
    var code: Int = -1,
    val message: String
)