package com.example.pruebadroid.ui.task.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pruebadroid.R
import com.example.pruebadroid.databinding.FragmentTaskListBinding
import com.example.pruebadroid.ui.task.TaskViewModel
import com.example.pruebadroid.utils.Globals

class TaskListFragment : Fragment(), TaskListAdapter.OnTaskListener {

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskListAdapter: TaskListAdapter

    private val taskViewModel: TaskViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initMenu()
        initListeners()
        initAdapter()
        initObservables()
    }

    private fun initMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_task, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_add_new_task -> {
                        findNavController().navigate(R.id.nav_task_detail)
                        true
                    }

                    R.id.action_filter_task -> {
                        val dialog =
                            FilterTaskDialogFragment(taskViewModel.filterID) { selectedFilter ->
                                taskViewModel.applyFilter(selectedFilter)
                            }
                        dialog.show(parentFragmentManager, "FilterDialog")
                        true
                    }

                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun initListeners() {
        binding.btnNoDataTaskList.setOnClickListener {
            findNavController().navigate(R.id.nav_task_detail)
        }
    }

    private fun initAdapter() {
        taskListAdapter = TaskListAdapter(this)
        binding.recyclerTaskList.layoutManager = LinearLayoutManager(context)
        binding.recyclerTaskList.adapter = taskListAdapter
        registerForContextMenu(binding.recyclerTaskList)
    }

    private fun initObservables() {
        taskViewModel.filterTaskListLiveData.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.constraintNoDataTaskList.visibility = View.VISIBLE
            } else {
                taskListAdapter.updateDataList(it)
                binding.constraintNoDataTaskList.visibility = View.GONE
            }
        }

        taskViewModel.loadTaskList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMarkAsCompleted(id: String) {
        taskViewModel.markAsCompleted(id)
    }

    override fun onDeleted(id: String) {
        taskViewModel.deleteTask(id)
    }

    override fun onClick(id: String) {
        findNavController().navigate(
            R.id.nav_task_detail,
            Bundle().apply { putString(Globals.ARGUMENT_TASK_ID, id) })
    }
}