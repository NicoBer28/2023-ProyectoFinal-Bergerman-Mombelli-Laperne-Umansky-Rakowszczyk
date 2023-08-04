package com.example.proyectofinal.Activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.proyectofinal.R

class LogueoActivity : AppCompatActivity() {

    /*lateinit var bottomNav : BottomNavigationView

    private val oneFragment = OneFragment()
    private val twoFragment = TwoFragment()
    private val threeFragment = ThreeFragment()*/

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logueo)
    }
}

        /*bottomNav = findViewById(R.id.bottomNav)

        bottomNav.setOnNavigationItemSelectedListener() {
            when(it.itemId){
                R.id.one -> replaceFragment((oneFragment))
                R.id.two -> replaceFragment((twoFragment))
                R.id.three -> replaceFragment((threeFragment))
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView9, fragment)
            transaction.commit()
        }
    }*/
