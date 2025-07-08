package com.example.snacklearner

import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.navigation.NavigationView

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val sharedPref = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", null) ?: "Nepoznat korisnik"
        val isAdmin = sharedPref.getBoolean("is_admin", false)

        findPreference<Preference>("user_info")?.summary = username

        findPreference<Preference>("edit_profile")?.setOnPreferenceClickListener {
            val bundle = bundleOf("username" to username)
            findNavController().navigate(R.id.action_settingsFragment_to_editProfileFragment, bundle)
            true
        }

        findPreference<Preference>("logout")?.setOnPreferenceClickListener {
            sharedPref.edit().clear().apply()
            findNavController().navigate(R.id.action_settingsFragment_to_loginFragment)
            true
        }

        val navView = requireActivity().findViewById<NavigationView>(R.id.navigationView)
        navView.menu.findItem(R.id.nav_manage_users)?.isVisible = isAdmin
    }
}
