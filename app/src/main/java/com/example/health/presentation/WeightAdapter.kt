package com.example.myhealth.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.health.R
import com.example.health.data.weights.WeightsList

class WeightAdapter(
    private var weightList: List<WeightsList>,
    private val context: Context?
) : RecyclerView.Adapter<WeightAdapter.WeightViewHolder>() {

    inner class WeightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val value: TextView = itemView.findViewById(R.id.value)
        val recordDate: TextView = itemView.findViewById(R.id.recordDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeightViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_diary, parent, false)
        return WeightViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeightViewHolder, position: Int) {
        val weightDate = weightList[position]
        holder.value.text = "${weightDate.weightValue} кг"
        holder.recordDate.text = weightDate.recordDate
    }

    override fun getItemCount(): Int {
        return weightList.size
    }
}