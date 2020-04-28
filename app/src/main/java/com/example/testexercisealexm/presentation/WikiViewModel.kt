package com.example.testexercisealexm.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testexercisealexm.domain.model.WikiPoiDetails
import com.example.testexercisealexm.domain.use_case.WikiUseCase
import com.google.android.gms.maps.model.LatLng
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Provider

abstract class WikiViewModel : ViewModel() {
    abstract fun getNearestPois(radius: Int, position: LatLng)

    abstract fun getActions(): LiveData<List<WikiPoiDetails>>

    abstract fun getErrors(): LiveData<Throwable>
}

class WikiViewModelImp @Inject constructor(val wikiUseCase: WikiUseCase): WikiViewModel() {
    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun getNearestPois(radius: Int, position: LatLng){
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

    override fun getActions(): LiveData<List<WikiPoiDetails>> {
        TODO("Not yet implemented")
    }

    override fun getErrors(): LiveData<Throwable> {
        TODO("Not yet implemented")
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}

class ViewModelFactory @Inject constructor(private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModelProvider = viewModels[modelClass]
            ?: throw IllegalArgumentException("model class $modelClass not found")
        return viewModelProvider.get() as T
    }
}