 package com.example.proyectofinal.Fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.util.Log
import android.widget.ProgressBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.proyectofinal.ViewModelsFragments.LogInViewModel
import com.example.proyectofinal.R
import com.example.proyectofinal.ViewModelsFragments.SharedViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


lateinit var botonLogin: Button
lateinit var botonRegistro: Button
lateinit var botonRestablecerContra: Button
lateinit var usuario: EditText
lateinit var clave : EditText
var flagFuncionUbicacion : Int = 1


lateinit var usuarioIngresado : String
lateinit var claveIngresada : String

lateinit var locationManager: LocationManager

class LogIn : Fragment() {

    private lateinit var viewModel: LogInViewModel

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var auth: FirebaseAuth

    private val requestcode = 1

    lateinit var spinner: ProgressBar



    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_log_in, container, false)
        spinner = view.findViewById(R.id.progressBarLogIn)
        spinner.visibility = View.GONE // Para mostrar el ProgressBar
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Cloud Firestore instance
        val db = Firebase.firestore

        // Initialize Firebase Auth
        auth = Firebase.auth

        botonLogin = view.findViewById<Button>(R.id.btnLogin)
        botonRegistro = view.findViewById<Button>(R.id.btnRegister2)
        botonRestablecerContra = view.findViewById<Button>(R.id.restablecerContra)

        usuario = view.findViewById<EditText>(R.id.username)
        clave = view.findViewById<EditText>(R.id.password)
        //getLocation()

        botonLogin.setOnClickListener {

            usuarioIngresado = usuario.text.toString()
            claveIngresada = clave.text.toString()

            if (usuarioIngresado.isEmpty() || claveIngresada.isEmpty()) {
                val snackbar =
                    Snackbar.make(it, "Complete todos los campos", Snackbar.LENGTH_SHORT)
                snackbar.show()
            } else {
                auth.signInWithEmailAndPassword(usuarioIngresado, claveIngresada)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            spinner.visibility = View.VISIBLE // Para mostrar el ProgressBar
                            val userActual = FirebaseAuth.getInstance().currentUser
                            val idUsuarioActual = userActual?.uid
                            sharedViewModel.setUsuario(usuarioIngresado)
                            findNavController().navigate(R.id.mainActivity2)
                        }

                         else {
                            val snackbar =
                                Snackbar.make(it, "MAL", Snackbar.LENGTH_SHORT)
                            snackbar.show()
                        }
                    }
            }

        }

        botonRegistro.setOnClickListener {
            findNavController().navigate(R.id.register_fragment)
        }

        botonRestablecerContra.setOnClickListener {
            usuarioIngresado = usuario.text.toString()

            val emailAddress = usuarioIngresado

            if (usuarioIngresado.isEmpty()) {
                val snackbar =
                    Snackbar.make(it, "Ponga su email", Snackbar.LENGTH_SHORT)
                snackbar.show()
            } else {

                if (emailAddress != null) {
                    auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val snackbar =
                                    Snackbar.make(it, "Contraseña enviada", Snackbar.LENGTH_SHORT)
                                snackbar.show()
                            } else {
                                val snackbar =
                                    Snackbar.make(it, "Email incorrecto", Snackbar.LENGTH_SHORT)
                                snackbar.show()
                            }
                        }
                }
            }
        }
    }

}
