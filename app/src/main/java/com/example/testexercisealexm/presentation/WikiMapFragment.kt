package com.example.testexercisealexm.presentation

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.testexercisealexm.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.tbruyelle.rxpermissions2.RxPermissions
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.wiki_map_fragment.*
import javax.inject.Inject


class WikiMapFragment : DaggerFragment(), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMarkerClickListener {

    private val markersWithPageId = mutableMapOf<Marker, Int>()

    lateinit var rxPermissions: RxPermissions

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var fusedLocationClient: FusedLocationProviderClient

    lateinit var wikiViewModel: WikiViewModelImp

    private val newLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {

            stopLocationUpdates()

            val lastLocation =
                LatLng(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude)

            val position = CameraPosition.Builder()
                .target(lastLocation)
                .zoom(10.0f)
                .build()

            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true

            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position))

            googleMap.setOnCameraIdleListener(onCameraIdleListener)

            googleMap.setOnMarkerClickListener(this@WikiMapFragment)

            googleMap.setOnMyLocationButtonClickListener(this@WikiMapFragment)

            pbWikiMap.visibility = View.VISIBLE

            wikiViewModel.getNearestPois(getMapRadius(googleMap), lastLocation)
        }
    }

    companion object {
        /**
         * @return a new instance of the [WikiMapFragment]
         */
        fun newInstance() = WikiMapFragment()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rxPermissions = RxPermissions(this)

        wikiViewModel = ViewModelProvider(this, viewModelFactory)[WikiViewModelImp::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.wiki_map_fragment, container, false)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        rxPermissions
            .request(Manifest.permission.ACCESS_FINE_LOCATION)
            .subscribe { isGranted ->
                if (isGranted)
                    startLocationUpdates()
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapFragment = childFragmentManager.findFragmentById(R.id.wikiMap) as SupportMapFragment

        mapFragment.getMapAsync(this)

        wikiViewModel.getPoints().observe(viewLifecycleOwner, Observer { points ->
            pbWikiMap.visibility = View.GONE

            googleMap?.let { map ->
                map.clear()
                markersWithPageId.clear()

                points.forEach { point ->
                    val markerToAdd = MarkerOptions().position(point.position)
                    markersWithPageId[map.addMarker(markerToAdd)] = point.pageId
                }
            }
        })

        wikiViewModel.getPoiDetails().observe(viewLifecycleOwner, Observer { details ->
            pbWikiMap.visibility = View.GONE

            val detailsArgs = Bundle().apply { putParcelable(POI_DETAILS_KEY, details) }
            PoiDialogFragment.newInstance(detailsArgs).show(
                requireActivity().supportFragmentManager,
                POI_DETAILS_FRAGMENT_TAG
            )
        })

        wikiViewModel.getErrors().observe(viewLifecycleOwner, Observer { error ->
            pbWikiMap.visibility = View.GONE

            Snackbar.make(rootLayout, error.localizedMessage, Snackbar.LENGTH_LONG).show()
        })
    }

    private val onCameraIdleListener: GoogleMap.OnCameraIdleListener =
        GoogleMap.OnCameraIdleListener {
            pbWikiMap.visibility = View.VISIBLE
            wikiViewModel.getNearestPois(getMapRadius(googleMap), googleMap.cameraPosition.target)
        }

    private fun createLocationRequest(): LocationRequest {
        return LocationRequest.create().apply {
            interval = 3000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(newLocationCallback)
    }

    private fun startLocationUpdates() {
        if (rxPermissions.isGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            fusedLocationClient.requestLocationUpdates(
                createLocationRequest(),
                newLocationCallback,
                null
            )
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        startLocationUpdates()
        return true
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        markersWithPageId[marker]?.let {
            pbWikiMap.visibility = View.VISIBLE
            wikiViewModel.getPoiDetails(it)
        }

        return true
    }
}