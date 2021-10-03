package com.antsfamily.restafinder.data.remote

import com.google.gson.annotations.SerializedName

data class Restaurant(
    val id: String,
    val address: String,
    val country: String,
    @SerializedName("mainimage")
    val mainImage: String,
    val name: RestaurantValue,
    val description: RestaurantValue,
    val phone: String,
)

data class RestaurantValue(
    val lang: String,
    val value: String,
)
