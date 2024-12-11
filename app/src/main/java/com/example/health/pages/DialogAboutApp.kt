package com.example.health.pages

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.example.health.databinding.DialogAboutAppBinding

class DialogAboutApp(
    private val onSubmitClickListener: (Float) -> Unit
): DialogFragment() {
    private lateinit var binding : DialogAboutAppBinding
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAboutAppBinding.inflate(LayoutInflater.from(context))
        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)
        binding.btnClose.setOnClickListener {
           dismiss()
        }
        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }
}