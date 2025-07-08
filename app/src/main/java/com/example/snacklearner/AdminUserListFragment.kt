package com.example.snacklearner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.snacklearner.data.AppDatabase
import com.example.snacklearner.data.UserEntity
import kotlinx.coroutines.launch

class AdminUserListFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_admin_user_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewUsers)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = UserListAdapter(emptyList()) { user ->
            deleteUser(user)
        }

        recyclerView.adapter = adapter
        loadUsers()
    }

    private fun loadUsers() {
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            val users = db.userDao().getAllUsers()
            adapter.updateUsers(users)
        }
    }

    private fun deleteUser(user: UserEntity) {
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            db.userDao().deleteUser(user)
            loadUsers()
            Toast.makeText(context, "Korisnik obrisan", Toast.LENGTH_SHORT).show()
        }
    }
}
