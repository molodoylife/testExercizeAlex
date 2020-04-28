package com.example.testexercisealexm.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testexercisealexm.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PoiDialogFragment : BottomSheetDialogFragment() {
    companion object {
        /**
         * @return a new instance of the [WikiMapFragment]
         */
        fun newInstance() = PoiDialogFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.poi_dialog_fragment, container, false)
    }
}

