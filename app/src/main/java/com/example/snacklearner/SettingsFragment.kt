package com.example.snacklearner

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val logoutPref = findPreference<Preference>("logout")
        logoutPref?.setOnPreferenceClickListener {
            Toast.makeText(requireContext(), "Odjavljeni ste", Toast.LENGTH_SHORT).show()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, LoginFragment())
                .commit()
            true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        view.setBackgroundColor(Color.parseColor("#F5F5DC"))
        super.onViewCreated(view, savedInstanceState)
    }
}
