package com.antsfamily.restafinder.data.remote.model

import com.google.gson.annotations.SerializedName

data class Restaurant(
    val id: IdValue,
    val address: String,
    val city: String,
    val country: String,
    @SerializedName("mainimage")
    val mainImage: String,
    val name: List<RestaurantValue>,
    val description: List<RestaurantValue>,
    val phone: String,
)

data class RestaurantValue(
    val lang: String,
    val value: String,
)

data class IdValue(
    @SerializedName("\$oid")
    val id: String
)
