package com.example.snacklearner

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.snacklearner.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsFragment : PreferenceFragmentCompat() {

    private var currentUsername: String? = null

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

        val editProfilePref = findPreference<Preference>("edit_profile")
        editProfilePref?.setOnPreferenceClickListener {
            currentUsername?.let { username ->
                val bundle = Bundle().apply {
                    putString("username", username)
                }
                findNavController().navigate(R.id.action_settingsFragment_to_editProfileFragment, bundle)
            }
            true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundColor(Color.parseColor("#F5F5DC"))


        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(requireContext())
            val user = db.userDao().getLastLoggedInUser()
            currentUsername = user?.username
        }
    }
}

