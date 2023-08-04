package com.example.proyectofinal.ViewModelsFragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    var nombreUsuario : MutableLiveData<String> = MutableLiveData()

    fun setUsuario (usuario: String){
        nombreUsuario.value = usuario
    }
}