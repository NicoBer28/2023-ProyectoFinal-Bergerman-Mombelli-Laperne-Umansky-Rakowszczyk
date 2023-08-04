package com.example.proyectofinal.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import com.example.proyectofinal.R
import com.example.proyectofinal.Fragments.MapaFragment
import com.example.proyectofinal.Fragments.ThreeFragment
import com.example.proyectofinal.Fragments.TwoFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MapaActivity : AppCompatActivity() {

    lateinit var bottomNav: BottomNavigationView

    private val oneFragment = MapaFragment()
    private val twoFragment = TwoFragment()
    private val threeFragment = ThreeFragment()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa)

        bottomNav = findViewById(R.id.bottomNav)

        bottomNav.setOnNavigationItemSelectedListener() {
            when (it.itemId) {
                R.id.one -> replaceFragment((oneFragment))
                R.id.two -> replaceFragment((twoFragment))
                R.id.three -> replaceFragment((threeFragment))
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView3, fragment)
            transaction.commit()
        }
    }
}

