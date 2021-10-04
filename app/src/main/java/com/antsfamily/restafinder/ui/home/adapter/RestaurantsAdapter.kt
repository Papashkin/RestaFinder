package com.antsfamily.restafinder.ui.home.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.antsfamily.restafinder.R
import com.antsfamily.restafinder.data.local.model.RestaurantItem
import com.antsfamily.restafinder.databinding.ItemRestaurantBinding

class RestaurantsAdapter :
    ListAdapter<RestaurantItem, RestaurantsViewHolder>(RestaurantsDiffUtil()) {

    private var onFavouriteIconClickListener: ((item: RestaurantItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantsViewHolder {
        val bind = ItemRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RestaurantsViewHolder(bind, onFavouriteIconClickListener)
    }

    override fun onBindViewHolder(holder: RestaurantsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: RestaurantsViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNullOrEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            with(payloads.first()) {
                if (this is Bundle && isFavouriteChanged(this)) {
                    holder.setFavourite(getItem(position))
                }
            }
        }
    }

    fun setOnFavouriteIconClickListener(listener: (item: RestaurantItem) -> Unit) {
        onFavouriteIconClickListener = listener
    }

    private fun isFavouriteChanged(bundle: Bundle): Boolean =
        bundle.keySet().contains(RestaurantsDiffUtil.KEY_IS_FAVOURITE_CHANGED)
}

class RestaurantsViewHolder(
    private val binding: ItemRestaurantBinding,
    private var onFavouriteIconClickListener: ((item: RestaurantItem) -> Unit)?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: RestaurantItem) {
        with(binding) {
            restaurantNameTv.text = item.name
            restaurantAddressTv.text = item.address
            restaurantDescriptionTv.text = item.description

            restaurantIv.load(item.image) {
                placeholder(R.drawable.ic_placeholder_restaurant)
                transformations(RoundedCornersTransformation(8f))
            }
            setFavourite(item)

            favouriteIv.setOnClickListener { onFavouriteIconClickListener?.invoke(item) }
        }
    }

    fun setFavourite(item: RestaurantItem) {
        binding.favouriteIv.setImageResource(
            if (item.isFavourite) R.drawable.ic_favorite_selected else R.drawable.ic_favorite
        )
    }
}
