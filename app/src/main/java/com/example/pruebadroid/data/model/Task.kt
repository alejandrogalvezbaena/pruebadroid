package com.example.pruebadroid.data.model

data class Task(val id: String, var title: String, var description: String, var date: String,
                var state: String){
    companion object{
        const val STATE_PENDING = "0"
        const val STATE_COMPLETED = "1"
        const val STATE_ALL = "-1"
    }
}