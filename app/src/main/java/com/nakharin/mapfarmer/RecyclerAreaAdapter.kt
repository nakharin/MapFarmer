package com.nakharin.mapfarmer

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.gms.maps.model.Polygon

class RecyclerAreaAdapter(private val areaList: ArrayList<AreaModel>) : RecyclerView.Adapter<RecyclerAreaAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.view_recycler_area_item_row, parent, false))
    }

    override fun getItemCount(): Int {
        return areaList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (position % 2 == 0) {
            holder.vBackground.setBackgroundColor(holder.vBackground.resources.getColor(R.color.colorWhite))
        } else {
            holder.vBackground.setBackgroundColor(holder.vBackground.resources.getColor(R.color.colorGray1Alpha))
        }

        if (areaList[position].polygon.isVisible) {
            holder.imgItem.setImageDrawable(holder.imgItem.resources.getDrawable(R.mipmap.ic_visibility))
        } else {
            holder.imgItem.setImageDrawable(holder.imgItem.resources.getDrawable(R.mipmap.ic_visibility_off))
        }

        holder.txtItemTitle.text = areaList[position].title
        holder.txtItemSubTitle.text = areaList[position].subTitle

        holder.imgItem.setOnClickListener(OnImageClickListener(holder, areaList[position].polygon))
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val vBackground = v.findViewById<LinearLayout>(R.id.vBackground)!!
        val imgItem = v.findViewById<ImageView>(R.id.imgItem)!!
        val txtItemTitle = v.findViewById<TextView>(R.id.txtItemTitle)!!
        val txtItemSubTitle = v.findViewById<TextView>(R.id.txtItemSubTitle)!!
    }

    class OnImageClickListener(holder: ViewHolder, polygon: Polygon) : View.OnClickListener {

        private val h = holder
        private val r = h.imgItem.resources
        private val p = polygon

        override fun onClick(v: View) {
            if (v == h.imgItem) {
                if (h.imgItem.scaleType == ImageView.ScaleType.CENTER_INSIDE) {
                    h.imgItem.scaleType = ImageView.ScaleType.FIT_CENTER
                    h.imgItem.setImageDrawable(r.getDrawable(R.mipmap.ic_visibility_off))
                    p.isVisible = false
                } else {
                    h.imgItem.scaleType = ImageView.ScaleType.CENTER_INSIDE
                    h.imgItem.setImageDrawable(r.getDrawable(R.mipmap.ic_visibility))
                    p.isVisible = true
                }
            }
        }
    }
}