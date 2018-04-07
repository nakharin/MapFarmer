package com.nakharin.mapfarmer

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

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

        holder.imgItem.setImageResource(R.mipmap.ic_visibility)
        holder.txtItemTitle.text = areaList[position].title
        holder.txtItemSubTitle.text = areaList[position].subTitle
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val vBackground = v.findViewById<LinearLayout>(R.id.vBackground)!!
        val imgItem = v.findViewById<ImageView>(R.id.imgItem)!!
        val txtItemTitle = v.findViewById<TextView>(R.id.txtItemTitle)!!
        val txtItemSubTitle = v.findViewById<TextView>(R.id.txtItemSubTitle)!!
    }
}