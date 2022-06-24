package com.namascara.surya

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// этот класс-holder создаёт в Kotlin-коде переменные, связаные с объектами в xml-шаблоне,
// чтобы можно было из кода влиять на эти объекты и узнавать состояние этих объектов
// весь этот класс создан только для использования в классе-адаптере ViewPager2Adapter.kt,
// передающем данные в RecyclerView или ViewPager2 (точнее, в одной лишь функции onBindViewHolder),
// все переменные из этого класса используются только там и только один раз.

// The com.namascara.surya.ViewHolder class holds the view
class AsanaHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var posNumber : TextView = itemView.findViewById(R.id.pos_number_text)
    var holdAsansImage: ImageView = itemView.findViewById(R.id.list_view_sura)
    var holdAsansText: TextView = itemView.findViewById(R.id.list_view_text)
}