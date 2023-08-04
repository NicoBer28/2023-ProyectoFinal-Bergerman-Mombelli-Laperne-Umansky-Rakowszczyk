package com.example.proyectofinal.Fragments

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.proyectofinal.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MapaFragment : Fragment(), OnMapReadyCallback {

    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    private lateinit var googleMap: GoogleMap
    var latitudUser: Any? = 0.0
    var longitudUser: Any? = 0.0
    var requestcode = 1
    var flagFuncionUbicacion = 0
    lateinit var spinner: ProgressBar


    private val callback = OnMapReadyCallback { googleMap ->
        this.googleMap = googleMap
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                requestcode
            )
        } else {

            // Permissions already granted, proceed with location updates
            getLocation()
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_mapa, container, false)

        val botonUbiActual = view.findViewById<Button>(R.id.botonUbiActual)

        val userActual = FirebaseAuth.getInstance().currentUser
        val idUsuarioActual = userActual?.uid

        spinner = view.findViewById(R.id.progressBar)
        spinner.visibility = View.VISIBLE // Para mostrar el ProgressBar

        var mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)


/*
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if(document.id == idUsuarioActual){
                        //Toast.makeText(this, (document.get("Latitud") as Double).toString(), Toast.LENGTH_SHORT).show();
                        latitudUser = (document.get("Latitud") as Number).toDouble()
                        longitudUser = (document.get("Longitud") as Number).toDouble()

                        spinner.visibility = View.GONE // Para ocultar el ProgressBar

                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }*/


        botonUbiActual.setOnClickListener {

            mapFragment = childFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            googleMap.addMarker(
                MarkerOptions()
                    .position(LatLng(latitudUser as Double, longitudUser as Double))
                    .title("Marker")
                    .snippet("Population: 4,137,400")
            )
            mapFragment!!.getMapAsync { googleMap ->
                val cameraPosition = CameraPosition.Builder()
                    .target(LatLng(latitudUser as Double, longitudUser as Double))
                    .zoom(15f)
                    .build()
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
    }


    private fun getLocation() {
        Toast.makeText(requireContext(), "aaa.", Toast.LENGTH_SHORT).show()

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
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
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
                                            latitudUser = locationGps!!.latitude
                                            longitudUser = locationGps!!.longitude
                                            spinner.visibility = View.GONE // Para ocultar el ProgressBar

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
                                            latitudUser = locationGps!!.latitude
                                            longitudUser = locationGps!!.longitude
                                            spinner.visibility = View.GONE // Para ocultar el ProgressBar

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
                                latitudUser = locationGps!!.latitude
                                longitudUser = locationGps!!.longitude
                                spinner.visibility = View.GONE // Para ocultar el ProgressBar

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
                                latitudUser = locationGps!!.latitude
                                longitudUser = locationGps!!.longitude
                                spinner.visibility = View.GONE // Para ocultar el ProgressBar

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


        } else {
          //startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            val snackbar =
                Snackbar.make(
                    requireView(),
                    "Active la ubicacion",
                    Snackbar.LENGTH_SHORT
                )
            snackbar.show()
            spinner.visibility = View.GONE // Para ocultar el ProgressBar

        }


    }



}
