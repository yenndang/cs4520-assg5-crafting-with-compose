package com.cs4520.assignment5.old_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cs4520.assignment5.R
import com.cs4520.assignment5.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    // Using View Binding to access the views
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogin.setOnClickListener {
            val username = binding.editTextUsername.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (username == "admin" && password == "admin") {
                // Clear fields
                binding.editTextUsername.text.clear()
                binding.editTextPassword.text.clear()
                // Navigate to ProductListFragment
                findNavController().navigate(R.id.action_loginFragment_to_productListFragment)
            } else {
                // Show toast message
                Toast.makeText(context, "Incorrect username or password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
