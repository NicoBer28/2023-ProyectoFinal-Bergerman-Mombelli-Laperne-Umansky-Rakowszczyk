package com.example.proyectofinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
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

lateinit var usuarioIngresado : String
lateinit var claveIngresada : String

class LogIn : Fragment() {

    private lateinit var viewModel: LogInViewModel

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_log_in, container, false)
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

        botonLogin.setOnClickListener {
            usuarioIngresado = usuario.text.toString()
            claveIngresada = clave.text.toString()

            if(usuarioIngresado.isEmpty() || claveIngresada.isEmpty()) {
                val snackbar =
                    Snackbar.make(it, "Complete todos los campos", Snackbar.LENGTH_SHORT)
                snackbar.show()
            }else {

                auth.signInWithEmailAndPassword(usuarioIngresado, claveIngresada)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {

                            sharedViewModel.setUsuario(usuarioIngresado)
                            findNavController().navigate(R.id.mapsActivity)
                        } else {
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

            if(usuarioIngresado.isEmpty()){
                val snackbar =
                    Snackbar.make(it, "Ponga su email", Snackbar.LENGTH_SHORT)
                snackbar.show()
            }else {

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
