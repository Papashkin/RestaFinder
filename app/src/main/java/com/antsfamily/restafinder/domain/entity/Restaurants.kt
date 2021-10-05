package com.antsfamily.restafinder.domain.entity

import com.google.gson.annotations.SerializedName

data class Restaurants(
    val results: List<Restaurant>
)

data class Restaurant(
    val id: IdValue,
    val address: String,
    @SerializedName("listimage")
    val image: String,
    val name: List<RestaurantValue>,
    @SerializedName("short_description")
    val description: List<RestaurantValue>,
    val favourite: Boolean
)

data class RestaurantValue(
    val lang: String,
    val value: String,
)

data class IdValue(
    @SerializedName("\$oid")
    val id: String
)
