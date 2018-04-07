package com.nakharin.mapfarmer

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import java.io.IOException

class MapsActivity : AppCompatActivity() {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val PLACE_AUTOCOMPLETE_REQUEST_CODE = 2
        private const val TAG = "MapsActivity"
    }

    private lateinit var toolbar: Toolbar
    private lateinit var txtSearch: TextView

    private lateinit var autocompleteFragment: PlaceAutocompleteFragment
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    private lateinit var fabAdd: FloatingActionButton
    private lateinit var fabDone: FloatingActionButton

    private lateinit var arrLatLng: ArrayList<LatLng>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        initWidget()

        txtSearch.setOnClickListener(onClickListener)
        fabAdd.setOnClickListener(onClickListener)
        fabDone.setOnClickListener(onClickListener)

        autocompleteFragment.setOnPlaceSelectedListener(onPlaceSelectionListener)

        mapFragment.getMapAsync(onMapReadyCallback)
    }

    private fun initWidget() {
        toolbar = findViewById(R.id.toolbar)
        toolbar.title = "FarmMap"
        setSupportActionBar(toolbar)

        txtSearch = findViewById(R.id.txtSearch)

        autocompleteFragment = fragmentManager
                .findFragmentById(R.id.place_autocomplete_fragment) as PlaceAutocompleteFragment
        mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment

        arrLatLng = ArrayList()

        fabAdd = findViewById(R.id.fabAdd)
        fabDone = findViewById(R.id.fabDone)
    }

    private fun checkPermissionLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            setUpGoogleMap()
        } else {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    @SuppressLint("MissingPermission")
    private fun setUpGoogleMap() {
        map.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            }
        }
    }

    private fun placeMarkerOnMap(latLng: LatLng, title: String, snippet: String) {
        val markerOptions = MarkerOptions().position(latLng)
        markerOptions.title(title)
        markerOptions.snippet(snippet)
        map.addMarker(markerOptions)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    private fun getAddress(latLng: LatLng): String {
        val geocoder = Geocoder(this)
        val addresses: List<Address>?
        val address: Address?
        var addressText = ""

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (null != addresses && !addresses.isEmpty()) {
                address = addresses[0]
                for (i in 0 until address.maxAddressLineIndex) {
                    addressText += if (i == 0) address.getAddressLine(i) else "\n" + address.getAddressLine(i)
                }
            }
        } catch (e: IOException) {
            Log.e("MapsActivity", e.localizedMessage)
        }

        return addressText
    }

    private fun loadPlacePicker() {
        val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
        try {
            startActivityForResult(intent.build(this@MapsActivity), PLACE_AUTOCOMPLETE_REQUEST_CODE)
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission has been denied by user")
                } else {
                    setUpGoogleMap()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.map, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when(item.itemId) {
                R.id.menu_map_search -> {
                    loadPlacePicker()
                    return true
                }
            }

            return false
        }

        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PLACE_AUTOCOMPLETE_REQUEST_CODE -> {
                when (resultCode) {
                    RESULT_OK -> {
                        val place = PlaceAutocomplete.getPlace(this, data)
                        val placeName = place.name.toString()
                        val placeAddress = place.address.toString()

                        placeMarkerOnMap(place.latLng, placeName, placeAddress)
                    }
                    PlaceAutocomplete.RESULT_ERROR -> {
                        val status = PlaceAutocomplete.getStatus(this, data)
                        Log.e(TAG, status.statusMessage)
                    }
                    RESULT_CANCELED -> {
                        Log.e(TAG, "canceled")
                    }
                }
            }
        }
    }

    private val onPlaceSelectionListener = object : PlaceSelectionListener {

        override fun onPlaceSelected(place: Place) {
        }

        override fun onError(status: Status) {
        }
    }

    private val onClickListener = View.OnClickListener {

        if (it == txtSearch) {
            loadPlacePicker()
        }

        if (it == fabAdd) {
            map.let {
                arrLatLng.add(it.cameraPosition.target)
                val circleOptions = CircleOptions()
                circleOptions.center(it.cameraPosition.target)
                circleOptions.radius(5.0)
                circleOptions.fillColor(resources.getColor(R.color.colorPrimaryDark))
                circleOptions.strokeWidth(5f)
                circleOptions.strokeColor(resources.getColor(R.color.colorWhite))
                map.addCircle(circleOptions)
            }
        }

        if (it == fabDone) {
            if (arrLatLng.size > 0) {
                val polygonOptions = PolygonOptions()
                arrLatLng.forEach {
                    polygonOptions.add(it)
                }
                polygonOptions.strokeColor(resources.getColor(R.color.colorPrimaryDark))
                polygonOptions.strokeWidth(5f)
                polygonOptions.fillColor(resources.getColor(R.color.colorPrimaryAlpha))
                polygonOptions.clickable(true)
                map.addPolygon(polygonOptions)

                arrLatLng.clear()
            }
        }
    }

    private val onMapReadyCallback = OnMapReadyCallback {
        map = it

        map.uiSettings.isZoomControlsEnabled = true
        map.mapType = GoogleMap.MAP_TYPE_HYBRID

        map.setOnPolygonClickListener(onPolygonClickListener)

        checkPermissionLocation()
    }

    private val onPolygonClickListener = GoogleMap.OnPolygonClickListener {
        it.remove()
    }
}