package com.example.proyectofinal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.annotation.SuppressLint
import android.view.Menu
import android.view.MenuInflater
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity2 : AppCompatActivity() {

    lateinit var bottomNav: BottomNavigationView

    private val oneFragment = OneFragment()
    private val twoFragment = TwoFragment()
    private val threeFragment = ThreeFragment()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

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
            transaction.replace(R.id.fragmentContainerView9, fragment)
            transaction.commit()
        }
    }
}
