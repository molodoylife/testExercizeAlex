package com.example.testexercisealexm.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testexercisealexm.domain.model.WikiPoiDetails
import com.example.testexercisealexm.domain.model.WikiPoint
import com.example.testexercisealexm.domain.use_case.WikiUseCase
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

class WikiViewModelImp @Inject constructor(val wikiUseCase: WikiUseCase): ViewModel() {
    private val disposables: CompositeDisposable = CompositeDisposable()

    private val points: MutableLiveData<List<WikiPoint>> = MutableLiveData()

    private val errors: MutableLiveData<Throwable> = MutableLiveData()

    fun getPoints(): LiveData<List<WikiPoint>> {
        return points
    }

    fun getErrors(): LiveData<Throwable> {
        return errors
    }

    fun getNearestPois(radius: Int, position: LatLng){
        wikiUseCase.getNearestPois(radius, position.latitude, position.longitude)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                disposables.add(it)
            }
            .subscribe ({
                it
            }, {
                it
            })
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}

@Singleton
class ViewModelFactory @Inject constructor(private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModels[modelClass]?.get() as T
}