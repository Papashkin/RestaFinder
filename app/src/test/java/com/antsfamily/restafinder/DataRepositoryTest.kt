package com.antsfamily.restafinder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.antsfamily.restafinder.data.DataRepository
import com.antsfamily.restafinder.data.DataRepositoryImpl
import com.antsfamily.restafinder.data.local.LocalSource
import com.antsfamily.restafinder.data.local.model.Coordinates
import com.antsfamily.restafinder.data.remote.RemoteSource
import com.antsfamily.restafinder.domain.entity.IdValue
import com.antsfamily.restafinder.domain.entity.Restaurant
import com.antsfamily.restafinder.domain.entity.RestaurantValue
import com.antsfamily.restafinder.domain.entity.Restaurants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DataRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var localSource: LocalSource

    @Mock
    private lateinit var remoteSource: RemoteSource

    @Mock
    private lateinit var repository: DataRepository

    @Before
    fun setup() {
        localSource = mock(LocalSource::class.java)
        remoteSource = mock(RemoteSource::class.java)
        repository = DataRepositoryImpl(localSource, remoteSource)
    }

    @Test
    fun `get valid list of restaurants`() = testCoroutineRule.runBlockingTest {
        `when`(remoteSource.getRestaurants(MOCK_COORDINATES.latitude, MOCK_COORDINATES.longitude))
            .thenReturn(MOCK_RESTAURANTS_LIST)
        val restaurants = repository.getRestaurants(MOCK_COORDINATES)
        assert(restaurants.results == MOCK_RESTAURANTS_LIST.results)
    }

    @Test
    fun `get empty list of restaurants`() = testCoroutineRule.runBlockingTest {
        `when`(remoteSource.getRestaurants(MOCK_COORDINATES.latitude, MOCK_COORDINATES.longitude))
            .thenReturn(Restaurants(emptyList()))
        val restaurants = repository.getRestaurants(MOCK_COORDINATES)
        assert(restaurants.results.isEmpty())
    }

    @Test(expected = RuntimeException::class)
    fun `get remote exception`() = testCoroutineRule.runBlockingTest {
        `when`(remoteSource.getRestaurants(MOCK_COORDINATES.latitude, MOCK_COORDINATES.longitude))
            .thenThrow(MOCK_EXCEPTION)
        repository.getRestaurants(MOCK_COORDINATES)
    }

    @Test
    fun `get favourite restaurants IDs`() = testCoroutineRule.runBlockingTest {
        `when`(localSource.getFavouriteRestaurantIds())
            .thenReturn(MOCK_FAVOURITE_RESTAURANT_IDS)
        val ids = repository.getFavouriteRestaurantIds()
        assert(ids.size == 2)
    }

    companion object {
        private val MOCK_FAVOURITE_RESTAURANT_IDS = listOf("1", "2")
        private val MOCK_RESTAURANTS_LIST = Restaurants(
            results = listOf(
                Restaurant(
                    IdValue("1"),
                    "Helsinki 1",
                    "mock_URL_1",
                    listOf(RestaurantValue("en", "MockBar 1")),
                    listOf(RestaurantValue("en", "description of the MockBar 1")),
                    false
                ),
                Restaurant(
                    IdValue("2"),
                    "Helsinki 2",
                    "mock_URL_2",
                    listOf(RestaurantValue("en", "MockBar 2")),
                    listOf(RestaurantValue("en", "description of the MockBar 2")),
                    false
                )
            )
        )
        private val MOCK_COORDINATES = Coordinates(2.22, 3.33)
        private val MOCK_EXCEPTION = RuntimeException("mock_exception")
    }
}
