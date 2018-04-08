package com.nakharin.mapfarmer.Utils

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.location.Address
import android.location.Geocoder
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import java.io.IOException


class MapUtility {

    fun getPolygonCenterPoint(latLngList: List<LatLng>): LatLng {
        val builder = LatLngBounds.builder()
        for (latLng in latLngList) {
            builder.include(latLng)
        }
        var bounds = builder.build()
        return bounds.center
    }

    fun shoelaceArea(v: List<LatLng>): Double {
        val n = v.size
        var a = 0.0
        for (i in 0 until n - 1) {
            a += v[i].latitude * v[i + 1].longitude - v[i + 1].latitude * v[i].longitude
        }
        return Math.abs(a + v[n - 1].latitude * v[0].longitude - v[0].latitude * v[n - 1].longitude) / 2.0
    }

    fun makeBitmap(resources: Resources, number: String): Bitmap {

        val scale = resources.displayMetrics.density

        val bounds = Rect()
        val paintText = Paint(Paint.ANTI_ALIAS_FLAG)
        paintText.color = Color.YELLOW
        paintText.textSize = 20 * scale
        paintText.getTextBounds(number, 0, number.length, bounds)
        val conf = Bitmap.Config.ARGB_8888
        val bitmap = Bitmap.createBitmap(bounds.width(), bounds.height(), conf)

        val canvas = Canvas(bitmap)
        val x = bitmap.width - bounds.width() - 3
        val y = bounds.height()
        canvas.drawText(number, x.toFloat(), y.toFloat(), paintText)

        return bitmap
    }

    fun getAddress(context: Context, latLng: LatLng): String {
        val addressList: List<Address>?
        val address1: String?
        val address2: String?
        val state: String?
        val zipCode: String?
        val country: String?

        try {
            addressList = Geocoder(context).getFromLocation(latLng.latitude, latLng.longitude, 1)
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
}