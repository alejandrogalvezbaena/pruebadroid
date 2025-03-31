package com.example.pruebadroid.ui.task

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pruebadroid.data.model.Task
import com.example.pruebadroid.utils.SharedPreferencesUtils

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val _filterTaskList = MutableLiveData<ArrayList<Task>>()
    val filterTaskListLiveData: LiveData<ArrayList<Task>> get() = _filterTaskList
    private var taskListOriginal: ArrayList<Task> = ArrayList()

    private val _task = MutableLiveData<Task>()
    val taskLiveData: LiveData<Task> get() = _task

    private val _saveOK = MutableLiveData(false)
    val saveOkLiveData: LiveData<Boolean> get() = _saveOK

    var filterID: String = Task.STATE_ALL

    private fun applyChanges(taskList: ArrayList<Task>) {
        SharedPreferencesUtils.saveTaskList(getApplication(), taskList)
        loadTaskList()
    }

    fun loadTaskList() {
        taskListOriginal = SharedPreferencesUtils.loadTaskList(getApplication())
        when (filterID) {
            Task.STATE_PENDING -> {
                val taskListPending =
                    ArrayList(taskListOriginal.filter { it.state == Task.STATE_PENDING })
                _filterTaskList.postValue(taskListPending)
            }

            Task.STATE_COMPLETED -> {
                val taskListCompleted =
                    ArrayList(taskListOriginal.filter { it.state == Task.STATE_COMPLETED })
                _filterTaskList.postValue(taskListCompleted)
            }

            else -> _filterTaskList.postValue(taskListOriginal)
        }
    }

    fun applyFilter(filterID: String) {
        this.filterID = filterID
        loadTaskList()
    }

    fun loadTask(id: String) {
        taskListOriginal.find { it.id == id }?.let { task -> _task.postValue(task) }
    }

    fun createTask(title: String, description: String, date: String, state: String) {
        val taskListNew = ArrayList(taskListOriginal)
        val idNew = if (taskListNew.isEmpty()) 0 else ((taskListNew[taskListNew.size - 1].id + 1))
        taskListNew.add(Task(idNew.toString(), title, description, date, state))

        applyChanges(taskListNew)
        _saveOK.postValue(true)
    }

    fun editTask(id: String, title: String, description: String, date: String, state: String) {
        val taskListNew = ArrayList(taskListOriginal)
        val task = taskListNew.find { it.id == id }
        task?.title = title
        task?.description = description
        task?.date = date
        task?.state = state
        applyChanges(taskListNew)
        _saveOK.postValue(true)
    }

    fun markAsCompleted(id: String) {
        val taskListNew = ArrayList(taskListOriginal)
        val task = taskListNew.find { it.id == id }
        task?.state = Task.STATE_COMPLETED
        applyChanges(taskListNew)
    }

    fun deleteTask(id: String) {
        val taskListNew = ArrayList(taskListOriginal)
        val task = taskListNew.find { it.id == id }
        taskListNew.remove(task)
        applyChanges(taskListNew)
    }

    fun resetViewModel() {
        _saveOK.postValue(false)
    }
}