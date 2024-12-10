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
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.health.R
import com.example.health.data.drugs.DrugsDTO
import com.example.health.data.drugs.DrugsList
import com.example.health.data.weights.WeightsDTO
import com.example.health.utils.RetrofitInstance
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class DrugsAdapter(
    private var drugsList: List<DrugsDTO>,
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<DrugsAdapter.DrugsViewHolder>() {
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
        holder.name.text = drug.drugName
        holder.btnInfo.setOnClickListener {
            showDialog(drug)
        }
    }

    override fun getItemCount(): Int {
        return drugsList.size
    }

    fun updateData(newDrugsList: List<DrugsDTO>) {
        drugsList = newDrugsList
        notifyDataSetChanged()
    }

    private fun showDialog(drug: DrugsDTO) {
        val drugId = drug.drugId ?: return
        lifecycleOwner.lifecycleScope.launch {
            try {
                val drugInstructions = RetrofitInstance.drugInstructionsApi.getDrugInstructionByDrugId(drugId)

                val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_instruction, null)
                val dialogBuilder = AlertDialog.Builder(context)
                    .setView(dialogView)
                    .setCancelable(true)
                val dialog = dialogBuilder.create()
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                val textViewsMap = mapOf(
                    R.id.name to drug.drugName,
                    R.id.description to (drugInstructions.description ?: "Нет описания"),
                    R.id.activeIngredients to (drugInstructions.activeIngredients ?: "Нет активных ингредиентов"),
                    R.id.indications to (drugInstructions.indications ?: "Нет показаний"),
                    R.id.composition to (drugInstructions.composition ?: "Нет состава"),
                    R.id.contraindications to (drugInstructions.contraindications ?: "Нет противопоказаний"),
                    R.id.sideEffects to (drugInstructions.sideEffects ?: "Нет побочных эффектов"),
                    R.id.overdose to (drugInstructions.overdose ?: "Нет информации о передозировке"),
                    R.id.storageConditions to (drugInstructions.storageConditions ?: "Нет условий хранения"),
                    R.id.prescriptionRequirements to if (drugInstructions.prescriptionRequirements) "Да" else "Нет",
                    R.id.price to drug.drugPrice.toString(),
                    R.id.analog to drug.drugAnalog
                )
                textViewsMap.forEach { (id, text) ->
                    dialogView.findViewById<TextView>(id).text = text
                }
                dialogView.findViewById<ImageView>(R.id.btnClose).setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            } catch (e: Exception) {
                Log.e("DrugsAdapter", "Error fetching drug instructions", e)
            }
        }
    }
}

//        if (favoriteDrugs.contains(drug)) {
//            holder.btnLike.setImageResource(R.drawable.ic_favorite_like)
//        } else {
//            holder.btnLike.setImageResource(R.drawable.ic_favorite)
//        }

//        holder.btnLike.setOnClickListener {
//            if (drug.like) {
//                if (favoriteDrugs.contains(drug)) {
//                    favoriteDrugs.remove(drug)
//                    holder.btnLike.setImageResource(R.drawable.ic_favorite)
//                } else {
//                    favoriteDrugs.add(drug)
//                    holder.btnLike.setImageResource(R.drawable.ic_favorite_like)
//                }
//            }
//        }

