package com.example.testexercisealexm.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testexercisealexm.domain.model.WikiPoiDetails
import com.example.testexercisealexm.domain.model.WikiPoint
import com.example.testexercisealexm.domain.use_case.WikiUseCase
import com.google.android.gms.maps.model.LatLng
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

/**
 * ViewModel for implementing MVVM architecture for presentation layer
 *
 * @param wikiUseCase used to request list of poisand their details
 * */
class WikiViewModel @Inject constructor(val wikiUseCase: WikiUseCase): ViewModel() {
    private val disposables: CompositeDisposable = CompositeDisposable()

    /**
     * Live data objects to update ui
     * */
    private val points: MutableLiveData<List<WikiPoint>> = MutableLiveData()
    private val pointDetails: MutableLiveData<WikiPoiDetails> = MutableLiveData()
    private val errors: MutableLiveData<String> = MutableLiveData()

    fun getPoints(): LiveData<List<WikiPoint>> = points
    fun getPoiDetails(): LiveData<WikiPoiDetails> = pointDetails
    fun getErrors(): LiveData<String> = errors

    /**
     * Requests Api for a new portion of nearest pois. After result is received live data
     * posts it to observer (WikiMapFragment)
     *
     * @param radius distanse from center to search wiki pois
     * @param position center of searched area
     * */
    fun getNearestPois(radius: Int, position: LatLng){
        wikiUseCase.getNearestPois(radius, position.latitude, position.longitude)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                disposables.add(it)
            }
            .subscribe ({
                points.postValue(it)
            }, {
                errors.postValue(it.localizedMessage)
            })
    }

    /**
     * Requests Api for details of selected poi. After result is received live data
     * posts it to observer (WikiMapFragment)
     *
     * @param wikiPoint WikiPoint object to receive details
     * */
    fun getPoiDetails(wikiPoint: WikiPoint){
        wikiUseCase.getPoiDetails(wikiPoint)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                disposables.add(it)
            }
            .subscribe ({
                pointDetails.postValue(it)
            }, {
                errors.postValue(it.localizedMessage)
            })
    }

    /**
     * Clearing disposables on View destroying
     * */
    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}

/**
 * Factory for retrieving viewModels
 * */
@Singleton
class ViewModelFactory @Inject constructor(private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModels[modelClass]?.get() as T
}