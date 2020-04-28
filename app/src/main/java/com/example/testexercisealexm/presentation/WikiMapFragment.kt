package com.example.testexercisealexm.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.testexercisealexm.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import javax.inject.Inject

class WikiMapFragment : Fragment() {

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var wikiViewModel: WikiViewModel

    companion object {
        /**
         * @return a new instance of the [WikiMapFragment]
         */
        fun newInstance() = WikiMapFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        wikiViewModel = ViewModelProvider(this, viewModelFactory)[WikiViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.wiki_map_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapFragment = childFragmentManager.findFragmentById(R.id.wikiMap) as SupportMapFragment

        mapFragment.getMapAsync {
            googleMap = it

            val centerLatLng = LatLng(50.43905,30.5165969)

            val position = CameraPosition.Builder()
                .target(centerLatLng)
                .zoom(10.0f)
                .build()

            googleMap.addMarker(
                MarkerOptions().position(centerLatLng)
                    .title("You're here")
            )
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position))

            googleMap.setOnCameraIdleListener(onCameraIdleListener)
        }

        val poiDialogFragment: PoiDialogFragment = PoiDialogFragment.newInstance()
        poiDialogFragment.show(fragmentManager!!, "dialog")

    }

    private val onCameraIdleListener: GoogleMap.OnCameraIdleListener = GoogleMap.OnCameraIdleListener {

    }
}