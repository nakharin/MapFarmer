package com.nakharin.mapfarmer.Model

import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.Polygon

data class Area(val tag:Int, var title: String, var subTitle: String, val polygon: Polygon, val marker: Marker, var state: Boolean)