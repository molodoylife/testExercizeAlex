package com.example.testexercisealexm.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testexercisealexm.R
import com.example.testexercisealexm.domain.model.WikiPoiDetails
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.poi_dialog_fragment.*


class PoiDialogFragment : BottomSheetDialogFragment() {
    companion object {
        /**
         * @return a new instance of the [WikiMapFragment]
         */
        @JvmStatic
        fun newInstance(args: Bundle) = PoiDialogFragment().apply {
            arguments = args
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.poi_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val details: WikiPoiDetails? = arguments?.getParcelable(POI_DETAILS_KEY)

        details?.let {
            title.text = details.title
            description.text = details.description
        }

        linkWiki.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW)
            browserIntent.data = Uri.parse(
                "https://en.wikipedia.org/wiki/${details!!.title.replace(
                    "\\s".toRegex(),
                    "_"
                )}"
            )
            startActivity(browserIntent)
        }
    }
}

