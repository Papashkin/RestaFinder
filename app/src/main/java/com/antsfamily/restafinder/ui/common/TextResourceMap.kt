package com.antsfamily.restafinder.ui.common

import com.antsfamily.restafinder.R
import com.antsfamily.restafinder.presentation.TextResource

fun TextResource.resourceId(): Int {
    return when (this) {
        TextResource.RESTAURANT_ADD_TO_FAVOURITES -> R.string.restaurant_add_to_favourite
        TextResource.RESTAURANT_REMOVE_FROM_FAVOURITES -> R.string.restaurant_remove_from_favourite
    }
}
