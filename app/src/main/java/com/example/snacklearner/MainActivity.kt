package com.example.snacklearner

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.snacklearner.data.AppDatabase
import com.example.snacklearner.data.UserEntity
import com.example.snacklearner.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private var currentUser: UserEntity? = null
    private var isAdmin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.navigationView.setupWithNavController(navController)

        CoroutineScope(Dispatchers.Main).launch {
            val db = AppDatabase.getDatabase(this@MainActivity)
            currentUser = db.userDao().getLastLoggedInUser()
            isAdmin = currentUser?.isAdmin == true

            val menu = binding.navigationView.menu
            val manageUsersItem = menu.findItem(R.id.nav_manage_users)
            manageUsersItem?.isVisible = isAdmin

            binding.fab.setOnClickListener {
                navController.navigate(R.id.addRecipeFragment)
            }
        }

        binding.toolbar.setNavigationIcon(R.drawable.ic_menu_hamburger)
        binding.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        return when (item.itemId) {
            R.id.nav_all_recipes -> {
                navController.navigate(R.id.searchFragment)
                true
            }
            R.id.nav_settings -> {
                navController.navigate(R.id.settingsFragment)
                true
            }
            R.id.nav_manage_users -> {
                if (isAdmin) {
                    navController.navigate(R.id.adminUserListFragment)
                } else {
                    Toast.makeText(this, "Nemate dozvolu za pristup korisnicima.", Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> false
        }.also {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
    }
}
