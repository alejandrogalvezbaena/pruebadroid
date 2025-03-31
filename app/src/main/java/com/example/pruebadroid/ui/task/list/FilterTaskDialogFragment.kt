package com.example.pruebadroid.ui.task.list

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.pruebadroid.R
import com.example.pruebadroid.data.model.Task
import com.example.pruebadroid.databinding.FragmentFiltersTaskDialogBinding

class FilterTaskDialogFragment(private val selectedFilter: String, private val onApplyFilter: (String) -> Unit) : DialogFragment() {

    private var _binding: FragmentFiltersTaskDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentFiltersTaskDialogBinding.inflate(LayoutInflater.from(requireContext()))

        when(selectedFilter){
            Task.STATE_PENDING -> binding.radioBtnPendingFiltersTaskDialog.isChecked = true
            Task.STATE_COMPLETED -> binding.radioBtnCompletedFiltersTaskDialog.isChecked = true
            else -> binding.radioBtnAllFiltersTaskDialog.isChecked = true
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.filters_task_dialog_title))
            .setView(binding.root)
            .setPositiveButton(getString(R.string.filters_task_dialog_ok_button)) { _, _ ->
                val selectedOption =
                    when (binding.radioGroupFiltersTaskDialog.checkedRadioButtonId) {
                        R.id.radioBtnAllFiltersTaskDialog -> Task.STATE_ALL
                        R.id.radioBtnPendingFiltersTaskDialog -> Task.STATE_PENDING
                        R.id.radioBtnCompletedFiltersTaskDialog -> Task.STATE_COMPLETED
                        else -> Task.STATE_ALL
                    }
                onApplyFilter(selectedOption)
            }
            .setNegativeButton(getString(R.string.filters_task_dialog_cancel_button)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}