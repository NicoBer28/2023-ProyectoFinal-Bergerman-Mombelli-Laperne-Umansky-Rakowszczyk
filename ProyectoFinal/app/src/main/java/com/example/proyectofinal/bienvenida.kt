package com.example.proyectofinal

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class bienvenida : Fragment() {

    companion object {
        fun newInstance() = bienvenida()
    }

    private lateinit var viewModel: BienvenidaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bienvenida, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BienvenidaViewModel::class.java)
        // TODO: Use the ViewModel
    }

}