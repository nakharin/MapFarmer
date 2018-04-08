package com.nakharin.mapfarmer.Controller

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
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
import com.google.android.gms.maps.model.*
import com.nakharin.mapfarmer.Adapter.RecyclerAreaAdapter
import com.nakharin.mapfarmer.Event.RecyclerItemClickListener
import com.nakharin.mapfarmer.Model.AreaModel
import com.nakharin.mapfarmer.R
import com.nakharin.mapfarmer.Utils.MapUtility
import com.zqg.kotlin.LoadingDialog

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

    private lateinit var arrCircle: ArrayList<Circle>
    private lateinit var arrAreaModel: ArrayList<AreaModel>

    private var positonRemove: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        initWidget()

        imgNavigation.setOnClickListener(onClickListener)
        imgMapType.setOnClickListener(onClickListener)

        imgRemoveArea.setOnClickListener(onClickListener)
        imgAddArea.setOnClickListener(onClickListener)

        fabAdd.setOnClickListener(onClickListener)
        fabDone.setOnClickListener(onClickListener)

        autocompleteFragment.setOnPlaceSelectedListener(onPlaceSelectionListener)

        recyclerArea.addOnItemClickListener(onItemClickListener)

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

        arrCircle = ArrayList()
        arrAreaModel = ArrayList()

        recyclerArea.adapter = RecyclerAreaAdapter(arrAreaModel)
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
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 18f))
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

    private fun setNavigationGone() {
        vNavigation.visibility = GONE
        imgNavigation.setImageResource(R.mipmap.ic_next)
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

    private val onItemClickListener: RecyclerItemClickListener.OnClickListener = object : RecyclerItemClickListener.OnClickListener {
        override fun onItemClick(position: Int, view: View) {
            val centerLatLng = MapUtility().getPolygonCenterPoint(arrAreaModel[position].polygon.points)
            val zoomLevel = map.cameraPosition.zoom
            if (zoomLevel.toInt() > 16) {
                map.animateCamera(CameraUpdateFactory.newLatLng(centerLatLng))
            } else {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(centerLatLng, 16f))
            }

            arrAreaModel[position].marker.showInfoWindow()

            positonRemove = position
        }
    }

    private val onClickListener = View.OnClickListener {

        if (it == imgNavigation) {
            val isShow = vNavigation.visibility
            if (isShow == VISIBLE) {
                setNavigationGone()
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
                if (it.mapType == GoogleMap.MAP_TYPE_NORMAL) {
                    it.mapType = GoogleMap.MAP_TYPE_HYBRID
                } else {
                    it.mapType = GoogleMap.MAP_TYPE_NORMAL
                }
            }
        }

        if (it == imgRemoveArea) {
            if (positonRemove != -1) {
                arrAreaModel[positonRemove].polygon.remove()
                arrAreaModel[positonRemove].marker.remove()
                arrAreaModel.removeAt(positonRemove)
                recyclerArea.adapter.notifyDataSetChanged()
                positonRemove = -1
            }
        }

        if (it == imgAddArea) {
            val isShow = imgCenterPoint.visibility
            if (isShow == GONE) {
                imgCenterPoint.visibility = VISIBLE
                fabAdd.visibility = VISIBLE
                fabDone.visibility = VISIBLE
                setNavigationGone()
            }
        }

        if (it == fabAdd) {
            map.let {
                val circleOptions = CircleOptions()
                circleOptions.center(it.cameraPosition.target)
                circleOptions.radius(5.0)
                circleOptions.fillColor(resources.getColor(R.color.colorPrimaryDark))
                circleOptions.strokeWidth(5f)
                circleOptions.strokeColor(resources.getColor(R.color.colorWhite))
                circleOptions.clickable(true)
                val circle = it.addCircle(circleOptions)
                arrCircle.add(circle)
            }
        }

        if (it == fabDone) {
            if (arrCircle.size > 0) {

                val dialog = LoadingDialog(this, "Loading...")
                dialog.show()

                val polygonOptions = PolygonOptions()
                arrCircle.forEach {
                    polygonOptions.add(it.center)
                }
                polygonOptions.strokeColor(resources.getColor(R.color.colorPrimaryDark))
                polygonOptions.strokeWidth(5f)
                polygonOptions.fillColor(resources.getColor(R.color.colorPrimaryAlpha))
                polygonOptions.clickable(true)
                val polygon = map.addPolygon(polygonOptions)

                val number = arrAreaModel.size + 1
                val centerLatLng = MapUtility().getPolygonCenterPoint(polygon.points)
                val title = MapUtility().getAddress(it.context, centerLatLng)
                val snippet = MapUtility().shoelaceArea(polygon.points)
                val icon = MapUtility().makeBitmap(resources, number.toString())

                val markerOptions = MarkerOptions()
                markerOptions.position(centerLatLng)
                markerOptions.title(title)
                markerOptions.snippet(snippet.toString())
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
                val marker = map.addMarker(markerOptions)

                val areaModel = AreaModel(title, snippet.toString(), polygon, marker)
                arrAreaModel.add(areaModel)

                recyclerArea.adapter.notifyDataSetChanged()

                Handler().postDelayed({
                    arrCircle.forEach {
                        it.remove()
                    }
                    arrCircle.clear()
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

        checkPermissionLocation()
    }
}

fun RecyclerView.addOnItemClickListener(listener: RecyclerItemClickListener.OnClickListener) {
    this.addOnChildAttachStateChangeListener(RecyclerItemClickListener(this, listener, null))
}

fun RecyclerView.addOnItemClickListener(listener: RecyclerItemClickListener.OnLongClickListener) {
    this.addOnChildAttachStateChangeListener(RecyclerItemClickListener(this, null, listener))
}

fun RecyclerView.addOnItemClickListener(onClick: RecyclerItemClickListener.OnClickListener, onLongClick: RecyclerItemClickListener.OnLongClickListener) {
    this.addOnChildAttachStateChangeListener(RecyclerItemClickListener(this, onClick, onLongClick))
}
