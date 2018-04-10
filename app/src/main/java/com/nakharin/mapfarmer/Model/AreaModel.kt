package com.nakharin.mapfarmer.Model

import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.Polygon

data class AreaModel(val tag:Int, val title: String, val subTitle: String, val polygon: Polygon, val marker: Marker, var state: Boolean)