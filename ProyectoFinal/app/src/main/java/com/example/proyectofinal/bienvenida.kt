package com.example.proyectofinal

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer

class bienvenida : Fragment() {

    companion object {
        fun newInstance() = bienvenida()
    }

    private lateinit var viewModel: BienvenidaViewModel

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var textoWelcome: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bienvenida, container, false)

        textoWelcome = view.findViewById(R.id.textoWelcome)

        sharedViewModel.nombreUsuario.observe(viewLifecycleOwner, Observer {  usuario ->
            textoWelcome.text = usuario

        })


        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BienvenidaViewModel::class.java)
        // TODO: Use the ViewModel
    }

}