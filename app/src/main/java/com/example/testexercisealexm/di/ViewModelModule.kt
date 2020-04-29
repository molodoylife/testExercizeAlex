package com.example.testexercisealexm.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testexercisealexm.presentation.ViewModelFactory
import com.example.testexercisealexm.presentation.WikiViewModelImp
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(WikiViewModelImp::class)
    internal abstract fun wishListViewModel(viewModel: WikiViewModelImp): ViewModel

}