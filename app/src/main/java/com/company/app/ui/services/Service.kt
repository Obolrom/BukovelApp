package com.company.app.ui.services

import com.google.gson.annotations.SerializedName

data class Service(
        @SerializedName("id")
        var id: Int,

        @SerializedName("name")
        var title : String,

        @SerializedName("description")
        var description: String,

        @SerializedName("mark")
        var score: Float)