package com.example.myhealth.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.health.R
import com.example.health.data.pressures.PressuresDTO
import com.example.health.data.pressures.PressuresList

class PressureAdapter(
    private var pressureList: List<PressuresDTO>,
    private val context: Context?,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<PressureAdapter.PressureViewHolder>() {

    inner class PressureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val value: TextView = itemView.findViewById(R.id.value)
        val recordDate: TextView = itemView.findViewById(R.id.recordDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PressureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_diary, parent, false)
        return PressureViewHolder(view)
    }

    override fun onBindViewHolder(holder: PressureViewHolder, position: Int) {
        val pressureData = pressureList[position]
        holder.value.text = "${pressureData.upperValue} / ${pressureData.lowerValue} / ${pressureData.pulseValue}"
        holder.recordDate.text = pressureData.recordDate

        holder.itemView.setOnLongClickListener {
            onDelete(pressureData.pressureId ?: 0)
            true
        }
    }

    override fun getItemCount(): Int {
        return pressureList.size
    }

    fun updateData(newPressureList: List<PressuresDTO>) {
        pressureList = newPressureList
        notifyDataSetChanged()
    }
}
