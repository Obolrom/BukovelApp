package com.company.app.ui.services

import com.google.gson.annotations.SerializedName

data class Service(
        @SerializedName("id")
        val id: Int,

        @SerializedName("name")
        val title : String,

        @SerializedName("description")
        val imageUrl: String,

        @SerializedName("mark")
        val score: Float)