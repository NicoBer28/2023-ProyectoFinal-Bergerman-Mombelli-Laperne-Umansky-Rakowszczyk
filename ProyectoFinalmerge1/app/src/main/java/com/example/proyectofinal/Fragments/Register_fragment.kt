package com.example.proyectofinal.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.proyectofinal.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

lateinit var name: EditText
lateinit var apellido : EditText
lateinit var email: EditText
lateinit var direccion: EditText
lateinit var edad: EditText
lateinit var usernameCreate: EditText
lateinit var passwordCreate : EditText
lateinit var  passwordConfirm: EditText

lateinit var btnPreviousFragment: Button
lateinit var registerConfirm: Button

lateinit var nombreIngresado : String
lateinit var apellidoIngresado : String
lateinit var emailIngresado : String
lateinit var direccionIngresada : String
lateinit var edadIngresada : String
lateinit var userIngresado : String
lateinit var contraIngresada : String
lateinit var contraConfIngresada : String

var flag = 0

class Register_fragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = Firebase.firestore

        auth = Firebase.auth

        btnPreviousFragment = view.findViewById<Button>(R.id.btnPreviousFragment)
        registerConfirm = view.findViewById<Button>(R.id.registerConfirm)
        name = view.findViewById<EditText>(R.id.name)
        apellido = view.findViewById<EditText>(R.id.apellido)
        email = view.findViewById<EditText>(R.id.email)
        direccion = view.findViewById<EditText>(R.id.direccion)
        edad = view.findViewById<EditText>(R.id.edad)
        usernameCreate = view.findViewById<EditText>(R.id.usernameCreate)
        passwordCreate = view.findViewById<EditText>(R.id.passwordCreate)
        passwordConfirm = view.findViewById<EditText>(R.id.passwordConfirm)

        registerConfirm.setOnClickListener {
            nombreIngresado = name.text.toString()
            apellidoIngresado = apellido.text.toString()
            emailIngresado = email.text.toString()
            direccionIngresada = direccion.text.toString()
            edadIngresada = edad.text.toString()
            userIngresado = usernameCreate.text.toString()
            contraIngresada = passwordCreate.text.toString()
            contraConfIngresada = passwordConfirm.text.toString()

            var usuarios: MutableList<String> = mutableListOf(nombreIngresado,
                apellidoIngresado,
                emailIngresado,
                edadIngresada,
                userIngresado,
                contraIngresada,
                contraConfIngresada)

            for(campos in usuarios){
                if(campos == ""){
                    flag = 1
                }
            }

            if(contraIngresada == contraConfIngresada && flag == 0) {


                auth.createUserWithEmailAndPassword(emailIngresado, contraIngresada)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            // Get the currently authenticated user
                            val userActual = FirebaseAuth.getInstance().currentUser

                            // Create a new user document with the user's ID as the document ID
                            val userDocRef = userActual?.let { it1 ->
                                db.collection("users").document(
                                    it1.uid
                                )
                            }

                            // Set the data for the user document
                            val userData = hashMapOf(
                                "Nombre" to nombreIngresado,
                                "Apellido" to apellidoIngresado,
                                "Email" to emailIngresado,
                                "Direccion" to direccionIngresada,
                                "Edad" to edadIngresada,
                                "User" to userIngresado,
                                "Contraseña" to contraIngresada
                            )

                            userDocRef?.set(userData)?.addOnSuccessListener {

                            }?.addOnFailureListener { e ->
                                val snackbar =
                                    Snackbar.make(it, "usuario creado", Snackbar.LENGTH_SHORT)
                                snackbar.show()
                            }

                            val snackbar =
                                Snackbar.make(it, "usuario creado", Snackbar.LENGTH_SHORT)
                            snackbar.show()

                            findNavController().navigate(R.id.mainActivity2)

                        } else {
                            // If sign in fails, display a message to the user.

                            val snackbar = Snackbar.make(it, "La contraseña debe contener al menos 6 caracteres", Snackbar.LENGTH_SHORT)
                            snackbar.show()
                        }
                    }
        }else {
                if (flag == 1) {
                    flag = 0
                    val snackbar =
                        Snackbar.make(it, "Hay campos vacios", Snackbar.LENGTH_SHORT)
                    snackbar.show()
                } else {
                val snackbar =
                    Snackbar.make(it, "Las contraseñas no coinciden", Snackbar.LENGTH_SHORT)
                snackbar.show()
                }
        }
     }

        btnPreviousFragment.setOnClickListener {
            findNavController().navigate(R.id.logIn)
        }
    }
}

