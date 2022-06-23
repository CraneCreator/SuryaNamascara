package com.namascara.surya

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// The com.namascara.surya.ViewHolder class holds the view
class AsanaHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var posNumber : TextView = itemView.findViewById(R.id.pos_number_text)
    var holdAsansImage: ImageView = itemView.findViewById(R.id.list_view_sura)
    var holdAsansText: TextView = itemView.findViewById(R.id.list_view_text)
}