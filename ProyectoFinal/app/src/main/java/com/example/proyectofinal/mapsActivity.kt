package com.example.proyectofinal

import android.app.PendingIntent.getActivity
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
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

class mapsActivity : AppCompatActivity(), OnMapReadyCallback{
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    private lateinit var googleMap: GoogleMap
    var latitudUser: Double = 10.0
    var longitudUser: Double = 10.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val btnUbiActual = findViewById<Button>(R.id.btnUbiActual)
        val userActual = FirebaseAuth.getInstance().currentUser
        val idUsuarioActual = userActual?.uid

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if(document.id == idUsuarioActual){
                        //Toast.makeText(this, (document.get("Latitud") as Double).toString(), Toast.LENGTH_SHORT).show();

                        latitudUser = document.get("Latitud") as Double
                        longitudUser = document.get("Longitud") as Double

                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        btnUbiActual.setOnClickListener{
            googleMap.addMarker(
                MarkerOptions()
                    .position(LatLng(latitudUser, longitudUser))
                    .title("Marker")
                    .snippet("Population: 4,137,400")
            )
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.mapFragment) as SupportMapFragment
            mapFragment.getMapAsync{ googleMap ->
                val cameraPosition = CameraPosition.Builder()
                    .target(LatLng(latitudUser, longitudUser))
                    .zoom(15f)
                    .build()
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition)) }


        }



        //Toast.makeText(this, latitudUser.toString(), Toast.LENGTH_SHORT).show();

    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

    }

}