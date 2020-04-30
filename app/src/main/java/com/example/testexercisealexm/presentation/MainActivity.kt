package com.example.testexercisealexm.presentation

import android.os.Bundle
import com.example.testexercisealexm.R
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * Starting the map container fragment
         * */
        supportFragmentManager.beginTransaction().run {
            replace(android.R.id.content, WikiMapFragment.newInstance())
            commit()
        }
    }
}
