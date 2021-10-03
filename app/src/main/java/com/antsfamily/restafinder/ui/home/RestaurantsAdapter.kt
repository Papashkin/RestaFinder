package com.antsfamily.restafinder.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.antsfamily.restafinder.R
import com.antsfamily.restafinder.data.remote.model.Restaurant
import com.antsfamily.restafinder.databinding.ItemRestaurantBinding

class RestaurantsAdapter : ListAdapter<Restaurant, RestaurantsViewHolder>(RestaurantsDiffUtil()) {

    override fun onBindViewHolder(holder: RestaurantsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantsViewHolder {
        val bind = ItemRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RestaurantsViewHolder(bind)
    }
}

class RestaurantsViewHolder(private val binding: ItemRestaurantBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Restaurant) {
        with(binding) {
            restaurantNameTv.text = item.name.firstOrNull()?.value.orEmpty()
            restaurantAddressTv.text = item.address
            restaurantDescriptionTv.text = item.description.firstOrNull()?.value.orEmpty()

            restaurantIv.load(item.image) {
                placeholder(R.drawable.ic_placeholder_restaurant)
                transformations(RoundedCornersTransformation(8f))
            }

            favouriteIv.setImageResource(R.drawable.ic_favorite) //TODO rework it
        }
    }
}

class RestaurantsDiffUtil : DiffUtil.ItemCallback<Restaurant>() {
    override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
        return oldItem.id == newItem.id
    }
}
