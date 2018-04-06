package com.nakharin.mapfarmer

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatButton
import android.util.Log
import android.view.View
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
import java.io.IOException

class MapsActivity : AppCompatActivity() {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val TAG = "MapsActivity"
    }

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    private lateinit var btnAdd: AppCompatButton
    private lateinit var btnDone: AppCompatButton

    private lateinit var arrLatLng: ArrayList<LatLng>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment

        arrLatLng = ArrayList()

        btnAdd = findViewById(R.id.btnAdd)
        btnDone = findViewById(R.id.btnDone)

        btnAdd.setOnClickListener(onClickListener)
        btnDone.setOnClickListener(onClickListener)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        mapFragment.getMapAsync(onMapReadyCallback)
    }

    private val onClickListener = View.OnClickListener {
        if (it == btnAdd) {
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

        if (it == btnDone) {
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
        map.mapType = GoogleMap.MAP_TYPE_TERRAIN

        map.setOnPolygonClickListener(onPolygonClickListener)

        checkPermissionLocation()
    }

    private val onPolygonClickListener = GoogleMap.OnPolygonClickListener {
        it.remove()
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
}