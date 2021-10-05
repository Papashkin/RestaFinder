package com.antsfamily.restafinder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.antsfamily.restafinder.data.DataRepository
import com.antsfamily.restafinder.data.local.CoordinatesProvider
import com.antsfamily.restafinder.data.local.DataFetchingTimer
import com.antsfamily.restafinder.data.local.model.Coordinates
import com.antsfamily.restafinder.domain.entity.IdValue
import com.antsfamily.restafinder.domain.entity.Restaurant
import com.antsfamily.restafinder.domain.entity.RestaurantValue
import com.antsfamily.restafinder.domain.entity.Restaurants
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
    private val timer: DataFetchingTimer = Mockito.mock(DataFetchingTimer::class.java)

    @Mock
    private val provider: CoordinatesProvider = Mockito.mock(CoordinatesProvider::class.java)

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() = testCoroutineRule.runBlockingTest {
        Mockito.`when`(repository.getFavouriteRestaurantIds())
            .thenReturn(MOCK_FAVOURITE_RESTAURANT_IDS)

        viewModel = HomeViewModel(provider, repository, timer)
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


    @Test
    fun `on Resume`() = testCoroutineRule.runBlockingTest {
        Mockito.`when`(provider.getCoordinates()).thenReturn(MOCK_COORDINATES)
        Mockito.`when`(repository.getRestaurants(MOCK_COORDINATES))
            .thenReturn(MOCK_RESTAURANTS_LIST)

        viewModel.onResume()
        viewModel.onPause()
        viewModel.state.observeForever {
            assert(it.restaurants.size == MOCK_RESTAURANTS_LIST.results.size)
        }
    }

    companion object {
        private val MOCK_FAVOURITE_RESTAURANT_IDS = listOf("mock1", "mock2")
        private val MOCK_RESTAURANTS_LIST = Restaurants(
            results = listOf(
                Restaurant(
                    IdValue("mock1"),
                    "Helsinki 1",
                    "mock_URL_1",
                    listOf(RestaurantValue("en", "MockBar 1")),
                    listOf(RestaurantValue("en", "description of the MockBar 1")),
                    false
                ),
                Restaurant(
                    IdValue("mock2"),
                    "Helsinki 2",
                    "mock_URL_2",
                    listOf(RestaurantValue("en", "MockBar 2")),
                    listOf(RestaurantValue("en", "description of the MockBar 2")),
                    false
                ),
                Restaurant(
                    IdValue("mock3"),
                    "Helsinki 3",
                    "mock_URL_3",
                    listOf(RestaurantValue("en", "MockBar 3")),
                    listOf(RestaurantValue("en", "description of the MockBar 3")),
                    false
                ),
            )
        )
        private val MOCK_COORDINATES = Coordinates(2.22, 3.33)
    }
}
