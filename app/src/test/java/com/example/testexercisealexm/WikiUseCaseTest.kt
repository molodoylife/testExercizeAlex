package com.example.testexercisealexm

import com.example.testexercisealexm.domain.model.WikiPoint
import com.example.testexercisealexm.domain.use_case.WikiUseCaseImp
import com.example.testexercisealexm.repo.WikiRepo
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class LooksDomainTest {
    @get:Rule
    val schedulers = RxImmediateSchedulerRule()

    @InjectMocks
    lateinit var mockWikiUseCase: WikiUseCaseImp

    @Mock
    lateinit var mockWikiRepo: WikiRepo

    private val mockPoiList = listOf<WikiPoint>()

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)

        Mockito.`when`(
            mockWikiRepo.getNearestWikiPois(
                Mockito.anyInt(),
                Mockito.anyDouble(),
                Mockito.anyDouble()
            )
        ).thenReturn(
            Single.just(mockPoiList)
        )
    }

    @Test
    fun testWikiUseCaseGetNearestWikiPoints() {
        // Trigger
        val testObserver: TestObserver<List<WikiPoint>> =
            mockWikiUseCase.getNearestPois(20_0000, 30.0, 20.0).test()

        // Check if radius no more than 10 000 even if we pass more (wiki requirements)
        Mockito.verify(mockWikiRepo).getNearestWikiPois(10_000, 30.0, 20.0)

        // Validation
        testObserver.assertValues(mockPoiList)

        // clean up
        testObserver.dispose()
    }

    //TODO create test for other methods
}