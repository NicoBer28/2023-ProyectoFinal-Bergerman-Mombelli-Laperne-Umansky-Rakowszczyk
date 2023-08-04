package com.example.proyectofinal.Pruebas

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.proyectofinal.Fragments.botonLogin
import com.example.proyectofinal.R

class bienvenida : Fragment() {

    /*companion object {
        fun newInstance() = bienvenida()
    }

    private lateinit var viewModel: BienvenidaViewModel

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var textoWelcome: TextView

    */@SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bienvenida, container, false)

        botonLogin = view.findViewById<Button>(R.id.button3)

        botonLogin.setOnClickListener{
            findNavController().navigate(R.id.navigation2)
        }
/*
        textoWelcome = view.findViewById(R.id.textoWelcome)

        sharedViewModel.nombreUsuario.observe(viewLifecycleOwner, Observer {  usuario ->
            textoWelcome.text = usuario

        })

*/
        return view
    }




}