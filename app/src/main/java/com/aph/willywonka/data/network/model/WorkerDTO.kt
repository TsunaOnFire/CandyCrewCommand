package com.aph.willywonka.data.network.model

import com.aph.willywonka.data.model.FavouriteModel
import com.google.gson.annotations.SerializedName

data class WorkerDTO(
    @SerializedName("id") var id: Int?,
    @SerializedName("first_name") val first_name: String?,
    @SerializedName("last_name") val last_name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("profession") val profession: String?,
    @SerializedName("quota") val quota: String?,
    @SerializedName("height") val height: Int?,
    @SerializedName("country") val country: String?,
    @SerializedName("age") val age: Int?,
    @SerializedName("gender") val gender: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("favorite") val favorite: FavouriteModel?,
)



