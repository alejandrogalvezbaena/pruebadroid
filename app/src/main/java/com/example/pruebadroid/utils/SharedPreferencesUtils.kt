package com.example.pruebadroid.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.pruebadroid.data.model.Character
import com.example.pruebadroid.data.model.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SharedPreferencesUtils {
    private const val TASK_PREFERENCES = "task_preferences"
    private const val TASK_PREFERENCES_LIST = "task_preferences_list"
    private const val RICKMORTY_PREFERENCES = "rickmorty_preferences"
    private const val RICKMORTY_PREFERENCES_LIST = "rickmorty_preferences_list"

    private fun getTaskPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences(TASK_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun loadTaskList(application: Application): ArrayList<Task> {
        val sharedPreferences = getTaskPreferences(application)
        val json = sharedPreferences.getString(TASK_PREFERENCES_LIST, "[]")
        val type = object : TypeToken<ArrayList<Task>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun saveTaskList(application: Application, taskList: ArrayList<Task>) {
        val sharedPreferences = getTaskPreferences(application)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(taskList)
        editor.putString(TASK_PREFERENCES_LIST, json)
        editor.apply()
    }

    private fun getRickMortyPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences(RICKMORTY_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun loadRickMortyList(application: Application): ArrayList<Character> {
        val sharedPreferences = getRickMortyPreferences(application)
        val json = sharedPreferences.getString(RICKMORTY_PREFERENCES_LIST, "[]")
        val type = object : TypeToken<ArrayList<Character>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun saveRickMortyList(application: Application, characterList: ArrayList<Character>) {
        val sharedPreferences = getRickMortyPreferences(application)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(characterList)
        editor.putString(RICKMORTY_PREFERENCES_LIST, json)
        editor.apply()
    }
}