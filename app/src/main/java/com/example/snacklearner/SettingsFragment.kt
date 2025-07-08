package com.example.snacklearner

import android.content.Context
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.snacklearner.data.AppDatabase
import kotlinx.coroutines.launch

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)

        val sharedPreferences = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("user_id", -1)

        val editProfilePref = findPreference<Preference>("edit_profile")
        val logoutPref = findPreference<Preference>("logout")
        val manageUsersPref = findPreference<Preference>("manage_users")
        val userInfoPref = findPreference<Preference>("user_info")

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            val user = db.userDao().getUserById(userId)

            if (user != null) {
                userInfoPref?.summary = user.username

                if (user.isAdmin) {
                    manageUsersPref?.isVisible = true
                    manageUsersPref?.setOnPreferenceClickListener {
                        findNavController().navigate(R.id.adminUserListFragment)
                        true
                    }
                } else {
                    manageUsersPref?.isVisible = false
                }
            }
        }

        editProfilePref?.setOnPreferenceClickListener {
            findNavController().navigate(R.id.editProfileFragment)
            true
        }

        logoutPref?.setOnPreferenceClickListener {
            sharedPreferences.edit().clear().apply()
            findNavController().navigate(R.id.loginFragment)
            true
        }
    }
}
