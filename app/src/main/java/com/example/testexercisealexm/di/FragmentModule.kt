package com.example.testexercisealexm.di

import com.example.testexercisealexm.presentation.WikiMapFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeMyFragment(): WikiMapFragment
}