package com.nakharin.mapfarmer

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
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
import com.zqg.kotlin.LoadingDialog
import java.io.IOException

class MapsActivity : AppCompatActivity() {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val PLACE_AUTOCOMPLETE_REQUEST_CODE = 2
        private const val TAG = "MapsActivity"
    }

    private lateinit var toolbar: Toolbar

    private lateinit var autocompleteFragment: PlaceAutocompleteFragment
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    private lateinit var vNavigation: RelativeLayout
    private lateinit var imgNavigation: ImageView
    private lateinit var imgMapType: ImageButton

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recyclerArea: RecyclerView

    private lateinit var imgCenterPoint: ImageView

    private lateinit var imgRemoveArea: ImageView
    private lateinit var imgAddArea: ImageView

    private lateinit var fabAdd: FloatingActionButton
    private lateinit var fabDone: FloatingActionButton

    private lateinit var arrLatLng: ArrayList<LatLng>
    private lateinit var arrAreaModel: ArrayList<AreaModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        initWidget()

        imgNavigation.setOnClickListener(onClickListener)
        imgMapType.setOnClickListener(onClickListener)

        imgRemoveArea.setOnClickListener(onClickListener)
        imgAddArea.setOnClickListener(onClickListener)

        fabAdd.setOnClickListener(onClickListener)
        fabDone.setOnClickListener(onClickListener)

        autocompleteFragment.setOnPlaceSelectedListener(onPlaceSelectionListener)

        mapFragment.getMapAsync(onMapReadyCallback)
    }

    private fun initWidget() {
        toolbar = findViewById(R.id.toolbar)
        toolbar.title = "FarmMap"
        setSupportActionBar(toolbar)

        autocompleteFragment = fragmentManager
                .findFragmentById(R.id.place_autocomplete_fragment) as PlaceAutocompleteFragment

        mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment

        vNavigation = findViewById(R.id.vNavigation)
        imgNavigation = findViewById(R.id.imgNavigation)
        imgMapType = findViewById(R.id.imgMapType)

        recyclerArea = findViewById(R.id.recyclerArea)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerArea.layoutManager = linearLayoutManager
        imgCenterPoint = findViewById(R.id.imgCenterPoint)

        imgRemoveArea = findViewById(R.id.imgRemoveArea)
        imgAddArea = findViewById(R.id.imgAddArea)

        fabAdd = findViewById(R.id.fabAdd)
        fabDone = findViewById(R.id.fabDone)


        arrLatLng = ArrayList()
        arrAreaModel = ArrayList()
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
        var addressList: List<Address>?
        val address1: String?
        val address2: String?
        val state: String?
        val zipCode: String?
        val country: String?

        try {
            addressList = Geocoder(this).getFromLocation(latLng.latitude, latLng.longitude, 1)
        } catch (e: IOException) {
            Log.e("MapsActivity", e.localizedMessage)
            return e.localizedMessage
        }

        if (addressList != null) {
            address1 = addressList[0].getAddressLine(0)
            address2 = addressList[0].getAddressLine(1)
            state = addressList[0].adminArea
            zipCode = addressList[0].postalCode
            country = addressList[0].countryName
            return "$address1 $address2 $state $zipCode $country"
        } else {
            return ""
        }
    }

    private fun shoelaceArea(v: List<LatLng>): Double {
        val n = v.size
        var a = 0.0
        for (i in 0 until n - 1) {
            a += v[i].latitude * v[i + 1].longitude - v[i + 1].latitude * v[i].longitude
        }
        return Math.abs(a + v[n - 1].latitude * v[0].longitude - v[0].latitude * v[n - 1].longitude) / 2.0
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
            when (item.itemId) {
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

        if (it == imgNavigation) {
            val isShow = vNavigation.visibility
            if (isShow == VISIBLE) {
                vNavigation.visibility = GONE
                imgNavigation.setImageResource(R.mipmap.ic_next)
            } else {
                vNavigation.visibility = VISIBLE
                imgNavigation.setImageResource(R.mipmap.ic_back)

                imgCenterPoint.visibility = GONE
                fabAdd.visibility = GONE
                fabDone.visibility = GONE
            }
        }

        if (it == imgMapType) {
            map.let {
                if (map.mapType == GoogleMap.MAP_TYPE_NORMAL) {
                    map.mapType = GoogleMap.MAP_TYPE_HYBRID
                } else {
                    map.mapType = GoogleMap.MAP_TYPE_NORMAL
                }
            }
        }

        if (it == imgRemoveArea) {

        }

        if (it == imgAddArea) {
            val isShow = imgCenterPoint.visibility
            if (isShow == GONE) {
                imgCenterPoint.visibility = VISIBLE
                fabAdd.visibility = VISIBLE
                fabDone.visibility = VISIBLE
                vNavigation.visibility = GONE
                imgNavigation.setImageResource(R.mipmap.ic_next)
            }
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

                val dialog = LoadingDialog(this, "Loading...")
                dialog.show()

                val polygonOptions = PolygonOptions()
                arrLatLng.forEach {
                    polygonOptions.add(it)
                }
                polygonOptions.strokeColor(resources.getColor(R.color.colorPrimaryDark))
                polygonOptions.strokeWidth(5f)
                polygonOptions.fillColor(resources.getColor(R.color.colorPrimaryAlpha))
                polygonOptions.clickable(true)
                val polygon = map.addPolygon(polygonOptions)

                val areaModel = AreaModel(getAddress(arrLatLng[0]), "" + shoelaceArea(polygon.points), polygon)
                arrAreaModel.add(areaModel)

                recyclerArea.adapter = RecyclerAreaAdapter(arrAreaModel)

                Handler().postDelayed({
                    arrLatLng.clear()
                    imgCenterPoint.visibility = GONE
                    fabAdd.visibility = GONE
                    fabDone.visibility = GONE
                    dialog.close()
                }, 1000)
            }
        }
    }

    private val onMapReadyCallback = OnMapReadyCallback {
        map = it

        map.uiSettings.isZoomControlsEnabled = true
        map.mapType = GoogleMap.MAP_TYPE_NORMAL

        map.setOnMapClickListener(onMapClickListener)
        map.setOnPolygonClickListener(onPolygonClickListener)

        checkPermissionLocation()
    }

    private val onMapClickListener = GoogleMap.OnMapClickListener {
        if (vNavigation.visibility == VISIBLE) {
            vNavigation.visibility = GONE
            imgNavigation.setImageResource(R.mipmap.ic_next)
        }
    }

    private val onPolygonClickListener = GoogleMap.OnPolygonClickListener {
        //        it.remove()
    }
}