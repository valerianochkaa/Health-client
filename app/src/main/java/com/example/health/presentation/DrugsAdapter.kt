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
import com.example.health.utils.RetrofitClient
import androidx.lifecycle.lifecycleScope
import com.example.health.data.drugLike.DrugLikeApi
import com.example.health.data.drugLike.DrugWithLikeStatus
import kotlinx.coroutines.launch

class DrugsAdapter(
    private var drugsList: List<DrugWithLikeStatus>,
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val drugLikeApi: DrugLikeApi, // API для работы с лайками
    private val token: String // Токен авторизации
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
        val drugWithLikeStatus = drugsList[position]
        val drug = drugWithLikeStatus.drug

        // Установка имени лекарства
        holder.name.text = drug.drugName

        // Установка иконки лайка
        holder.btnLike.setImageResource(
            if (drugWithLikeStatus.isLiked) R.drawable.ic_favorite_like
            else R.drawable.ic_favorite
        )

        // Обработка клика на кнопку информации
        holder.btnInfo.setOnClickListener {
            showDialog(drug)
        }

        // Обработка клика на кнопку лайка
        holder.btnLike.setOnClickListener {
            toggleLike(holder, position)
        }
    }

    private fun toggleLike(holder: DrugsViewHolder, position: Int) {
        val drugWithLikeStatus = drugsList[position]
        val drugId = drugWithLikeStatus.drug.drugId ?: return // Проверяем, что drugId не null

        // Изменение статуса лайка
        lifecycleOwner.lifecycleScope.launch {
            try {
                if (drugWithLikeStatus.isLiked) {
                    // Удаление лайка
                    drugLikeApi.deleteLike(drugId, token)
                } else {
                    // Добавление лайка
                    drugLikeApi.insertLike(drugId, token)
                }

                // Обновляем локальный статус лайка
                val updatedDrugWithLikeStatus = drugWithLikeStatus.copy(isLiked = !drugWithLikeStatus.isLiked)
                drugsList = drugsList.toMutableList().apply {
                    set(position, updatedDrugWithLikeStatus)
                }

                // Обновляем UI
                notifyItemChanged(position)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Ошибка при изменении лайка", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun updateData(newDrugsList: List<DrugWithLikeStatus>) {
        drugsList = newDrugsList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return drugsList.size
    }

    private fun showDialog(drug: DrugsDTO) {
        val drugId = drug.drugId ?: return
        lifecycleOwner.lifecycleScope.launch {
            try {
                val drugInstructions = RetrofitClient.drugInstructionsApi.getDrugInstructionByDrugId(drugId)

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


//class DrugsAdapter(
//    private var drugsList: List<DrugWithLikeStatus>,
//    private val context: Context,
//    private val lifecycleOwner: LifecycleOwner
//) : RecyclerView.Adapter<DrugsAdapter.DrugsViewHolder>() {
//
//    inner class DrugsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val name: TextView = itemView.findViewById(R.id.name)
//        val btnInfo: ImageView = itemView.findViewById(R.id.btnInfo)
//        val btnLike: ImageView = itemView.findViewById(R.id.btnLike)
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrugsViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_drugs_search, parent, false)
//        return DrugsViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: DrugsViewHolder, position: Int) {
//        val drugWithLikeStatus = drugsList[position]
//        val drug = drugWithLikeStatus.drug
//        holder.name.text = drug.drugName
//        holder.btnInfo.setOnClickListener {
//            showDialog(drug)
//        }
//        holder.btnLike.setImageResource(
//            if (drugWithLikeStatus.isLiked) R.drawable.ic_favorite_like
//            else R.drawable.ic_favorite
//        )
//    }
//
//    fun updateData(newDrugsList: List<DrugWithLikeStatus>) {
//        drugsList = newDrugsList
//        notifyDataSetChanged()
//    }
//
//    override fun getItemCount(): Int {
//        return drugsList.size
//    }
//
//    private fun showDialog(drug: DrugsDTO) {
//        val drugId = drug.drugId ?: return
//        lifecycleOwner.lifecycleScope.launch {
//            try {
//                val drugInstructions = RetrofitClient.drugInstructionsApi.getDrugInstructionByDrugId(drugId)
//
//                val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_instruction, null)
//                val dialogBuilder = AlertDialog.Builder(context)
//                    .setView(dialogView)
//                    .setCancelable(true)
//                val dialog = dialogBuilder.create()
//                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//                val textViewsMap = mapOf(
//                    R.id.name to drug.drugName,
//                    R.id.description to (drugInstructions.description ?: "Нет описания"),
//                    R.id.activeIngredients to (drugInstructions.activeIngredients ?: "Нет активных ингредиентов"),
//                    R.id.indications to (drugInstructions.indications ?: "Нет показаний"),
//                    R.id.composition to (drugInstructions.composition ?: "Нет состава"),
//                    R.id.contraindications to (drugInstructions.contraindications ?: "Нет противопоказаний"),
//                    R.id.sideEffects to (drugInstructions.sideEffects ?: "Нет побочных эффектов"),
//                    R.id.overdose to (drugInstructions.overdose ?: "Нет информации о передозировке"),
//                    R.id.storageConditions to (drugInstructions.storageConditions ?: "Нет условий хранения"),
//                    R.id.prescriptionRequirements to if (drugInstructions.prescriptionRequirements) "Да" else "Нет",
//                    R.id.price to drug.drugPrice.toString(),
//                    R.id.analog to drug.drugAnalog
//                )
//                textViewsMap.forEach { (id, text) ->
//                    dialogView.findViewById<TextView>(id).text = text
//                }
//                dialogView.findViewById<ImageView>(R.id.btnClose).setOnClickListener {
//                    dialog.dismiss()
//                }
//                dialog.show()
//            } catch (e: Exception) {
//                Log.e("DrugsAdapter", "Error fetching drug instructions", e)
//            }
//        }
//    }
//}


