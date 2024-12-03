package com.example.myhealth.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.health.R
import com.example.health.data.temperatures.TemperaturesDTO
import com.example.health.data.temperatures.TemperaturesList

class TemperatureAdapter(
    private var temperatureList: List<TemperaturesDTO>,
    private val context: Context?,
    private val onDelete: (Int) -> Unit
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
        val temperatureData = temperatureList[position]
        holder.value.text = "${temperatureData.temperatureValue} °C"
        holder.recordDate.text = temperatureData.recordDate

        holder.itemView.setOnLongClickListener {
            onDelete(temperatureData.temperatureId ?: 0)
            true
        }
    }

    override fun getItemCount(): Int {
        return temperatureList.size
    }

    fun updateData(newTemperatureList: List<TemperaturesDTO>) {
        temperatureList = newTemperatureList
        notifyDataSetChanged()
    }
}