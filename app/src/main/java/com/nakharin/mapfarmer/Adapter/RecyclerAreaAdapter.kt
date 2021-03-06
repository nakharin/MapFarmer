package com.nakharin.mapfarmer.Adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.Polygon
import com.nakharin.mapfarmer.Model.Area
import com.nakharin.mapfarmer.R

class RecyclerAreaAdapter(private val areaList: ArrayList<Area>) : RecyclerView.Adapter<RecyclerAreaAdapter.ViewHolder>() {

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.view_recycler_area_item_row, parent, false))
    }

    override fun getItemCount(): Int {
        return areaList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (areaList[position].state) {
            holder.vBackground.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGray2Alpha))
            holder.txtItemTitle.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite))
            holder.txtItemSubTitle.setTextColor(ContextCompat.getColor(mContext, R.color.colorWhite))
        } else {
            holder.vBackground.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorWhite))
            holder.txtItemTitle.setTextColor(ContextCompat.getColor(mContext, R.color.colorGray2))
            holder.txtItemSubTitle.setTextColor(ContextCompat.getColor(mContext, R.color.colorGray2))
        }

        if (areaList[position].polygon.isVisible) {
            holder.imgItem.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.ic_visibility))
        } else {
            holder.imgItem.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.ic_visibility_off))
        }

        holder.txtItemTitle.text = areaList[position].title
        holder.txtItemSubTitle.text = areaList[position].subTitle

        holder.imgItem.setOnClickListener(OnImageClickListener(holder, areaList[position].polygon, areaList[position].marker))
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val vBackground = v.findViewById<LinearLayout>(R.id.vBackground)!!
        val imgItem = v.findViewById<ImageView>(R.id.imgItem)!!
        val txtItemTitle = v.findViewById<TextView>(R.id.txtItemTitle)!!
        val txtItemSubTitle = v.findViewById<TextView>(R.id.txtItemSubTitle)!!
    }

    class OnImageClickListener(holder: ViewHolder, polygon: Polygon, marker: Marker) : View.OnClickListener {

        private val h = holder
        private val p = polygon
        private val m = marker

        override fun onClick(v: View) {
            if (v == h.imgItem) {
                if (h.imgItem.scaleType == ImageView.ScaleType.CENTER_INSIDE) {
                    h.imgItem.scaleType = ImageView.ScaleType.FIT_CENTER
                    h.imgItem.setImageDrawable(ContextCompat.getDrawable(v.context, R.mipmap.ic_visibility_off))
                    p.isVisible = false
                    m.isVisible = false
                } else {
                    h.imgItem.scaleType = ImageView.ScaleType.CENTER_INSIDE
                    h.imgItem.setImageDrawable(ContextCompat.getDrawable(v.context, R.mipmap.ic_visibility))
                    p.isVisible = true
                    m.isVisible = true
                }
            }
        }
    }
}