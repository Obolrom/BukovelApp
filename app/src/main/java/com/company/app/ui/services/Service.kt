package com.company.app.ui.services

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Service(
        @SerializedName("id")
        val id: Int,

        @SerializedName("name")
        val title : String,

        @SerializedName("description")
        val imageUrl: String,

        @SerializedName("reviews")
        @Expose(serialize = false, deserialize = false)
        var reviews: List<String>?,

        @SerializedName("mark")
        val score: Float)