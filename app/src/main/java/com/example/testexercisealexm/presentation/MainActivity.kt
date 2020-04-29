package com.example.testexercisealexm.presentation

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.example.testexercisealexm.R
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val wikiMapFragment = WikiMapFragment.newInstance()

        supportFragmentManager.beginTransaction().run{
            replace(android.R.id.content, wikiMapFragment)
            commit()
        }
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}
