package com.example.pruebadroid.ui.task.detail

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.pruebadroid.R
import com.example.pruebadroid.data.model.Task
import com.example.pruebadroid.databinding.FragmentTaskDetailBinding
import com.example.pruebadroid.ui.task.TaskViewModel
import com.example.pruebadroid.utils.Globals
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TaskDetailFragment : Fragment() {

    private var _binding: FragmentTaskDetailBinding? = null
    private val binding get() = _binding!!

    private val taskViewModel: TaskViewModel by activityViewModels()

    private lateinit var taskId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initArguments()
        initViews()
        initListeners()
        initObservables()
    }

    private fun initArguments(){
        taskId = arguments?.getString(Globals.ARGUMENT_TASK_ID) ?: ""
    }

    private fun initViews(){
        val options = resources.getStringArray(R.array.task_status_options).toList()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerStateTaskDetail.adapter = adapter
    }

    private fun initListeners() {
        binding.editDateTaskDetail.setOnClickListener {
            showDatePickerDialog(binding.editDateTaskDetail)
        }
        binding.btnSaveTaskDetail.setOnClickListener {
            if (taskId.isEmpty()){
                taskViewModel.createTask(
                    binding.editTitleTaskDetail.text.toString(),
                    binding.editDescriptionTaskDetail.text.toString(),
                    binding.editDateTaskDetail.text.toString(),
                    binding.spinnerStateTaskDetail.selectedItemPosition.toString()
                )
            } else {
                taskViewModel.editTask(
                    taskId,
                    binding.editTitleTaskDetail.text.toString(),
                    binding.editDescriptionTaskDetail.text.toString(),
                    binding.editDateTaskDetail.text.toString(),
                    binding.spinnerStateTaskDetail.selectedItemPosition.toString()
                )
            }
        }
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(selectedYear, selectedMonth, selectedDay)
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val formattedDate = dateFormat.format(selectedDate.time)
                    editText.setText(formattedDate)
                },
                year, month, day
            )

        datePickerDialog.show()
    }

    private fun initObservables() {
        taskViewModel.saveOkLiveData.observe(viewLifecycleOwner){
            if (it == true) {
                taskViewModel.resetViewModel()
                findNavController().popBackStack()
            }
        }

        taskViewModel.taskLiveData.observe(viewLifecycleOwner){
            if (taskId.isNotEmpty()){
                fillTaskEdit(it)
            }
        }

        if (taskId.isNotEmpty()) {
            taskViewModel.loadTask(taskId)
        }
    }

    private fun fillTaskEdit(task: Task){
        binding.editTitleTaskDetail.setText(task.title)
        binding.editDescriptionTaskDetail.setText(task.description)
        binding.editDateTaskDetail.setText(task.date)
        binding.spinnerStateTaskDetail.setSelection(task.state.toInt())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}