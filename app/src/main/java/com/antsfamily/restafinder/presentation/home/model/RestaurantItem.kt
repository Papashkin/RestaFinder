package com.antsfamily.restafinder.presentation.home.model

data class RestaurantItem(
    val id: String,
    val address: String,
    val image: String,
    val name: String,
    val description: String,
    val isFavourite: Boolean
)
