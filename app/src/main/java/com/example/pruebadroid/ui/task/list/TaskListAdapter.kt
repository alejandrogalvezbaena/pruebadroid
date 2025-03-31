package com.example.pruebadroid.ui.task.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.pruebadroid.R
import com.example.pruebadroid.data.model.Task
import com.example.pruebadroid.databinding.ItemTaskListBinding

class TaskListAdapter(val listener: OnTaskListener) :
    RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    private var dataList: ArrayList<Task> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding =
            ItemTaskListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = dataList[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun updateDataList(dataList: ArrayList<Task>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    inner class TaskViewHolder(private val binding: ItemTaskListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnLongClickListener { view ->
                val popupMenu = PopupMenu(binding.root.context, view)
                popupMenu.inflate(R.menu.context_menu_task)
                val itemMenu = popupMenu.menu.findItem(R.id.action_completed)
                if (dataList[adapterPosition].state == Task.STATE_COMPLETED) {
                    itemMenu.isVisible = false
                }
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.action_completed -> {
                            listener.onMarkAsCompleted(dataList[adapterPosition].id)
                            true
                        }

                        R.id.action_deleted -> {
                            listener.onDeleted(dataList[adapterPosition].id)
                            true
                        }

                        else -> false
                    }
                }
                popupMenu.show()
                true
            }

            binding.root.setOnClickListener {
                listener.onClick(dataList[adapterPosition].id)
            }
        }

        fun bind(task: Task) {
            binding.textTitleTask.text = task.title
            binding.textDescriptionTask.text = task.description

            var imageId = 0
            when (task.state) {
                Task.STATE_PENDING -> imageId = R.drawable.ic_pending_task
                Task.STATE_COMPLETED -> imageId = R.drawable.ic_completed_task
            }
            binding.imageStatusTask.setImageResource(imageId)
        }
    }

    interface OnTaskListener {
        fun onMarkAsCompleted(id: String)
        fun onDeleted(id: String)
        fun onClick(id: String)
    }
}