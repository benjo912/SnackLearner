package com.example.snacklearner
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.snacklearner.data.AppDatabase
import com.example.snacklearner.data.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditProfileFragment : Fragment() {

    private lateinit var fullNameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var saveButton: Button

    private var currentUser: UserEntity? = null
    private var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fullNameEditText = view.findViewById(R.id.editTextFullName)
        emailEditText = view.findViewById(R.id.editTextEmail)
        passwordEditText = view.findViewById(R.id.editTextPassword)
        saveButton = view.findViewById(R.id.buttonSave)

        username = arguments?.getString("username")

        if (username == null) {
            Toast.makeText(requireContext(), "Greška: korisnik nije prijavljen", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            val user = db.userDao().getUserByUsername(username!!)
            if (user != null) {
                currentUser = user
                fullNameEditText.setText(user.fullName)
                emailEditText.setText(user.email)
                passwordEditText.setText(user.password)
            }
        }

        saveButton.setOnClickListener {
            val fullName = fullNameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (currentUser != null) {
                val updatedUser = currentUser!!.copy(
                    fullName = fullName,
                    email = email,
                    password = password
                )

                lifecycleScope.launch {
                    val db = AppDatabase.getDatabase(requireContext())
                    db.userDao().updateUser(updatedUser)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Podaci su spremljeni", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                }
            }

        }
        }
    }

