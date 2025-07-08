package com.example.snacklearner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.snacklearner.data.AppDatabase
import com.example.snacklearner.data.UserEntity
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var fullNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var registerButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usernameEditText = view.findViewById(R.id.editTextUsername)
        passwordEditText = view.findViewById(R.id.editTextPassword)
        fullNameEditText = view.findViewById(R.id.editTextFullName)
        emailEditText = view.findViewById(R.id.editTextEmail)
        registerButton = view.findViewById(R.id.buttonRegister)

        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val fullName = fullNameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()

            if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
                Toast.makeText(context, "Popunite sva polja", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(requireContext())
                val userDao = db.userDao()

                val existingUser = userDao.getUserByUsername(username)
                if (existingUser != null) {
                    Toast.makeText(context, "Korisnik već postoji", Toast.LENGTH_SHORT).show()
                } else {
                    val newUser = UserEntity(
                        username = username,
                        password = password,
                        fullName = fullName,
                        email = email,
                        isAdmin = false
                    )

                    val insertedId = userDao.insertUser(newUser)

                    val sharedPref = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                    sharedPref.edit()
                        .putInt("user_id", insertedId.toInt())
                        .putString("username", newUser.username)
                        .putBoolean("isAdmin", newUser.isAdmin)
                        .apply()

                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        }
    }
}
