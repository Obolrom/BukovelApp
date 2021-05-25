package com.company.app.ui.services

import com.google.gson.annotations.SerializedName

data class ServiceReview(
    @SerializedName("userNickName")
    val user: String,

    @SerializedName("serviceName")
    val serviceName: String,

    @SerializedName("review")
    val review: String,

    @SerializedName("mark")
    val mark: Double
)