package com.example.testexercisealexm.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.testexercisealexm.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val wikiMapFragment = WikiMapFragment.newInstance()

        supportFragmentManager.beginTransaction().run{
            replace(android.R.id.content, wikiMapFragment)
            commit()
        }
    }
}
