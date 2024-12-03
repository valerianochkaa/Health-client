package com.example.myhealth.ui.adapters

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.health.R
import com.example.health.data.drugs.DrugsList

class DrugsAdapter(var drugsList: List<DrugsList>, private val context: Context?) :
    RecyclerView.Adapter<DrugsAdapter.DrugsViewHolder>() {

    private val favoriteDrugs = mutableSetOf<DrugsList>()

    inner class DrugsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val btnInfo: ImageView = itemView.findViewById(R.id.btnInfo)
        val btnLike: ImageView = itemView.findViewById(R.id.btnLike)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrugsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_drugs_search, parent, false)
        return DrugsViewHolder(view)
    }

    override fun onBindViewHolder(holder: DrugsViewHolder, position: Int) {
        val drug = drugsList[position]
        holder.name.text = drug.name

        // Устанавливаем иконку в зависимости от того, является ли лекарство любимым
        if (favoriteDrugs.contains(drug)) {
            holder.btnLike.setImageResource(R.drawable.ic_favorite_like)
        } else {
            holder.btnLike.setImageResource(R.drawable.ic_favorite)
        }

        holder.btnInfo.setOnClickListener {
            showDialog(drug)
        }

        holder.btnLike.setOnClickListener {
            // Добавляем или удаляем лекарство из избранного только если like == true
            if (drug.like) {
                if (favoriteDrugs.contains(drug)) {
                    favoriteDrugs.remove(drug)
                    holder.btnLike.setImageResource(R.drawable.ic_favorite)
                } else {
                    favoriteDrugs.add(drug)
                    holder.btnLike.setImageResource(R.drawable.ic_favorite_like)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return drugsList.size
    }

    fun setFilteredList(drugsList: List<DrugsList>) {
        this.drugsList = drugsList
        notifyDataSetChanged()
    }


    private fun showDialog(drugsList: DrugsList) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_instruction, null)
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(true)
        val dialog = dialogBuilder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Заполнение диалогового окна данными о лекарстве
        val title: TextView = dialogView.findViewById(R.id.name)
        val description: TextView = dialogView.findViewById(R.id.description)
        val activeIngredients: TextView = dialogView.findViewById(R.id.activeIngredients)
        val indications: TextView = dialogView.findViewById(R.id.composition)
        val composition: TextView = dialogView.findViewById(R.id.indications)
        val contraindications: TextView = dialogView.findViewById(R.id.contraindications)
        val sideEffects: TextView = dialogView.findViewById(R.id.sideEffects)
        val overdose: TextView = dialogView.findViewById(R.id.overdose)
        val storageConditions: TextView = dialogView.findViewById(R.id.storageConditions)
        val prescriptionRequirements: TextView = dialogView.findViewById(R.id.prescriptionRequirements)
        val price: TextView = dialogView.findViewById(R.id.price)
        val analog: TextView = dialogView.findViewById(R.id.analog)

        title.text = drugsList.name
        description.text = drugsList.description
        activeIngredients.text = drugsList.activeIngredients
        indications.text = drugsList.indications
        composition.text = drugsList.composition
        contraindications.text = drugsList.contraindications
        sideEffects.text = drugsList.sideEffects
        overdose.text = drugsList.overdose
        storageConditions.text = drugsList.storageConditions
        prescriptionRequirements.text = if (drugsList.prescriptionRequirements) {
            "Да"
        } else {
            "Нет"
        }
        price.text = drugsList.price.toString()
        analog.text = drugsList.analog

        val btnClose: ImageView = dialogView.findViewById(R.id.btnClose)
        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}

