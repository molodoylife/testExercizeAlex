package com.example.testexercisealexm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.testexercisealexm.domain.model.WikiPoiDetails
import com.example.testexercisealexm.domain.model.WikiPoint
import com.example.testexercisealexm.domain.use_case.WikiUseCase
import com.example.testexercisealexm.presentation.WikiViewModel
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * unit test for WikiViewModel
 */
class WikiViewModelUnitTest {

    private lateinit var viewModel: WikiViewModel

    @Mock lateinit var mockWikiUseCase: WikiUseCase
    @Mock lateinit var mockDetailsObserver: Observer<WikiPoiDetails>
    @Mock lateinit var mockPointsObserver: Observer<List<WikiPoint>>
    @Mock lateinit var mockErrorObserver: Observer<String>

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val schedulers = RxImmediateSchedulerRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        viewModel = WikiViewModel(mockWikiUseCase)

        viewModel.getErrors().observeForever(mockErrorObserver)

        viewModel.getPoiDetails().observeForever(mockDetailsObserver)

        viewModel.getPoints().observeForever(mockPointsObserver)
    }

    @Test
    fun testGetNearestPoiListWikiViewModel() {
        val poiListMock = mutableListOf<WikiPoint>()

        Mockito.`when`(mockWikiUseCase.getNearestPois(Mockito.anyInt(), Mockito.anyDouble(), Mockito.anyDouble())).thenReturn(
            Single.just(
            poiListMock
        ))

        viewModel.getNearestPois(20, LatLng(20.0, 50.0))

        Mockito.verify(mockWikiUseCase).getNearestPois(20, 20.0, 50.0)

        Mockito.verify(mockPointsObserver).onChanged(Mockito.eq(poiListMock))
    }

    //TODO Add tests for other methods
}
