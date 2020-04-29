package com.example.testexercisealexm.di

import com.example.testexercisealexm.domain.use_case.WikiUseCase
import com.example.testexercisealexm.domain.use_case.WikiUseCaseImp
import com.example.testexercisealexm.presentation.MainActivity
import com.example.testexercisealexm.repo.WikiRepo
import com.example.testexercisealexm.repo.WikiRepoImp
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeAuthActivity(): MainActivity

    @Binds
    abstract fun bindWikiUseCase(wikiUseCase: WikiUseCaseImp): WikiUseCase

    @Binds
    abstract fun bindWikiRepo(wikiRepo: WikiRepoImp): WikiRepo
}