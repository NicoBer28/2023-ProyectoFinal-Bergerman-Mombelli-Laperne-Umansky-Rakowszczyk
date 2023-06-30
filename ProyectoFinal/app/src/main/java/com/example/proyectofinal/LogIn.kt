 package com.example.proyectofinal

import android.Manifest
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
var flagFuncionUbicacion : Int = 0


lateinit var usuarioIngresado : String
lateinit var claveIngresada : String

lateinit var locationManager: LocationManager

class LogIn : Fragment() {

    private lateinit var viewModel: LogInViewModel

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var auth: FirebaseAuth

    private val requestcode = 1


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
        botonRegistro = view.findViewById<Button>(R.id.btnRegister)
        botonRestablecerContra = view.findViewById<Button>(R.id.restablecerContra)

        usuario = view.findViewById<EditText>(R.id.username)
        clave = view.findViewById<EditText>(R.id.password)
        getLocation()

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
                            if(flagFuncionUbicacion == 0){
                                getLocation()
                            }else {
                                val userActual = FirebaseAuth.getInstance().currentUser
                                val idUsuarioActual = userActual?.uid
                                var latitudUser: Double = 10.0
                                var longitudUser: Double = 10.0

                                db.collection("users")
                                    .get()
                                    .addOnSuccessListener { result ->
                                        for (document in result) {
                                            if(document.id == idUsuarioActual){
                                                latitudUser = document.get("Latitud") as Double
                                                longitudUser = document.get("Longitud") as Double

                                            }
                                        }

                                    }
                                    .addOnFailureListener { exception ->
                                        Log.w(ContentValues.TAG, "Error getting documents.", exception)
                                    }
                                sharedViewModel.setUsuario(usuarioIngresado)
                                findNavController().navigate(R.id.mainActivity2)
                            }

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

    private fun getLocation() {

        var locationGps: Location? = null
        var locationNetwork: Location? = null

        val uid = Firebase.auth.currentUser?.uid
        locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        //Pedir permiso de ubicacion

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                requestcode
            )
        }

        if (hasGps || hasNetwork) {

            if (hasGps) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000,
                    0F,
                    object : LocationListener {
                        override fun onLocationChanged(p0: Location) {
                            if (p0 != null) {
                                locationGps = p0
                                if (uid != null) {
                                    Firebase.firestore.collection("users").document(uid).update(
                                        "Longitud",
                                        locationGps!!.longitude,
                                        "Latitud",
                                        locationGps!!.latitude
                                    )
                                        .addOnSuccessListener {

                                            flagFuncionUbicacion = 1
                                            /*val snackbar =
                                                Snackbar.make(
                                                    requireView(),
                                                    "Location Data feeds start",
                                                    Snackbar.LENGTH_SHORT
                                                )
                                            snackbar.show()
                                            */

                                        }
                                        .addOnFailureListener {
                                            val snackbar =
                                                Snackbar.make(
                                                    requireView(),
                                                    "Failed location feed",
                                                    Snackbar.LENGTH_SHORT
                                                )
                                            snackbar.show()
                                        }
                                }
                            }
                        }

                    })

                val localGpsLocation =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (localGpsLocation != null)
                    locationGps = localGpsLocation
            }
            if (hasNetwork) {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    5000,
                    0F,
                    object : LocationListener {
                        override fun onLocationChanged(p0: Location) {
                            if (p0 != null) {
                                locationNetwork = p0
                                if (uid != null) {
                                    Firebase.firestore.collection("Users").document(uid)
                                        .update(
                                            "Longitud",
                                            locationNetwork!!.longitude,
                                            "Latitud",
                                            locationNetwork!!.latitude
                                        )
                                        .addOnSuccessListener {
                                            flagFuncionUbicacion = 1

                                            val snackbar =
                                                Snackbar.make(
                                                    requireView(),
                                                    "Location Data feeds start",
                                                    Snackbar.LENGTH_SHORT
                                                )
                                            snackbar.show()
                                        }
                                        .addOnFailureListener {
                                            val snackbar =
                                                Snackbar.make(
                                                    requireView(),
                                                    "Failed location feed",
                                                    Snackbar.LENGTH_SHORT
                                                )
                                            snackbar.show()
                                        }
                                }
                            }
                        }

                    })

                val localNetworkLocation =
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (localNetworkLocation != null)
                    locationNetwork = localNetworkLocation
            }

            if (locationGps != null && locationNetwork != null) {
                if (locationGps!!.accuracy > locationNetwork!!.accuracy) {
                    if (uid != null) {
                        Firebase.firestore.collection("Users").document(uid).update(
                            "Longitud",
                            locationGps!!.longitude, "Latitud", locationGps!!.latitude
                        )
                            .addOnSuccessListener {
                                flagFuncionUbicacion = 1

                                val snackbar =
                                    Snackbar.make(
                                        requireView(),
                                        "Location Data feeds start",
                                        Snackbar.LENGTH_SHORT
                                    )
                                snackbar.show()
                            }
                            .addOnFailureListener {
                                val snackbar =
                                    Snackbar.make(
                                        requireView(),
                                        "Failed location feed",
                                        Snackbar.LENGTH_SHORT
                                    )
                                snackbar.show()
                            }
                    }
                } else {
                    if (uid != null) {
                        Firebase.firestore.collection("Users").document(uid).update(
                            "Longitud",
                            locationNetwork!!.longitude, "Latitud", locationNetwork!!.latitude
                        )
                            .addOnSuccessListener {
                                flagFuncionUbicacion = 1
                                val snackbar =
                                    Snackbar.make(
                                        requireView(),
                                        "Location Data feeds start",
                                        Snackbar.LENGTH_SHORT
                                    )
                                snackbar.show()
                            }
                            .addOnFailureListener {
                                val snackbar =
                                    Snackbar.make(
                                        requireView(),
                                        "Failed location feed",
                                        Snackbar.LENGTH_SHORT
                                    )
                                snackbar.show()
                            }
                    }
                }
            }

        }// else {
          //  startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        //}
    }
}
