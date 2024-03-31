package com.cs4520.assignment5.old_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cs4520.assignment5.R
import com.cs4520.assignment5.api.AppDatabaseSingleton
import com.cs4520.assignment5.databinding.FragmentProductListBinding
import com.cs4520.assignment5.repository.ProductRepository
import com.cs4520.assignment5.viewmodel.ProductViewModel
import com.cs4520.assignment5.api.RetrofitInstance
import com.cs4520.assignment5.viewmodel.ProductViewModelFactory
import com.cs4520.assignment5.utils.Result

class ProductListFragment : Fragment() {
    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    // Initialize productAdapter with an empty list upfront
    private val productAdapter: ProductAdapter by lazy {
        ProductAdapter(mutableListOf())
    }

    private val viewModel: ProductViewModel by viewModels {
        ProductViewModelFactory(
            ProductRepository(
                RetrofitInstance.api,
                AppDatabaseSingleton.getDatabase(requireContext()).productDao(),
                requireContext()
            )
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeProducts()
        viewModel.fetchProducts() // fetches when view is created
    }

    // Update based on TA's feedback
    override fun onResume() { //to initiate an API fetch in the onResume lifecycle callback of an activity or fragment
        super.onResume()
        viewModel.fetchProducts() // fetches when fragment resumes

    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = productAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    if (!viewModel.isFetching && totalItemCount <= (lastVisibleItemPosition + 2)) {// VISIBLE_THRESHOLD = 2?
                        viewModel.loadMoreProducts()
                    }
                }
            })
        }
    }

    private fun observeProducts() {
        viewModel.productList.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    if (result.data.isEmpty() && productAdapter.itemCount == 0) {
                        binding.textViewEmpty.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE
                    } else {
                        binding.textViewEmpty.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                        productAdapter.updateProducts(result.data)
                    }
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    if (productAdapter.itemCount == 0) {
                        binding.textViewEmpty.visibility = View.VISIBLE
                        binding.textViewEmpty.text = result.exception.message ?: "An error occurred"
                        binding.recyclerView.visibility = View.GONE
                    } else {
                        Toast.makeText(context, "API error. Try scrolling down again for more content.", Toast.LENGTH_SHORT).show()
                    }
                }
                is Result.Empty -> {
                    binding.progressBar.visibility = View.GONE
                    if (productAdapter.itemCount == 0) {
                        binding.textViewEmpty.visibility = View.VISIBLE
                        binding.textViewEmpty.text = getString(R.string.scroll_again)
                        binding.recyclerView.visibility = View.GONE
                    } else {
                        Toast.makeText(context, "No products available. Try scrolling down again for more content.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}