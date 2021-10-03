package com.antsfamily.restafinder.data.remote.model

import com.google.gson.annotations.SerializedName

data class Restaurant(
    val id: IdValue,
    val address: String,
    @SerializedName("listimage")
    val image: String,
    val name: List<RestaurantValue>,
    @SerializedName("short_description")
    val description: List<RestaurantValue>,
)

data class RestaurantValue(
    val lang: String,
    val value: String,
)

data class IdValue(
    @SerializedName("\$oid")
    val id: String
)
