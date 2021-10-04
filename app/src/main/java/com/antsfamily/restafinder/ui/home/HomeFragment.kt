package com.antsfamily.restafinder.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.antsfamily.restafinder.R
import com.antsfamily.restafinder.databinding.FragmentHomeBinding
import com.antsfamily.restafinder.presentation.EventObserver
import com.antsfamily.restafinder.presentation.HomeViewModel
import com.antsfamily.restafinder.presentation.TextResource
import com.antsfamily.restafinder.ui.common.resourceId
import com.antsfamily.restafinder.ui.home.adapter.RestaurantsAdapter
import com.antsfamily.restafinder.utils.mapDistinct
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModels()

    private val adapter: RestaurantsAdapter = RestaurantsAdapter()

    private var snackbar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(FragmentHomeBinding.bind(view)) {
            observeState(this)
            observeEvents(this)
            bindInteractions(this)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    private fun observeState(binding: FragmentHomeBinding) {
        with(binding) {
            viewModel.state.mapDistinct { it.isStartLoadingVisible }
                .observe(viewLifecycleOwner) { startLoading.isVisible = it }
            viewModel.state.mapDistinct { it.isErrorVisible }
                .observe(viewLifecycleOwner) { errorWithRetryContent.isVisible = it }
            viewModel.state.mapDistinct { it.isFetchLoadingVisible }
                .observe(viewLifecycleOwner) { fetchLoading.isVisible = it }
            viewModel.state.mapDistinct { it.isRestaurantsVisible }
                .observe(viewLifecycleOwner) { restaurantsRv.isVisible = it }
            viewModel.state.mapDistinct { it.restaurants }
                .observe(viewLifecycleOwner) { adapter.submitList(it) }
        }
    }

    private fun observeEvents(binding: FragmentHomeBinding) {
        viewModel.showSnackBar.observe(viewLifecycleOwner, EventObserver {
            showSnackbar(binding.root, it)
        })
    }

    private fun bindInteractions(binding: FragmentHomeBinding) {
        with(binding) {
            errorRetryBtn.setOnClickListener { viewModel.onRetryButtonClick() }
            restaurantsRv.adapter = adapter.apply {
                setOnFavouriteIconClickListener { viewModel.onFavouriteIconClick(it) }
            }
        }
    }

    private fun showSnackbar(view: View, text: TextResource) {
        snackbar?.dismiss()
        snackbar = Snackbar.make(view, text.resourceId(), Snackbar.LENGTH_SHORT)
        snackbar?.show()
    }
}

