package com.nakharin.mapfarmer.Controller

import android.animation.Animator
import android.animation.ObjectAnimator
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
import android.widget.*
import co.metalab.asyncawait.async
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
import com.nakharin.mapfarmer.Utils.AnimationUtility
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

    private lateinit var vNavigationParent: FrameLayout
    private lateinit var vNavigation: RelativeLayout
    private lateinit var imgNavigation: ImageView
    private lateinit var imgMapType: ImageButton

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recyclerArea: RecyclerView

    private lateinit var imgCenterPoint: ImageView

    private lateinit var imgRemoveArea: ImageView
    private lateinit var imgEditArea: ImageView
    private lateinit var imgAddArea: ImageView

    private lateinit var fabAdd: FloatingActionButton
    private lateinit var fabDone: FloatingActionButton

    private lateinit var arrCircle: ArrayList<Circle>
    private lateinit var arrAreaModel: ArrayList<AreaModel>

    private var numberMarker: Int = 1
    private var isNavigationShow: Boolean = false
    private var positionRemove: Int = -1

    /******************************************************************************************
     ********************************* Method *************************************************
     ******************************************************************************************/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        initWidget()

        imgNavigation.setOnClickListener(onClickListener)
        imgMapType.setOnClickListener(onClickListener)

        imgRemoveArea.setOnClickListener(onClickListener)
        imgEditArea.setOnClickListener(onClickListener)
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

        vNavigationParent = findViewById(R.id.vNavigationParent)
        vNavigation = findViewById(R.id.vNavigation)
        imgNavigation = findViewById(R.id.imgNavigation)
        imgMapType = findViewById(R.id.imgMapType)

        recyclerArea = findViewById(R.id.recyclerArea)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerArea.layoutManager = linearLayoutManager

        imgCenterPoint = findViewById(R.id.imgCenterPoint)

        imgRemoveArea = findViewById(R.id.imgRemoveArea)
        imgEditArea = findViewById(R.id.imgEditArea)
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

    private fun showNavigationArea() {
        val anim = ObjectAnimator.ofFloat(vNavigationParent, View.TRANSLATION_X, vNavigation.width.toFloat())
        anim.duration = AnimationUtility.DEFAULT_ANIMATION_DURATION
        anim.start()
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                imgNavigation.setImageResource(R.mipmap.ic_back)
                imgCenterPoint.visibility = GONE
                fabAdd.visibility = GONE
                fabDone.visibility = GONE
                isNavigationShow = true
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationStart(animation: Animator) {
            }
        })
    }

    private fun hideNavigationArea(showFab: Boolean) {
        val anim = ObjectAnimator.ofFloat(vNavigationParent, View.TRANSLATION_X, 0F)
        anim.duration = AnimationUtility.DEFAULT_ANIMATION_DURATION
        anim.start()
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                imgNavigation.setImageResource(R.mipmap.ic_next)
                isNavigationShow = false

                if (showFab) {
                    imgCenterPoint.visibility = View.VISIBLE
                    fabAdd.visibility = View.VISIBLE
                    fabDone.visibility = View.VISIBLE
                }
            }

            override fun onAnimationCancel(animation: Animator) {
            }

            override fun onAnimationStart(animation: Animator) {
            }
        })
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

    /******************************************************************************************
     ****************************** Listener *************************************************
     ******************************************************************************************/

    private val onPlaceSelectionListener = object : PlaceSelectionListener {

        override fun onPlaceSelected(place: Place) {
            val placeName = place.name.toString()
            val placeAddress = place.address.toString()

            placeMarkerOnMap(place.latLng, placeName, placeAddress)
        }

        override fun onError(status: Status) {
            Log.e(TAG, status.statusMessage)
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

            if (positionRemove != -1) {
                arrAreaModel[positionRemove].state = false
            }
            arrAreaModel[position].state = true
            positionRemove = position
            recyclerArea.adapter.notifyDataSetChanged()
        }
    }

    private val onClickListener = View.OnClickListener {

        if (it == imgNavigation) {
            if (isNavigationShow) {
                hideNavigationArea(false)
            } else {
                showNavigationArea()
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
            if (positionRemove != -1) {
                arrAreaModel[positionRemove].polygon.remove()
                arrAreaModel[positionRemove].marker.remove()
                arrAreaModel.removeAt(positionRemove)
                recyclerArea.adapter.notifyDataSetChanged()
                positionRemove = -1
            }
        }

        if (it == imgEditArea) {
            Toast.makeText(this, "Edit", Toast.LENGTH_SHORT).show()
        }

        if (it == imgAddArea) {
            val isShow = imgCenterPoint.visibility
            if (isShow == GONE) {
                hideNavigationArea(true)
            }
        }

        if (it == fabAdd) {
            map.let {
                val circleOptions = CircleOptions()
                circleOptions.center(it.cameraPosition.target)
                circleOptions.radius(4.0)
                circleOptions.fillColor(resources.getColor(R.color.colorPrimaryDark))
                circleOptions.strokeWidth(4f)
                circleOptions.strokeColor(resources.getColor(R.color.colorWhite))
                circleOptions.clickable(true)
                val circle = it.addCircle(circleOptions)
                arrCircle.add(circle)

                Toast.makeText(this, "Added !!!", Toast.LENGTH_SHORT).show()
            }
        }

        if (it == fabDone) {
            if (arrCircle.size > 0) {

                val dialog = LoadingDialog(this, "Loading...")
                dialog.cancelable(false)
                dialog.show()

                async {

                    val polygonOptions = PolygonOptions()
                    arrCircle.forEach {
                        polygonOptions.add(it.center)
                    }

                    polygonOptions.strokeColor(resources.getColor(R.color.colorPrimaryDark))
                    polygonOptions.strokeWidth(5f)
                    polygonOptions.fillColor(resources.getColor(R.color.colorPrimaryAlpha))
                    polygonOptions.clickable(true)

                    val polygon = map.addPolygon(polygonOptions)
                    val centerLatLng = MapUtility().getPolygonCenterPoint(polygon.points)
                    val title = await {
                        MapUtility().getAddress(it.context, centerLatLng)
                    }
                    val snippet = MapUtility().calculateArea(polygon.points)
                    val icon = await {
                        MapUtility().makeBitmap(resources, numberMarker.toString())
                    }
                    val areaThaiFormat = MapUtility().convertToThaiArea(snippet)

                    val markerOptions = MarkerOptions()
                    markerOptions.position(centerLatLng)
                    markerOptions.title(title)
                    markerOptions.snippet(areaThaiFormat)
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
                    val marker = map.addMarker(markerOptions)
                    marker.tag = numberMarker

                    val areaModel = AreaModel(numberMarker, title, areaThaiFormat, polygon, marker, false)
                    arrAreaModel.add(areaModel)

                    recyclerArea.adapter.notifyDataSetChanged()

                    numberMarker++

                }.finally {
                    Handler().postDelayed({
                        arrCircle.forEach {
                            it.remove()
                        }
                        arrCircle.clear()
                        imgCenterPoint.visibility = GONE
                        fabAdd.visibility = GONE
                        fabDone.visibility = GONE
                        dialog.close()
                    }, 100)
                }
            }
        }
    }

    private val onMapReadyCallback = OnMapReadyCallback {
        map = it

        map.uiSettings.isZoomControlsEnabled = true
        map.mapType = GoogleMap.MAP_TYPE_NORMAL

        map.setOnMarkerClickListener(onMarkerClickListener)

        checkPermissionLocation()
    }

    private val onMarkerClickListener: GoogleMap.OnMarkerClickListener = GoogleMap.OnMarkerClickListener {

        val position = arrAreaModel.getPositionFromTag(it.tag as Int)
        if (positionRemove != -1) {
            arrAreaModel[positionRemove].state = false
        }

        arrAreaModel[position].state = true
        positionRemove = position
        recyclerArea.adapter.notifyDataSetChanged()

        false
    }
}

/******************************************************************************************
 ****************************** Extension *************************************************
 ******************************************************************************************/

fun ArrayList<AreaModel>.getPositionFromTag(tag: Int) : Int {
    for (i in this.indices) {
        if (this[i].tag == tag) {
            return i
        }
    }
    return -1
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
