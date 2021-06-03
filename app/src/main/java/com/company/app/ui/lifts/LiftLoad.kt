package com.company.app.ui.lifts

import com.google.gson.annotations.SerializedName

data class LiftLoad(
    @SerializedName("name")
    val name: String,
    @SerializedName("isWorking")
    val active: Boolean,
    @SerializedName("mark")
    val load: Float
)