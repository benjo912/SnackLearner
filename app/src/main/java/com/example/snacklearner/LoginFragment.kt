package com.example.snacklearner

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.snacklearner.data.AppDatabase
import com.example.snacklearner.data.UserEntity
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var db: AppDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = requireActivity() as MainActivity
        db = (activity.application as SnackLearnerApp).database

        val usernameEditText = view.findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = view.findViewById<EditText>(R.id.passwordEditText)
        val loginButton = view.findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            lifecycleScope.launch {
                val user = db.userDao().getUserByUsername(username)

                if (user != null && user.password == password) {
                    Toast.makeText(requireContext(), "Dobrodošao ${user.role}", Toast.LENGTH_SHORT).show()
                    activity.getToolbar().visibility = View.VISIBLE
                    activity.getDrawerLayout().setDrawerLockMode(androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED)

                    val bundle = Bundle().apply { putString("role", user.role) }
                    val searchFragment = SearchFragment()
                    searchFragment.arguments = bundle

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, searchFragment)
                        .commit()
                } else if (user == null) {
                    val newUser = UserEntity(username, password, "user")
                    db.userDao().insertUser(newUser)
                    Toast.makeText(requireContext(), "Registracija uspješna", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Pogrešna lozinka", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
