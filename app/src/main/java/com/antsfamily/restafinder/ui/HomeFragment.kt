package com.antsfamily.restafinder.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.antsfamily.restafinder.R
import com.antsfamily.restafinder.databinding.FragmentHomeBinding
import com.antsfamily.restafinder.presentation.HomeViewModel
import com.antsfamily.restafinder.presentation.ViewModelFactory
import com.antsfamily.restafinder.presentation.withFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: HomeViewModel by viewModels { withFactory(viewModelFactory) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(FragmentHomeBinding.bind(view)) {
            observeState(this)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    private fun observeState(binding: FragmentHomeBinding) {
//        with(binding) {
//            viewModel.state.mapDistinct { it.isLoading }
//                .observe(viewLifecycleOwner) { loadingPb.isVisible = it }
//        }
    }
}

