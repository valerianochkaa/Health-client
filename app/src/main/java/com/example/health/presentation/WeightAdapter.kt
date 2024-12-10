package com.example.myhealth.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.health.R
import com.example.health.data.weights.WeightsDTO

class WeightAdapter(
    private var weightList: List<WeightsDTO>,
    private val context: Context?,
    private val onDelete: (Int) -> Unit
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
        val weightData = weightList[position]
        holder.value.text = "${weightData.weightValue} кг"
        holder.recordDate.text = weightData.recordDate
        holder.itemView.setOnLongClickListener {
            onDelete(weightData.weightId ?: 0)
            true
        }
    }

    override fun getItemCount(): Int {
        return weightList.size
    }

    fun updateData(newWeightList: List<WeightsDTO>) {
        weightList = newWeightList
        notifyDataSetChanged()
    }
}