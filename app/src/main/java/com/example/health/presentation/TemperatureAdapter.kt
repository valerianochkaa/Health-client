package com.example.myhealth.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.health.R
import com.example.health.data.temperatures.TemperaturesList

class TemperatureAdapter(
    private var temperatureList: List<TemperaturesList>,
    private val context: Context?
) : RecyclerView.Adapter<TemperatureAdapter.TemperatureViewHolder>() {

    inner class TemperatureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val value: TextView = itemView.findViewById(R.id.value)
        val recordDate: TextView = itemView.findViewById(R.id.recordDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemperatureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_diary, parent, false)
        return TemperatureViewHolder(view)
    }

    override fun onBindViewHolder(holder: TemperatureViewHolder, position: Int) {
        val temperatureDate = temperatureList[position]
        holder.value.text = "${temperatureDate.temperatureValue}°С"
        holder.recordDate.text = temperatureDate.recordDate
    }

    override fun getItemCount(): Int {
        return temperatureList.size
    }
}