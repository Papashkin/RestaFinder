package com.antsfamily.restafinder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.antsfamily.restafinder.data.local.model.Coordinates
import com.antsfamily.restafinder.data.remote.model.IdValue
import com.antsfamily.restafinder.data.remote.model.Restaurant
import com.antsfamily.restafinder.data.remote.model.RestaurantValue
import com.antsfamily.restafinder.data.remote.model.RestaurantsList
import com.antsfamily.restafinder.domain.repository.ContentRepository
import com.antsfamily.restafinder.domain.usecase.GetRestaurantsUseCase
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
class GetRestaurantsUseCaseTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var repository: ContentRepository

    @Mock
    private lateinit var getRestaurantsUseCase: GetRestaurantsUseCase

    @Before
    fun setup() {
        repository = mock(ContentRepository::class.java)
        getRestaurantsUseCase = GetRestaurantsUseCase(repository)
    }

    @Test
    fun `get valid list of restaurants`() = testCoroutineRule.runBlockingTest {
        `when`(repository.getRestaurants(MOCK_COORDINATES.latitude, MOCK_COORDINATES.longitude))
            .thenReturn(MOCK_RESTAURANTS_LIST)
        val restaurants = getRestaurantsUseCase.run(MOCK_COORDINATES)
        assert(restaurants.results == MOCK_RESTAURANTS_LIST.results)
    }

    @Test
    fun `get empty list of restaurants`() = testCoroutineRule.runBlockingTest {
        `when`(repository.getRestaurants(MOCK_COORDINATES.latitude, MOCK_COORDINATES.longitude))
            .thenReturn(RestaurantsList(emptyList()))
        val restaurants = getRestaurantsUseCase.run(MOCK_COORDINATES)
        assert(restaurants.results.isEmpty())
    }

    @Test(expected = RuntimeException::class)
    fun `get exception`() = testCoroutineRule.runBlockingTest {
        `when`(repository.getRestaurants(MOCK_COORDINATES.latitude, MOCK_COORDINATES.longitude))
            .thenThrow(MOCK_EXCEPTION)
        getRestaurantsUseCase.run(MOCK_COORDINATES)
    }

    companion object {
        private val MOCK_RESTAURANTS_LIST = RestaurantsList(
            results = listOf(
                Restaurant(
                    IdValue("1"),
                    "Helsinki 1",
                    "mock_URL_1",
                    listOf(RestaurantValue("en", "MockBar 1")),
                    listOf(RestaurantValue("en", "description of the MockBar 1")),
                ),
                Restaurant(
                    IdValue("2"),
                    "Helsinki 2",
                    "mock_URL_2",
                    listOf(RestaurantValue("en", "MockBar 2")),
                    listOf(RestaurantValue("en", "description of the MockBar 2")),
                )
            )
        )
        private val MOCK_COORDINATES = Coordinates(2.22, 3.33)
        private val MOCK_EXCEPTION = RuntimeException("mock_exception")
    }
}