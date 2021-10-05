package com.antsfamily.restafinder.ui.home.adapter

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.antsfamily.restafinder.presentation.home.model.RestaurantItem

class RestaurantsDiffUtil : DiffUtil.ItemCallback<RestaurantItem>() {
    override fun areContentsTheSame(oldItem: RestaurantItem, newItem: RestaurantItem): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: RestaurantItem, newItem: RestaurantItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun getChangePayload(oldItem: RestaurantItem, newItem: RestaurantItem): Any? {
        val bundle = Bundle().apply {
            if (oldItem.isFavourite != newItem.isFavourite) {
                putBoolean(KEY_IS_FAVOURITE_CHANGED, true)
            }
        }
        return if (bundle.isEmpty) null else bundle
    }

    companion object {
        const val KEY_IS_FAVOURITE_CHANGED = "KEY_IS_FAVOURITE_CHANGED"
    }
}
