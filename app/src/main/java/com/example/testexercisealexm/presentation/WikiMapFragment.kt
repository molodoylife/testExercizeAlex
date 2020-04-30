package com.example.testexercisealexm.presentation

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.testexercisealexm.R
import com.example.testexercisealexm.domain.model.WikiPoint
import com.example.testexercisealexm.presentation.poi_details.PoiDialogFragment
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

/**
 * Fragment containing google map for wiki poi search
 */
class WikiMapFragment : DaggerFragment(), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnCameraIdleListener {

    companion object {
        /**
         * @return a new instance of the [WikiMapFragment]
         */
        fun newInstance() = WikiMapFragment()
    }

    /**
     * Factory to retrieve viewModel
     */
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    /**
     * Client to get fresh user location
     */
    @Inject
    lateinit var fusedLocationClient: FusedLocationProviderClient

    /**
     * Containing current portion of google markers binded to points
     */
    private val markersWithPageId = mutableMapOf<Marker, WikiPoint>()

    /**
     * Last known location
     */
    private lateinit var lastLocation: LatLng

    /**
     * For convenient permissions managing
     */
    lateinit var rxPermissions: RxPermissions

    /**
     * Child google map fragment to acces to GoogleMap object
     */
    private lateinit var mapFragment: SupportMapFragment

    /**
     * GoogleMap object
     */
    private lateinit var googleMap: GoogleMap

    /**
     * ViewModel to implement MVVM presentation architecture
     */
    lateinit var wikiViewModel: WikiViewModel

    /**
     * Callback on receive fresh user location
     */
    private val newLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // stop requesting location
            stopLocationUpdates()

            // update last location
            lastLocation =
                LatLng(locationResult.lastLocation.latitude, locationResult.lastLocation.longitude)

            // get camera position and animate there
            val position = CameraPosition.Builder()
                .target(lastLocation)
                .zoom(10.0f)
                .build()
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position))

            // allow google map to set current user location
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true

            // set WikiMapFragment as a listener for some google map events
            googleMap.setOnCameraIdleListener(this@WikiMapFragment)
            googleMap.setOnMarkerClickListener(this@WikiMapFragment)
            googleMap.setOnMyLocationButtonClickListener(this@WikiMapFragment)

            // start receiving nearest wiki pois
            lastLocation?.let {
                pbWikiMap.visibility = View.VISIBLE
                wikiViewModel.getNearestPois(getMapRadius(googleMap), it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        rxPermissions = RxPermissions(this)
        wikiViewModel = ViewModelProvider(this, viewModelFactory)[WikiViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.wiki_map_fragment, container, false)
    }

    /**
     * Callback when googlemap is ready
     */
    override fun onMapReady(map: GoogleMap) {
        // set local variable
        googleMap = map

        // ask for location permission
        rxPermissions
            .request(Manifest.permission.ACCESS_FINE_LOCATION)
            .subscribe { isGranted ->
                if (isGranted)
                    startLocationUpdates()
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get child google map fragment to async request GoogleMap object
        mapFragment = childFragmentManager.findFragmentById(R.id.wikiMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // declaring observers on viewModel events
        wikiViewModel.getPoints().observe(viewLifecycleOwner, Observer { points ->
            pbWikiMap.visibility = View.GONE

            // clear the map and point hash map
            googleMap.clear()
            markersWithPageId.clear()

            // fill hash map and place markers on the map
            points.forEach { point ->
                val markerToAdd = MarkerOptions().position(point.position)
                markersWithPageId[googleMap.addMarker(markerToAdd)] = point
            }
        })

        // declaring observers on viewModel events
        wikiViewModel.getPoiDetails().observe(viewLifecycleOwner, Observer { details ->
            pbWikiMap.visibility = View.GONE

            // starting new dialog fragment to display wiki point details
            val detailsArgs = Bundle().apply {
                putParcelable(POI_DETAILS_KEY, details)
                putDouble(CURRENT_LAT_KEY, lastLocation.latitude)
                putDouble(CURRENT_LON_KEY, lastLocation.longitude)
                putStringArrayList(POI_IMAGES_KEY, ArrayList(details.images))
            }
            PoiDialogFragment.newInstance(detailsArgs).show(
                requireActivity().supportFragmentManager,
                POI_DETAILS_FRAGMENT_TAG
            )
        })

        // declaring observers on viewModel errors
        wikiViewModel.getErrors().observe(viewLifecycleOwner, Observer { error ->
            pbWikiMap.visibility = View.GONE

            //TODO decide better error managing to display only user friendly errors
            Snackbar.make(rootLayout, getString(R.string.common_error_title), Snackbar.LENGTH_LONG).show()
        })
    }

    /**
     * @return LocationRequest with parameters
     * */
    private fun createLocationRequest() = LocationRequest.create().apply {
            interval = 3000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

    /**
     * Make location client stop location updates
     * */
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(newLocationCallback)
    }

    /**
     * Make location client start location updates
     * */
    private fun startLocationUpdates() {
        if (rxPermissions.isGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            fusedLocationClient.requestLocationUpdates(
                createLocationRequest(),
                newLocationCallback,
                null
            )
        }
    }

    /**
     * Refreshes user current location when user hit 'my location' button on the map
     * */
    override fun onMyLocationButtonClick(): Boolean {
        startLocationUpdates()
        return true
    }

    /**
     * Overrides onMarkerClick fun to request Poi details
     * */
    override fun onMarkerClick(marker: Marker): Boolean {
        markersWithPageId[marker]?.let {
            pbWikiMap.visibility = View.VISIBLE
            wikiViewModel.getPoiDetails(it)
        }

        return true
    }

    /**
     * Allows user to see new pois when moving map to another place
     * */
    override fun onCameraIdle() {
        pbWikiMap.visibility = View.VISIBLE
        wikiViewModel.getNearestPois(getMapRadius(googleMap), googleMap.cameraPosition.target)
    }
}