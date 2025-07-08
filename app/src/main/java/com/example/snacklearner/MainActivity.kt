package com.example.snacklearner

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.commit
import com.example.snacklearner.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private var isAdmin: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        binding.navigationView.setNavigationItemSelectedListener(this)

        isAdmin = intent.getBooleanExtra("isAdmin", false)

        if (isAdmin) {
            binding.fab.visibility = View.VISIBLE
        } else {
            binding.fab.visibility = View.GONE
        }

        binding.fab.setOnClickListener {
            Toast.makeText(this, "Dodavanje novog recepta", Toast.LENGTH_SHORT).show()
            // Ovdje otvori AddRecipeFragment
        }

        supportFragmentManager.commit {
            replace(R.id.fragmentContainer, SearchFragment())
        }
    }

    fun getToolbar() = binding.toolbar
    fun getDrawerLayout() = binding.drawerLayout

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
