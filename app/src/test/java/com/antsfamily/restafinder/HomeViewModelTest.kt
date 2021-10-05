package com.antsfamily.restafinder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.antsfamily.restafinder.data.DataRepository
import com.antsfamily.restafinder.data.local.CoordinatesProvider
import com.antsfamily.restafinder.presentation.TextResource
import com.antsfamily.restafinder.presentation.home.HomeViewModel
import com.antsfamily.restafinder.presentation.home.model.RestaurantItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    @Mock
    private val repository: DataRepository = Mockito.mock(DataRepository::class.java)

    @Mock
    private val provider: CoordinatesProvider = Mockito.mock(CoordinatesProvider::class.java)

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() = testCoroutineRule.runBlockingTest {
        Mockito.`when`(repository.getFavouriteRestaurantIds())
            .thenReturn(MOCK_FAVOURITE_RESTAURANT_IDS)

        viewModel = HomeViewModel(provider, repository)
    }

    @Test
    fun `add to favourites`() = testCoroutineRule.runBlockingTest {

        viewModel.onFavouriteIconClick(
            RestaurantItem(
                "mock3",
                "Helsinki 3",
                "mock_URL_3",
                "MockBar 3",
                "description of the MockBar 3",
                false
            )
        )
        viewModel.showSnackBar.observeForever {
            assert(it.getContentIfNotHandled() == TextResource.RESTAURANT_ADD_TO_FAVOURITES)
        }
    }

    @Test
    fun `remove from favourites`() = testCoroutineRule.runBlockingTest {

        viewModel.onFavouriteIconClick(
            RestaurantItem(
                "mock1",
                "Helsinki 1",
                "mock_URL_1",
                "MockBar 1",
                "description of the MockBar 1",
                true
            )
        )
        viewModel.showSnackBar.observeForever {
            assert(it.getContentIfNotHandled() == TextResource.RESTAURANT_REMOVE_FROM_FAVOURITES)
        }
    }

    companion object {
        private val MOCK_FAVOURITE_RESTAURANT_IDS = listOf("mock1", "mock2")
    }
}
