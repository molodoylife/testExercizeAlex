package com.example.testexercisealexm.presentation.poi_details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testexercisealexm.R
import com.example.testexercisealexm.domain.model.WikiPoiDetails
import com.example.testexercisealexm.presentation.CURRENT_LAT_KEY
import com.example.testexercisealexm.presentation.CURRENT_LON_KEY
import com.example.testexercisealexm.presentation.POI_DETAILS_KEY
import com.example.testexercisealexm.presentation.POI_IMAGES_KEY
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.poi_dialog_fragment.*


class PoiDialogFragment : BottomSheetDialogFragment() {
    private var details: WikiPoiDetails? = null
    private var currentLocation: LatLng? = null
    private var images: List<String>? = null

    companion object {
        /**
         * @return a new instance of the [WikiMapFragment]
         */
        @JvmStatic
        fun newInstance(args: Bundle) = PoiDialogFragment()
            .apply {
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

        details = arguments?.getParcelable(POI_DETAILS_KEY)
        currentLocation = LatLng(arguments?.getDouble(CURRENT_LAT_KEY)!!, arguments?.getDouble(
            CURRENT_LON_KEY
        )!!)
        images = arguments?.getStringArrayList(POI_IMAGES_KEY)

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvImages.layoutManager = layoutManager

        images?.let {
            rvImages.adapter = WikiImagesAdapter(it)
        }

        details?.let {
            title.text = it.title
            description.text = it.description
        }

        linkWiki.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW)
            browserIntent.data = Uri.parse(
                "https://en.wikipedia.org/wiki/${details!!.title.replace(
                    "\\s".toRegex(), "_")}"
            )
            startActivity(browserIntent)
        }

        getThere.setOnClickListener {
            details?.let {
                val intent = Intent(Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?saddr=${currentLocation!!.latitude},${currentLocation!!.longitude}&daddr=${it.coords.latitude},${it.coords.longitude}"))
                startActivity(intent)
            }


        }
    }
}

