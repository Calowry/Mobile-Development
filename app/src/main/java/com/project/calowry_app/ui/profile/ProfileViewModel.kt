package com.project.calowry_app.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class ProfileViewModel : ViewModel() {
    val userName = MutableLiveData<String>()
    val calorieCount = MutableLiveData<Int>()
    val name = MutableLiveData<String>()
    val age = MutableLiveData<Int>()
    val weight = MutableLiveData<Float>()
    val height = MutableLiveData<Float>()
    val isMale = MutableLiveData<Boolean>()
    val isFemale = MutableLiveData<Boolean>()

    fun saveButton() {
        // Implementasi logika yang sesuai untuk menyimpan data
    }

    fun openLanguageSettings() {
        // Implementasi logika yang sesuai untuk membuka pengaturan bahasa
    }
}