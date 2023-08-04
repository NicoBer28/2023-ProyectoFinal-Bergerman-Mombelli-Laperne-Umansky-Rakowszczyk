package com.example.proyectofinal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.proyectofinal.Fragments.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

lateinit var nombre_vendedor: EditText
lateinit var email_vendedor: EditText
lateinit var direccion_vendedor: EditText
lateinit var password_vendedor : EditText
lateinit var confirmar_password_vendedor: EditText
lateinit var btnConfirmar_registro_vendedor: Button

lateinit var nombreVendedor : String
lateinit var emailVendedor : String
lateinit var direccionVendedor : String
lateinit var contraseñaVendedor : String
lateinit var contraConfVendedor : String

var flag1 = 0

class registrar_vendedor_fragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vendedores, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = Firebase.firestore

        auth = Firebase.auth

        btnRegister_vendedor = view.findViewById<Button>(R.id.btnRegister_vendedor)
        btnConfirmar_registro_vendedor = view.findViewById<Button>(R.id.btnConfirmar_registro_vendedor)
        nombre_vendedor = view.findViewById<EditText>(R.id.nombre_vendedor)
        email_vendedor = view.findViewById<EditText>(R.id.email_vendedor)
        direccion_vendedor = view.findViewById<EditText>(R.id.direccion_vendedor)
        password_vendedor = view.findViewById<EditText>(R.id.vendedor_password)
        confirmar_password_vendedor = view.findViewById<EditText>(R.id.confirmar_password_vendedor)

        registerConfirm.setOnClickListener {
            nombreVendedor = nombre_vendedor.text.toString()
            emailVendedor = email.text.toString()
            direccionVendedor = direccion.text.toString()
            contraseñaVendedor = passwordCreate.text.toString()
            contraConfVendedor = passwordConfirm.text.toString()

            var usuarios: MutableList<String> = mutableListOf(
                nombreVendedor,
                emailVendedor,
                userIngresado,
                contraseñaVendedor,
                contraConfVendedor)

            for(campos in usuarios){
                if(campos == ""){
                    flag = 1
                }
            }

            if(contraseñaVendedor == contraConfVendedor && flag == 0) {


                auth.createUserWithEmailAndPassword(emailVendedor, contraseñaVendedor)
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
                                "Nombre" to nombreVendedor,
                                "Email" to emailVendedor,
                                "Direccion" to direccionVendedor,
                                "Contraseña" to contraseñaVendedor
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

    }
}

